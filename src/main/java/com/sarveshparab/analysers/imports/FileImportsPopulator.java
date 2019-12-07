package com.sarveshparab.analysers.imports;

import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.StringManipulator;
import com.sarveshparab.config.Conf;

import java.util.*;


public class FileImportsPopulator {


    public HashMap<String, Set<String>> createFileImportMap(List<String> fileList, boolean usePre) {
        HashMap<String, Set<String>> fileMap = new HashMap<>();

        List<String> bannedList = Arrays.asList(FileHandler.splitFileLineByLine(FileHandler.readLineByLine(Conf.BANNED_PACKAGE_PATH)));

        HashSet<String> bannedSet = new HashSet<>(bannedList);

        for (String filePath : fileList) {

            fileMap.put(filePath, new HashSet<>());

            String actualpath = "";
            if (usePre) {
                actualpath = Conf.ZK_PRE_CODE_BASE_PATH +
                        StringManipulator.packagePathToSysPath(filePath, "/") + Conf.JAVA_FILE_EXT;
            } else {
                actualpath = Conf.ZK_POST_CODE_BASE_PATH +
                        StringManipulator.packagePathToSysPath(filePath, "/") + Conf.JAVA_FILE_EXT;
            }

            if(!FileHandler.doesFileExists(actualpath)){
                continue;
            }

            String data = FileHandler.readLineByLine(actualpath);
            List<String> fileLines = Arrays.asList(FileHandler.splitFileLineByLine(data));

            for (String str : fileLines) {
                String[] linesplit = str.split(" ");

                if (linesplit.length != 0 && linesplit[0].equals("import")) {

                    List<String> importPackageSplit = Arrays.asList(linesplit[1].split("\\."));
                    boolean exclude = false;
                    for (String st : importPackageSplit) {
                        if (bannedSet.contains(st)) {
                            exclude = true;
                        }
                    }

                    if (!exclude) {
                        if (fileMap.get(filePath) == null) {
                            Set<String> importList = new HashSet<>();
                            importList.add(linesplit[1].substring(0, linesplit[1].length() - 1));
                            fileMap.put(filePath, importList);
                        } else {
                            Set<String> temp = fileMap.get(filePath);
                            temp.add(linesplit[1].substring(0, linesplit[1].length() - 1));
                            fileMap.put(filePath, temp);
                        }
                    }
                }
            }


        }

        return fileMap;

    }

    public Set<String> getFileImports(String filename, boolean usePre) {
        List<String> fileList = new ArrayList<>();
        fileList.add(filename);
        return createFileImportMap(fileList, usePre).get(filename);
    }

}
