package com.sarveshparab.analysers.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ImportsDiff {


    public List<String> getImportDifference(HashMap<String,List<String>> filemapEarlierVersion,HashMap<String,List<String>>
            filemapLaterVersion,String fileName1){
        List<String> imports1=filemapEarlierVersion.get(fileName1);
        List<String> imports2=filemapLaterVersion.get(fileName1);

        HashSet<String> importsOne=new HashSet<>(imports1);
        HashSet<String> importsTwo=new HashSet<>(imports2);

        imports2.removeAll(imports1);
        return new ArrayList<>(imports2);

    }
}
