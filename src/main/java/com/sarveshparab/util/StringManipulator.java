package com.sarveshparab.util;

import com.sarveshparab.config.Conf;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManipulator {

    public static String packagePathToSysPath(String packagePath, String sysDelim) {
        List<String> elems = Arrays.asList(packagePath.split("\\."));
        return String.join(sysDelim, elems);
    }


    public static String sysPathToPackagePath(String sysPath){
        String fileName;
        if(Conf.JAVA_FILE_EXT.equals(sysPath.substring(sysPath.length() - 5))) {
            fileName = sysPath
                    .replace(Conf.ZK_REMOTE_SRC_PATH, "")
                    .replace(Conf.ZK_REMOTE_TEST_PATH, "")
                    .replace(Conf.JAVA_FILE_EXT, "");
        }else{
            fileName = "";
        }
        List<String> elems = Arrays.asList(fileName.split("\\/"));
        return String.join(".", elems);

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

    public static String changeDotToUnderscore(String str){
        return str.replaceAll("\\.", "_");
    }

    public static String changeUnderscoreToDot(String str){
        return str.replaceAll("_", "\\.");
    }


    public static String extractIssueId(String commitMessage){

        String issueId = null;

        if(commitMessage.contains("ZOOKEEPER-")){
            Pattern pattern = Pattern.compile("(ZOOKEEPER-)\\w+");
            Matcher matcher = pattern.matcher(commitMessage);
            if (matcher.find())
            {
                issueId = matcher.group();
            }

        }


        return issueId;
    }

}
