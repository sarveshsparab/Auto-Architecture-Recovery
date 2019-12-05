package com.sarveshparab.analysers.semantic.metrics;

import java.util.Iterator;
import java.util.List;

public class Scorer {

    public Double cosSim(List<Double> vect1, List<Double> vect2, int len){
        Double sim=0.0;
        Double len1 = vectLength(vect1), len2 = vectLength(vect2);
        if(len1!=0 && len2!=0)
            sim = vectDotProduct(vect1, vect2, len)/(len1*len2);
        return sim;
    }

    private Double vectLength(List<Double> vect){
        Double len=0.0,data;
        Iterator<Double> itr = vect.iterator();
        while(itr.hasNext()){
            data = (Double)itr.next();
            len += data*data;
        }
        return Math.sqrt(len);
    }

    private Double vectDotProduct(List<Double> vect1,List<Double> vect2,int len){
        Double prod=0.0;
        int i;
        for(i=0;i<len;i++){
            prod += vect1.get(i)*vect2.get(i);
        }
        return prod;
    }
}
