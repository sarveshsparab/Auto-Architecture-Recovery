package com.sarveshparab.primary;

import com.sarveshparab.analysers.classfile.ReflectionBasedAnalyser;
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
    private static ReflectionBasedAnalyser reflectionBasedAnalyser = new ReflectionBasedAnalyser(semanticAnalyser,true);
    private static SmellAnalyser smellAnalyser = new SmellAnalyser();
    private static SecurityClustering securityClustering = new SecurityClustering(semanticAnalyser, reflectionBasedAnalyser);
    private static FileImportsPopulator fileImportsPopulator = new FileImportsPopulator();
    private static GitCommitAnalyser gitCommitAnalyser = new GitCommitAnalyser();

    public CommandCentral() {
    }

    public static void main(String args[]) {

        Set<String> smellyFiles = null;
        if (Params.LOAD_SMELL_ANALYSIS && FileHandler.doesFileExists(Conf.SMELLY_ANALYSIS_DUMP)){
            smellyFiles = FileHandler.readSetFromFile(Conf.SMELLY_ANALYSIS_DUMP);
        } else {
            smellAnalyser.runSmellAnalyser(Conf.ZK_PRE_SMELL_FILE);
            smellyFiles = smellAnalyser.getSmellyFiles();
        }

        FileHandler.writeSetToFile(Conf.SMELLY_ANALYSIS_DUMP, smellyFiles);

        List<String> secFiles = securityClustering.generateCluster(smellyFiles, true);

        Set<String> importsDiff = new HashSet<>();
        for (String secFile : secFiles) {

            Set<String> importsFromPost = fileImportsPopulator.getFileImports(secFile, false);
            Set<String> importsFromPre = fileImportsPopulator.getFileImports(secFile, true);

            importsFromPost.removeAll(importsFromPre);
            importsDiff.addAll(importsFromPost);
        }

        importsDiff.addAll(secFiles);

        FileHandler.writeSetToFile(Conf.SECURITY_CLUSTER_DUMP_WITH_IMPORTS_DIFF, importsDiff);

        Map<String, CommitContent> commitContentMap = null;
        if (Params.LOAD_COMMIT_ANALYSIS && FileHandler.doesFileExists(Conf.COMMIT_CONTENT_DUMP)) {
            commitContentMap = FileHandler.loadFromJson(Conf.COMMIT_CONTENT_DUMP);
        } else {
            gitCommitAnalyser.gitRemoteClone(Conf.ZK_REMOTE_GIT_URL, Conf.ZK_REMOTE_GIT_REPO);

            Map<String, Integer> commitFreqMap = gitCommitAnalyser.buildFruequencyMap(importsDiff);
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

                commitContentMap.put(entry.getKey(), cc);
            }

            FileHandler.saveToJson(Conf.COMMIT_CONTENT_DUMP, commitContentMap);

            gitCommitAnalyser.deleteGitRemoteClone(Conf.ZK_REMOTE_GIT_REPO);
        }
    }

}
