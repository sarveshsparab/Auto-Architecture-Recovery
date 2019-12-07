package com.sarveshparab.primary;

import com.sarveshparab.analysers.classfile.ReflectionBasedAnalyser;
import com.sarveshparab.analysers.domain.DomainAnalyser;
import com.sarveshparab.analysers.domain.MatchType;
import com.sarveshparab.analysers.git.CommitContent;
import com.sarveshparab.analysers.git.GitCommitAnalyser;
import com.sarveshparab.analysers.imports.FileImportsPopulator;
import com.sarveshparab.analysers.semantic.SemanticAnalyser;
import com.sarveshparab.analysers.smells.SmellAnalyser;
import com.sarveshparab.clustering.SecurityClustering;
import com.sarveshparab.config.Conf;
import com.sarveshparab.config.Params;
import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.StringManipulator;

import java.util.*;

public class CommandCentral {

    private static SemanticAnalyser semanticAnalyser=new SemanticAnalyser();
    private static ReflectionBasedAnalyser preReflectAnalyser = new ReflectionBasedAnalyser(semanticAnalyser,true);
    private static ReflectionBasedAnalyser postReflectAnalyser = new ReflectionBasedAnalyser(semanticAnalyser,false);
    private static SmellAnalyser smellAnalyser = new SmellAnalyser();
    private static DomainAnalyser domainAnalyser = new DomainAnalyser(semanticAnalyser);
    private static SecurityClustering securityClustering = new SecurityClustering(semanticAnalyser,
            preReflectAnalyser, domainAnalyser);
    private static FileImportsPopulator fileImportsPopulator = new FileImportsPopulator();
    private static GitCommitAnalyser gitCommitAnalyser = new GitCommitAnalyser();

    public CommandCentral() {
    }

    public static void main(String args[]) {

        // Smelly file analysis
        Set<String> smellyFiles = null;
        if (Params.LOAD_SMELL_ANALYSIS && FileHandler.doesFileExists(Conf.SMELLY_ANALYSIS_DUMP)){
            smellyFiles = FileHandler.readSetFromFile(Conf.SMELLY_ANALYSIS_DUMP);
        } else {
            smellAnalyser.runSmellAnalyser(Conf.ZK_PRE_SMELL_FILE);
            smellyFiles = smellAnalyser.getSmellyFiles();
            FileHandler.writeSetToFile(Conf.SMELLY_ANALYSIS_DUMP, smellyFiles);
        }

        // Build security related files cluster
        List<String> secFiles = securityClustering.generateCluster(smellyFiles);

        // Analyse import statements across versions of subject system
        Set<String> secFilesPostImportAnalysis = new HashSet<>();
        for (String secFile : secFiles) {

            Set<String> importsFromPost = fileImportsPopulator.getFileImports(secFile, false);
            Set<String> importsFromPre = fileImportsPopulator.getFileImports(secFile, true);

            importsFromPost.removeAll(importsFromPre);
            secFilesPostImportAnalysis.addAll(importsFromPost);
        }

        secFilesPostImportAnalysis.addAll(secFiles);
        FileHandler.writeSetToFile(Conf.SECURITY_CLUSTER_DUMP_WITH_IMPORTS_DIFF, secFilesPostImportAnalysis);

        // Build git commit analysis on security cluster files
        Map<String, CommitContent> commitContentMap = null;
        if (Params.LOAD_COMMIT_ANALYSIS && FileHandler.doesFileExists(Conf.COMMIT_CONTENT_DUMP)) {
            System.out.println("Loaded Pre-analyzed Commit files");
            commitContentMap = FileHandler.loadFromJson(Conf.COMMIT_CONTENT_DUMP);
        } else {
            gitCommitAnalyser.gitRemoteClone(Conf.ZK_REMOTE_GIT_URL, Conf.ZK_REMOTE_GIT_REPO);

            Map<String, Integer> commitFreqMap = gitCommitAnalyser.buildFreqMap(secFilesPostImportAnalysis);
            commitContentMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : commitFreqMap.entrySet()){
                CommitContent cc = new CommitContent();

                cc.setId(entry.getKey());
                cc.setMessage(gitCommitAnalyser.getCommitMessage(entry.getKey()));
                cc.setCommitURL(Conf.ZK_COMMIT_BASE_URL + entry.getKey());
                cc.setSecFilesCount(entry.getValue());
                cc.setAffectedFiles(gitCommitAnalyser.getCommitFiles(entry.getKey()).getAffectedFiles());
                cc.setAffectedFilesCount(cc.getAffectedFiles().size());
                cc.setAnalysedMsg(semanticAnalyser.processString(cc.getMessage()));
                cc.setIssueId(StringManipulator.extractIssueId(cc.getMessage()));

                Set<String> secFilesIntersect = new HashSet<>(secFilesPostImportAnalysis);
                secFilesIntersect.retainAll(cc.getAffectedFiles());

                cc.setSecFiles(secFilesIntersect);

                commitContentMap.put(entry.getKey(), cc);
            }

            FileHandler.saveToJson(Conf.COMMIT_CONTENT_DUMP, commitContentMap);

            gitCommitAnalyser.deleteGitRemoteClone(Conf.ZK_REMOTE_GIT_REPO);
        }

        // Post git-commit analysis
        Map<String, CommitContent> commitContentSecThresholdMap = new HashMap<>();
        Map<String, CommitContent> commitContentSecMsgMap = new HashMap<>();

        if (Params.LOAD_POST_GIT_ANALYSIS && (FileHandler.doesFileExists(Conf.COMMIT_CONTENT_BREACHING_THRESHOLD)
                && FileHandler.doesFileExists(Conf.COMMIT_CONTENT_MSG_BREACHING_THRESHOLD))){
            commitContentSecThresholdMap = FileHandler.loadFromJson(Conf.COMMIT_CONTENT_BREACHING_THRESHOLD);
            commitContentSecMsgMap = FileHandler.loadFromJson(Conf.COMMIT_CONTENT_MSG_BREACHING_THRESHOLD);
        } else {
            for (Map.Entry<String, CommitContent> entry : commitContentMap.entrySet()) {
                CommitContent cc = entry.getValue();

                // Consider commits only with more than a threshold of security related files
                if (cc.getSecFilesCount() > Params.COMMIT_FREQ_THRESHOLD) {
                    commitContentSecThresholdMap.put(cc.getId(), cc);
                    HashMap<Enum<MatchType>, List<String>> matchedWords = domainAnalyser.returnMatches(cc.getAnalysedMsg());

                    // Consider commits only whose commit message has a certain amount of security related words
                    if (matchedWords.get(MatchType.EXACT).size() >= Params.POST_COMMIT_EMATCH_THRESHOLD) {
                        commitContentSecMsgMap.put(cc.getId(), cc);
                    }
                }
            }

            FileHandler.saveToJson(Conf.COMMIT_CONTENT_BREACHING_THRESHOLD, commitContentSecThresholdMap);
            FileHandler.saveToJson(Conf.COMMIT_CONTENT_MSG_BREACHING_THRESHOLD, commitContentSecMsgMap);
        }

        if (Params.COMPUTE_AFFECTED_FILES_STATS){
            Map<String, Map<Enum<MatchType>, Integer>> commitMatchTypeFreq = new HashMap<>();
            for (Map.Entry<String, CommitContent> entry : commitContentSecMsgMap.entrySet()){
                CommitContent cc = entry.getValue();

                Map<Enum<MatchType>, Integer> matchTypeFreq = new HashMap<>();
                int eMatch = 0, sMatch = 0, hMatch = 0;

                for(String affFile : cc.getAffectedFiles()){
                    Set<String> analysedWords = postReflectAnalyser.extractAllFeatures(affFile, false);
                    HashMap<Enum<MatchType>, List<String>> extractedWords = domainAnalyser.returnMatches(analysedWords);
                    eMatch += extractedWords.get(MatchType.EXACT).size();
                    sMatch += extractedWords.get(MatchType.SYNONYM).size();
                    hMatch += extractedWords.get(MatchType.HYBRID).size();
                }

                matchTypeFreq.put(MatchType.EXACT, eMatch);
                matchTypeFreq.put(MatchType.SYNONYM, sMatch);
                matchTypeFreq.put(MatchType.HYBRID, hMatch);
                commitMatchTypeFreq.put(cc.getId(), matchTypeFreq);
            }
        }

        if(Params.GENERATE_FINAL_CLUSTERS){
            for (Map.Entry<String, CommitContent> entry : commitContentSecMsgMap.entrySet()){
                String cId = entry.getKey();
                CommitContent cc = entry.getValue();

                HashMap<Enum<MatchType>, List<String>> matchedWords = domainAnalyser.returnMatches(cc.getAnalysedMsg());
                if (matchedWords.get(MatchType.EXACT).size() >= Params.FINAL_CLUSTER_EMATCH_THRESHOLD){
                    FileHandler.saveToJson(Conf.FINAL_CLUSTERS + cId + ".json", cc);
                }
            }
        }

    }

}
