package com.sarveshparab.analysers.semantic;

import com.sarveshparab.config.Conf;
import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.StringManipulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyser {
    private Map<String, String> fileContentsMap;

    public SemanticAnalyser() {
        this.fileContentsMap = new HashMap<>();
    }

    public void analyze() {
        // Fetch all the smelly files
        List<String> smellyFiles = new ArrayList<>(); // connect to sonali
        smellyFiles.add("org.apache.zookeeper.server.quorum.QuorumPeerConfig");
        // Read the contents of the smelly files
        for (String smellyFile : smellyFiles) {
            String fileLoc = Conf.ZK_PRE_CODE_BASE_PATH +
                    StringManipulator.packagePathToSysPath(smellyFile, "/") + Conf.FILE_EXT;
            String fileContents = FileHandler.readLineByLine(fileLoc);
            fileContentsMap.put(smellyFile, fileContents);
        }
    }
}