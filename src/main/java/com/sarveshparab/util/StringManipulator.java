package com.sarveshparab.util;

import java.util.Arrays;
import java.util.List;

public class StringManipulator {

    public static String packagePathToSysPath(String packagePath, String sysDelim) {
        List<String> elems = Arrays.asList(packagePath.split("\\."));
        return String.join(sysDelim, elems);
    }

}
