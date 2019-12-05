package com.sarveshparab.util;

import java.util.Arrays;
import java.util.List;

public class StringManipulator {

    public static String packagePathToSysPath(String packagePath, String sysDelim) {
        List<String> elems = Arrays.asList(packagePath.split("\\."));
        return String.join(sysDelim, elems);
    }

    public static String listToString(List<String> s, String delimiter){
        StringBuffer ret = new StringBuffer("");
        for (int i = 0; s != null && i < s.size(); i++) {
            ret.append(s.get(i));
            if (i < s.size() - 1) {
                ret.append(delimiter);
            }
        }
        return ret.toString();
    }

    public static List<String> removeCamelCase(String str){
        List<String> postRemoval = Arrays.asList(str.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"));


        return postRemoval;
    }

}
