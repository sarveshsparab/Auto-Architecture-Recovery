package com.sarveshparab.clustering;

import com.sarveshparab.analysers.classfile.ReflectionBasedAnalyser;
import com.sarveshparab.analysers.domain.DomainAnalyser;
import com.sarveshparab.analysers.domain.MatchType;
import com.sarveshparab.analysers.semantic.SemanticAnalyser;
import java.util.*;

public class SecurityClustering {

    private ReflectionBasedAnalyser reflectionBasedAnalyser;
    private SemanticAnalyser semanticAnalyser;
    private List<String> filenames;

    public SecurityClustering(){
        semanticAnalyser=new SemanticAnalyser();
        reflectionBasedAnalyser=new ReflectionBasedAnalyser(semanticAnalyser,true);
        filenames=new ArrayList<>();
    }


    public List<String> generateCluster(Set<String> smellyFiles){

        int count = 0;

        for(String concernFile:smellyFiles) {
            List<String> fileWords = reflectionBasedAnalyser.extractAllFeatures(concernFile);
            //pass file words to domain analyser
            DomainAnalyser domainAnalyser = new DomainAnalyser(semanticAnalyser);
            //receive output from domain in a map
            HashMap<Enum<MatchType>, List<String>> wordMacthes = domainAnalyser.returnMatches(fileWords);

            if (wordMacthes.get(MatchType.EXACT).size() >= 1 || wordMacthes.get(MatchType.SYNONYM).size() >= 10
            || wordMacthes.get(MatchType.HYBRID).size()>=10) {
                filenames.add(concernFile);
            }

            count++;
            System.out.println("Processing : " + count + " / " + smellyFiles.size());
        }

        return filenames;
    }

}
