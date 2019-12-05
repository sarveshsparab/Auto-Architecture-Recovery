package com.sarveshparab.analysers.semantic.similarity;

import com.sarveshparab.analysers.semantic.linguistic.SimAlgo;
import com.sarveshparab.util.Misc;
import edu.cmu.lti.ws4j.RelatednessCalculator;

import java.util.List;

public class WordSimilarity {

    public WordSimilarity() {
    }

    public double calcSim(String word1, String word2, SimAlgo simAlgo) {
        RelatednessCalculator rc = Misc.rcList.get(simAlgo.useAlgo());
        return rc.calcRelatednessOfWords(word1, word2);
    }

    public double[] calcSimMulti(String word1, String word2, List<SimAlgo> simAlgoList) {
        double[] scores = new double[7];

        for (int i = 0; i < simAlgoList.size(); i++) {
            scores[i] = calcSim(word1, word2, simAlgoList.get(i));
        }

        return scores;
    }

}
