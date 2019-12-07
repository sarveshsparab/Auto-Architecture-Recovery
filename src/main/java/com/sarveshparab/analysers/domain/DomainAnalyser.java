package com.sarveshparab.analysers.domain;

import com.sarveshparab.analysers.semantic.SemanticAnalyser;
import com.sarveshparab.analysers.semantic.linguistic.SimAlgo;
import com.sarveshparab.analysers.semantic.similarity.SentenceSimilarityAlgorithms;
import com.sarveshparab.config.Conf;
import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.Misc;

import java.util.*;
import java.util.stream.Collectors;

public class DomainAnalyser {
    private HashSet<String> domainSet;
    private String fileContent;
    private List<String> data;
    private SemanticAnalyser semanticAnalyser;
    private List<SimAlgo> similarityAlgos;
    private SentenceSimilarityAlgorithms sentenceSimilarityAlgorithms;

    public DomainAnalyser(SemanticAnalyser semanticAnalyser){
        fileContent= FileHandler.readLineByLine(Conf.SECURITY_DOMAIN_FILE_PATH);
        data= Arrays.asList(FileHandler.splitFileLineByLine(fileContent));
        domainSet=new HashSet<>(data);
        this.semanticAnalyser=semanticAnalyser;
        similarityAlgos=new ArrayList<>();
        sentenceSimilarityAlgorithms=new SentenceSimilarityAlgorithms();
    }

    public HashMap<Enum<MatchType> , List<String>> returnMatches(Set<String> fileWords){
      HashMap<Enum<MatchType>,List<String>> returnMap=new HashMap<>();
      List<String> exactMatches=new ArrayList<>();
      List<String> synonymMatches=new ArrayList<>();
      List<String> hybridMatches=new ArrayList<>();
      populateAlgo(similarityAlgos);

      domainSet = (HashSet<String>) domainSet.stream().map(String::toLowerCase).collect(Collectors.toSet());

      process:for(String word:fileWords){
          word=word.toLowerCase();
          if(domainSet.contains(word)){

              exactMatches.add(word);
              continue;
          }
          for(String domainWord:domainSet){
              double sim=semanticAnalyser.simWords(word,domainWord, SimAlgo.Lin) / Misc.algoMax.get(SimAlgo.Lin.useAlgo());
              double hybridSim=sentenceSimilarityAlgorithms.avgSimilarity(word,domainWord);
              if(sim>0.30){
                  synonymMatches.add(word);
                  continue process;
              }
              else if(hybridSim>0.80){
                  hybridMatches.add(word);
              }

          }

      }

      returnMap.put(MatchType.EXACT,exactMatches);
      returnMap.put(MatchType.SYNONYM,synonymMatches);
      returnMap.put(MatchType.HYBRID,hybridMatches);

      return returnMap;
    }


    private void populateAlgo(List<SimAlgo> saList){
        saList.add(SimAlgo.WuPalmer);
        saList.add(SimAlgo.Resnik);
        saList.add(SimAlgo.JiangConrath);
        saList.add(SimAlgo.Lin);
        saList.add(SimAlgo.LeacockChodorow);
        saList.add(SimAlgo.Path);
        saList.add(SimAlgo.Lesk);
    }







}
