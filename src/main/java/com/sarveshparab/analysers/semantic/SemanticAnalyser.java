package com.sarveshparab.analysers.semantic;

import com.sarveshparab.analysers.semantic.dictionary.Trie;
import com.sarveshparab.analysers.semantic.linguistic.PLKeyWords;
import com.sarveshparab.analysers.semantic.linguistic.SimAlgo;
import com.sarveshparab.analysers.semantic.linguistic.StopWordPunc;
import com.sarveshparab.analysers.semantic.similarity.SentenceSimilarity;
import com.sarveshparab.analysers.semantic.similarity.WordSimilarity;
import com.sarveshparab.config.Conf;
import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.StringManipulator;

import java.io.IOException;
import java.util.*;

public class SemanticAnalyser {
    private Map<String, String> fileContentsMap;
    private Trie trie = new Trie();

    private WordSimilarity wordSimilarity;
    private SentenceSimilarity sentenceSimilarity;

    private StopWordPunc stopWordPunc;
    private PLKeyWords plKeyWords;

    public SemanticAnalyser() {
        this.fileContentsMap = new HashMap<>();

        try {
            trie.buildDict();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordSimilarity = new WordSimilarity();
        sentenceSimilarity = new SentenceSimilarity(trie);
        stopWordPunc = new StopWordPunc(trie);
        plKeyWords = new PLKeyWords();
    }

    private void analyze() {
        // Fetch all the smelly files
        List<String> smellyFiles = new ArrayList<>(); // connect to sonali
        smellyFiles.add("org.apache.zookeeper.server.quorum.QuorumPeerConfig");
        smellyFiles.add("org.apache.zookeeper.server.quorum.flexible.QuorumHierarchical");

        // Read the contents of the smelly files
        for (String smellyFile : smellyFiles) {
            String fileLoc = Conf.ZK_PRE_CODE_BASE_PATH +
                    StringManipulator.packagePathToSysPath(smellyFile, "/") + Conf.FILE_EXT;
            String fileContents = FileHandler.readLineByLine(fileLoc);
            fileContentsMap.put(smellyFile, fileContents);
        }

    }

    public double simJavaFiles(String file1, String file2, SimAlgo simAlgo, Boolean hasPackagePath){
        if(hasPackagePath){
            file1 = Conf.ZK_PRE_CODE_BASE_PATH + StringManipulator.packagePathToSysPath(file1, "/") + Conf.FILE_EXT;
            file2 = Conf.ZK_PRE_CODE_BASE_PATH + StringManipulator.packagePathToSysPath(file2, "/") + Conf.FILE_EXT;
        }
        String sent1 = FileHandler.readLineByLine(file1);
        String sent2 = FileHandler.readLineByLine(file2);

        return simSentence(sent1, sent2, simAlgo);
    }

    public double simSentence(String sent1, String sent2, SimAlgo simAlgo){
        return sentenceSimilarity.calcSim(sent1, sent2, simAlgo);
    }

    public double simWords(String word1, String word2, SimAlgo simAlgo){
        return wordSimilarity.calcSim(word1, word2, simAlgo);
    }

    public double[] simSentenceMulti(String sent1, String sent2, List<SimAlgo> simAlgo){
        return sentenceSimilarity.calcSimMulti(sent1, sent2, simAlgo);
    }

    public double[] simWordsMulti(String word1, String word2, List<SimAlgo> simAlgo){
        return wordSimilarity.calcSimMulti(word1, word2, simAlgo);
    }

    public List<String> removeStopWords(List<String> words){
        return stopWordPunc.removeStop(words);
    }

    public List<String> removeStopWords(Set<String> words) {
        return removeStopWords(new ArrayList<String>(words));
    }

    public List<String> removePLWords(List<String> words){
        return plKeyWords.removeJavaWords(plKeyWords.removeCWords(words));
    }

    public List<String> removePLWords(Set<String> words){
        return removePLWords(new ArrayList<>(words));
    }

}