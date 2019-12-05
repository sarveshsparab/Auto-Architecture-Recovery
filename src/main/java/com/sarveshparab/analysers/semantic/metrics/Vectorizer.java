package com.sarveshparab.analysers.semantic.metrics;

import com.sarveshparab.analysers.semantic.linguistic.Relevance;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @category Class
 * @purpose For creating vectors from tokens
 * @author Sarvesh
 */
public class Vectorizer {
    /**
     * @category Constructor
     * @author Sarvesh
     */
    public Vectorizer(){

    }
    /**
     * @category Function
     * @argument str : linked list of tokens in a string
     * @argument union : linked list of tokens formed after union of two strings
     * @argument relevance : object which sets which algo to use
     * @argument algoMax : Maximum value for the algo used
     * @return A vector in form of linked list based on algo
     * @author Sarvesh
     */
    public List<Double> vectorize(List<String> str, List<String> union, Relevance relevance, Double algoMax){
        int i;
        List <Double> vect = new LinkedList<Double>();
        Set<String> hashStr = new HashSet<String>();
        hashStr.addAll(str);

        for(i=0;i<union.size();i++){
            String unionWord = union.get(i);
            if(hashStr.contains(unionWord)){
                vect.add(i, algoMax);
            }else{
                Double rel,max = 0.0;
                Iterator<String> itr = hashStr.iterator();
                while(itr.hasNext()){
                    rel = relevance.calcRelatedness(unionWord, (String)itr.next());
                    max = rel > max ? rel : max;
                }
                vect.add(i, max);
            }
        }
        return vect;
    }

}
