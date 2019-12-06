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

        Set<String> exactWords = new HashSet<>();


        for(String concernFile:smellyFiles) {
            Set<String> fileWords = reflectionBasedAnalyser.extractAllFeatures(concernFile, true);
            //pass file words to domain analyser
            DomainAnalyser domainAnalyser = new DomainAnalyser(semanticAnalyser);
            //receive output from domain in a map
            HashMap<Enum<MatchType>, List<String>> wordMatches = domainAnalyser.returnMatches(fileWords);

            System.out.println(concernFile+","+
                    wordMatches.get(MatchType.EXACT).size()+","+wordMatches.get(MatchType.SYNONYM).size()
                    +","+wordMatches.get(MatchType.HYBRID).size()+","+fileWords.size());

            if (wordMatches.get(MatchType.EXACT).size() >= 3 && wordMatches.get(MatchType.SYNONYM).size() >= 2){
                filenames.add(concernFile);
                exactWords.addAll(wordMatches.get(MatchType.EXACT));
            } else if(wordMatches.get(MatchType.HYBRID).size()>=10) {
                filenames.add(concernFile);
            }

            count++;
//            System.out.println("Processing : " + count + " / " + smellyFiles.size());
        }

        return filenames;
    }

}
