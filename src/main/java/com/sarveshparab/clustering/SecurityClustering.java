package com.sarveshparab.clustering;

import com.sarveshparab.analysers.classfile.ReflectionBasedAnalyser;
import com.sarveshparab.analysers.domain.DomainAnalyser;
import com.sarveshparab.analysers.domain.MatchType;
import com.sarveshparab.analysers.semantic.SemanticAnalyser;
import com.sarveshparab.config.Conf;
import com.sarveshparab.config.Params;
import com.sarveshparab.util.FileHandler;

import java.util.*;

public class SecurityClustering {

    private ReflectionBasedAnalyser reflectionBasedAnalyser;
    private SemanticAnalyser semanticAnalyser;
    private DomainAnalyser domainAnalyser;
    private List<String> filenames;

    public SecurityClustering(SemanticAnalyser semanticAnalyser, ReflectionBasedAnalyser reflectionBasedAnalyser,
                              DomainAnalyser domainAnalyser){
        this.semanticAnalyser = semanticAnalyser;
        this.reflectionBasedAnalyser = reflectionBasedAnalyser;
        this.domainAnalyser = domainAnalyser;
        filenames=new ArrayList<>();
    }

    public List<String> generateCluster(Set<String> smellyFiles){

        if (Params.LOAD_SECURITY_CLUSTERS && FileHandler.doesFileExists(Conf.SECURITY_CLUSTER_DUMP)){
            return FileHandler.readListFromFile(Conf.SECURITY_CLUSTER_DUMP);
        }

        int count = 0;

        for(String concernFile:smellyFiles) {
            Set<String> fileWords = reflectionBasedAnalyser.extractAllFeatures(concernFile, true);

            HashMap<Enum<MatchType>, List<String>> wordMatches = domainAnalyser.returnMatches(fileWords);

            System.out.println(concernFile+","+
                    wordMatches.get(MatchType.EXACT).size()+","+wordMatches.get(MatchType.SYNONYM).size()
                    +","+wordMatches.get(MatchType.HYBRID).size()+","+fileWords.size());

            if (wordMatches.get(MatchType.EXACT).size() >= Params.PRE_COMMIT_EMATCH_THRESHOLD
                    && wordMatches.get(MatchType.SYNONYM).size() >= Params.PRE_COMMIT_SMATCH_THRESHOLD){
                filenames.add(concernFile);
            } else if(wordMatches.get(MatchType.HYBRID).size() >= Params.PRE_COMMIT_HMATCH_THRESHOLD) {
                filenames.add(concernFile);
            }

            count++;
        }

        FileHandler.writeListToFile(Conf.SECURITY_CLUSTER_DUMP, filenames);

        return filenames;
    }

}
