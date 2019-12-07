package com.sarveshparab.analysers.semantic.similarity;


import com.sarveshparab.analysers.semantic.dictionary.Trie;
import com.sarveshparab.analysers.semantic.linguistic.*;
import com.sarveshparab.analysers.semantic.metrics.Scorer;
import com.sarveshparab.analysers.semantic.metrics.Vectorizer;
import com.sarveshparab.util.Misc;
import com.sarveshparab.util.StringManipulator;

import java.util.*;

public class SentenceSimilarity {

    private Trie trie;
    private Lemmatizer lemmatizer;
    private StopWordPunc stopword;
    private Tokenizer tokenizer;
    private Stemmer stemmer;
    private Vectorizer vectorizer;
    private Scorer scorer;

    public SentenceSimilarity(Trie trie) {
        this.trie = trie;
        lemmatizer = new Lemmatizer();
        stopword = new StopWordPunc(trie);
        tokenizer = new Tokenizer();
        stemmer = new Stemmer();
        vectorizer = new Vectorizer();
        scorer = new Scorer();
    }

    public double calcSim(String sent1, String sent2, SimAlgo simAlgo) {

        List<String> token1 = processString(sent1);
        List<String> token2 = processString(sent2);

        List<String> tokenUnion = Misc.listUnion(token1, token2);

        List<Double> vect1 = vectorizer.vectorize(token1, tokenUnion, new Relevance(Misc.db, Misc.rcList.get(simAlgo.useAlgo())),
                Misc.algoMax.get(simAlgo.useAlgo()));
        List<Double> vect2 = vectorizer.vectorize(token2,tokenUnion,new Relevance(Misc.db, Misc.rcList.get(simAlgo.useAlgo())),
                Misc.algoMax.get(simAlgo.useAlgo()));

        return scorer.cosSim(vect1, vect2, tokenUnion.size());
    }

    public double[] calcSimMulti(String sent1, String sent2, List<SimAlgo> simAlgoList) {
        double[] scores = new double[7];

        for (int i = 0; i < simAlgoList.size(); i++) {
            scores[i] = calcSim(sent1, sent2, simAlgoList.get(i));
        }

        return scores;
    }

    public List<String> processString(String sent) {
        List<String> strTokens;

        strTokens = lemmatizer.lemmatize(sent);
        strTokens = stopword.removeStop(strTokens);
        strTokens = tokenizer.tokenize(StringManipulator.listToString(strTokens, " "));
        strTokens = stopword.removePunc(strTokens);
        strTokens = stemmer.stemmatize(strTokens,trie);
        Set<String> strUserHash = new HashSet<String>(strTokens);
        strTokens = new LinkedList<String>(strUserHash);
        Collections.sort(strTokens,String.CASE_INSENSITIVE_ORDER);

        return strTokens;
    }
}
