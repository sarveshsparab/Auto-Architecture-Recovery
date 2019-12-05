package com.sarveshparab.primary;


import com.sarveshparab.analysers.imports.FileImportsPopulator;
import com.sarveshparab.config.Conf;
import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.StringManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class BaseLogic {

    public static void main(String args[]){
//        String actualpath = Conf.ZK_PRE_CODE_BASE_PATH +
//                StringManipulator.packagePathToSysPath("org.apache.zookeeper.server.quorum.QuorumPeerConfig", "/") + Conf.FILE_EXT;
//        String data = FileHandler.readLineByLine(actualpath);
//        System.out.println(data);
        List<String> filelist=new ArrayList<>();
        filelist.add("org.apache.zookeeper.server.quorum.QuorumPeerConfig");
//        List<String> bannedList= Arrays.asList(FileHandler.splitFileLineByLine(FileHandler.readLineByLine("/Users/ameyakulkarni/Downloads/development/had/USC-CSCI578/src/main/java/com/sarveshparab/domain/banned.txt")));
////        for(String i:bannedList){
////            System.out.println(i);
////        }
        FileImportsPopulator f=new FileImportsPopulator();
        f.createFileImportMap(filelist);
    }
}
