package com.sarveshparab.analysers.semantic.linguistic;

import com.sarveshparab.config.Conf;
import com.sarveshparab.util.FileHandler;

import java.util.*;

public class PLKeyWords {

    private Set<String> javaKeyWords;
    private Set<String> cKeyWords;

    public PLKeyWords() {
        javaKeyWords = FileHandler.readListFromFile(Conf.JAVA_KEY_WORDS_FILE);
        cKeyWords = FileHandler.readListFromFile(Conf.C_KEY_WORDS_FILE);
    }

    public List<String> removeJavaWords(List<String> words){
        List<String> afterRemoval = new LinkedList<String>();
        Iterator<String> itr = words.iterator();
        while(itr.hasNext()) {
            String element = itr.next();
            if(javaKeyWords.contains(element.toLowerCase())){

            }else{
                afterRemoval.add(element);
            }
        }
        return afterRemoval;
    }

    public List<String> removeCWords(List<String> words){
        List<String> afterRemoval = new LinkedList<String>();
        Iterator<String> itr = words.iterator();
        while(itr.hasNext()) {
            String element = itr.next();
            if(cKeyWords.contains(element.toLowerCase())){

            }else{
                afterRemoval.add(element);
            }
        }
        return afterRemoval;
    }
}
