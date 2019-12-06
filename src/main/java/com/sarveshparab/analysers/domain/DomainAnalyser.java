package com.sarveshparab.analysers.domain;

import com.sarveshparab.analysers.semantic.SemanticAnalyser;
import com.sarveshparab.analysers.semantic.linguistic.SimAlgo;
import com.sarveshparab.config.Conf;
import com.sarveshparab.util.FileHandler;

import java.util.*;

public class DomainAnalyser {
    //compare with domain file list
    //check for exact match and add to list of exact matches
    //check for synonym match(threshold based sim) algo 4 and dump to a list
    //use sonali's algo threshold based
    //map.get("exact")

    private HashSet<String> domainSet;
    private String fileContent;
    private List<String> data;
    private SemanticAnalyser semanticAnalyser;
    private List<SimAlgo> similarityAlgos;

    public DomainAnalyser(SemanticAnalyser semanticAnalyser){
        fileContent= FileHandler.readLineByLine(Conf.SECURITY_DOMAIN_FILE_PATH);
        data= Arrays.asList(FileHandler.splitFileLineByLine(fileContent));
        domainSet=new HashSet<>(data);
        this.semanticAnalyser=semanticAnalyser;
        similarityAlgos=new ArrayList<>();
    }

    public HashMap<Enum<MatchType> , List<String>> returnMatches(List<String> fileWords){
      HashMap<Enum<MatchType>,List<String>> returnMap=new HashMap<>();
      List<String> exactMatches=new ArrayList<>();
      List<String> synonymMatches=new ArrayList<>();
      List<String> hybridMatches=new ArrayList<>();
      populateAlgo(similarityAlgos);
      for(String word:fileWords){
          if(domainSet.contains(word)){
              exactMatches.add(word);
          }
      }

      for(String fileWord:fileWords){
            for(String domainWord:domainSet){
                double sim=semanticAnalyser.simWords(fileWord.toLowerCase(),domainWord.toLowerCase(),similarityAlgos.get(4));
                if(sim>0.60){
                    synonymMatches.add(fileWord);
                }
            }
      }

      returnMap.put(MatchType.EXACT,exactMatches);
      returnMap.put(MatchType.SYNONYM,synonymMatches);

      return returnMap;
    }


    public void populateAlgo(List<SimAlgo> saList){

        saList.add(SimAlgo.WuPalmer);
        saList.add(SimAlgo.Resnik);
        saList.add(SimAlgo.JiangConrath);
        saList.add(SimAlgo.Lin);
        saList.add(SimAlgo.LeacockChodorow);
        saList.add(SimAlgo.Path);
        saList.add(SimAlgo.Lesk);
    }







}
