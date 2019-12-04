package com.sarveshparab.analysers.imports;

import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.StringManipulator;
import com.sarveshparab.config.Conf;

import java.util.HashMap;
import java.util.List;

public class FileImportsPopulator {
    public static HashMap<String, List<String>> createFileImportMap(List<String> fileList){
        HashMap<String,List<String>> fileMap=new HashMap<>();

        for(String filePath: fileList) {
            String actualpath = Conf.ZK_PRE_CODE_BASE_PATH +
                    StringManipulator.packagePathToSysPath(filePath, "/") + Conf.FILE_EXT;
            String data = FileHandler.readLineByLine(actualpath);

        }

        return fileMap;
    }

}
