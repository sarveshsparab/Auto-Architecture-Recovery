package com.sarveshparab.analysers.semantic.similarity;

import info.debatty.java.stringsimilarity.*;


public class SentenceSimilarityAlgorithms {

    public SentenceSimilarityAlgorithms(){

    }

    public double cosineSimilarity(String string1,String string2){
        Cosine cosineObeject = new Cosine();
        return cosineObeject.similarity(string1,string2);
    }

    public double jaccardSimilarity(String string1,String string2){
        Jaccard jaccardObject = new Jaccard();
        return jaccardObject.similarity(string1,string2);
    }

    public double jaroWinklerSimilarity(String string1,String string2){
        JaroWinkler jaroWinklerObject = new JaroWinkler();
        return jaroWinklerObject.similarity(string1,string2);
    }

    public double levensteinSimilarity(String string1,String string2){
        Levenshtein levenshteinObject = new Levenshtein();

        return (1/levenshteinObject.distance(string1,string2));
    }


    public double avgSimilarity(String string1,String string2){

        double sum = cosineSimilarity(string1,string2) +
                jaccardSimilarity(string1,string2)+
                jaroWinklerSimilarity(string1,string2)+
                levensteinSimilarity(string1,string2);


        return sum/4;
    }
}
