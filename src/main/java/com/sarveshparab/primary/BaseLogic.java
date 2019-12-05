package com.sarveshparab.primary;

import com.sarveshparab.analysers.git.CommitFileObject;
import com.sarveshparab.analysers.git.GitCommitAnalyser;
import com.sarveshparab.analysers.semantic.SemanticAnalyser;
import com.sarveshparab.config.Conf;

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
//        List<String> filelist=new ArrayList<>();
//        filelist.add("org.apache.zookeeper.server.quorum.QuorumPeerConfig");
//        List<String> bannedList= Arrays.asList(FileHandler.splitFileLineByLine(FileHandler.readLineByLine("/Users/ameyakulkarni/Downloads/development/had/USC-CSCI578/src/main/java/com/sarveshparab/domain/banned.txt")));
////        for(String i:bannedList){
////            System.out.println(i);
////        }
//        FileImportsPopulator f=new FileImportsPopulator();
//        f.createFileImportMap(filelist);
//        SemanticAnalyser semanticAnalyser = new SemanticAnalyser();
//        semanticAnalyser.analyze();

//
//        GitCommitAnalyser gitCommitAnalyser = new GitCommitAnalyser();
//        gitCommitAnalyser.gitRemoteClone(Conf.ZK_REMOTE_GIT_URL,Conf.ZK_REMOTE_GIT_REPO);
//        String commitMessage = gitCommitAnalyser.getCommitMessage("5a29daedeb5ac7e9e2af87ce1a7bbfad230d5c86");
//        CommitFileObject commitFileObject = gitCommitAnalyser.getCommitFiles("5a29daedeb5ac7e9e2af87ce1a7bbfad230d5c86");
//        System.out.println(commitFileObject.getAddedFiles());
//        gitCommitAnalyser.deleteGitRemoteClone(Conf.ZK_REMOTE_GIT_REPO);
    }
}
