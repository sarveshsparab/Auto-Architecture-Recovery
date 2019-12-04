package com.sarveshparab.analysers.imports;

import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.StringManipulator;
import com.sarveshparab.config.Conf;

import java.io.File;
import java.util.*;


public class FileImportsPopulator {


    public static HashMap<String, List<String>> createFileImportMap(List<String> fileList){
        HashMap<String,List<String>> fileMap=new HashMap<>();
        List<String> bannedList= Arrays.asList(FileHandler.splitFileLineByLine(FileHandler.readLineByLine(Conf.BANNED_PACKAGE_PATH)));
        HashSet<String> bannedSet=new HashSet<>(bannedList);
        for(String filePath: fileList) {
            String actualpath = Conf.ZK_PRE_CODE_BASE_PATH +
                    StringManipulator.packagePathToSysPath(filePath, "/") + Conf.FILE_EXT;
            String data = FileHandler.readLineByLine(actualpath);
            List<String> fileLines=Arrays.asList(FileHandler.splitFileLineByLine(data));

            for (String str:fileLines){
                String[] linesplit=str.split(" ");

                if(linesplit.length!=0 && linesplit[0].equals("import")){

                    List<String> importPackageSplit=Arrays.asList(linesplit[1].split("\\."));
                    boolean exclude=false;
                    for(String st:importPackageSplit){
                        if(bannedSet.contains(st)){
                            exclude=true;
                        }
                    }

                    for(String st: importPackageSplit){
                        if(!exclude){
                            if(fileMap.get(filePath)==null){
                                List<String> importList=new ArrayList<>();
                                importList.add(linesplit[1]);
                                fileMap.put(filePath,importList);
                            }
                            else{
                                List<String> temp=fileMap.get(filePath);
                                temp.add(linesplit[1]);
                                fileMap.put(filePath,temp);
                            }
                        }
                    }
                }
            }


        }

        return fileMap;
    }

}
