package com.sarveshparab.util;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.*;

import java.util.*;

public class Misc {

    public static ILexicalDatabase db = new NictWordNet();
    public static RelatednessCalculator wup = new WuPalmer(db);
    public static RelatednessCalculator res = new Resnik(db);
    public static RelatednessCalculator jcn = new JiangConrath(db);
    public static RelatednessCalculator lin = new Lin(db);
    public static RelatednessCalculator lch = new LeacockChodorow(db);
    public static RelatednessCalculator path = new Path(db);
    public static RelatednessCalculator lesk = new Lesk(db);
    public static RelatednessCalculator hso = new HirstStOnge(db);

    public static List<RelatednessCalculator> rcList;
    public static List<Double> algoMax;

    public static List<String> listUnion(List<String> list1, List<String> list2){
        List<String> union;
        Set<String> set = new HashSet<String>();
        set.addAll(list1);
        set.addAll(list2);
        union = new LinkedList<String>(set);
        Collections.sort(union,String.CASE_INSENSITIVE_ORDER);
        return union;
    }

    static {
        rcList = new LinkedList<RelatednessCalculator>();
        algoMax = new LinkedList<Double>();
        rcList.add(0, wup);
        rcList.add(1, res);
        rcList.add(2, jcn);
        rcList.add(3, lin);
        rcList.add(4, lch);
        rcList.add(5, path);
        rcList.add(6, lesk);
        rcList.add(7, hso);
        algoMax.add(0, 1.0);
        algoMax.add(1, 15.0);
        algoMax.add(2, 100.0);
        algoMax.add(3, 1.0);
        algoMax.add(4, 5.0);
        algoMax.add(5, 1.0);
        algoMax.add(6, 100.0);
        algoMax.add(7, 16.0);
    }

}
