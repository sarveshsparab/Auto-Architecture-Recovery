package com.sarveshparab.primary;

import com.sarveshparab.analysers.smells.SmellAnalyser;
import com.sarveshparab.clustering.SecurityClustering;
import com.sarveshparab.config.Conf;

import java.util.List;

public class CommandCentral {

    public CommandCentral() {
    }

    public static void main(String args[]){
        SmellAnalyser smellAnalyser = new SmellAnalyser();
        SecurityClustering securityClustering = new SecurityClustering();

        smellAnalyser.runSmellAnalyser(Conf.ZK_PRE_SMELL_FILE);
        List<String> secFiles = securityClustering.generateCluster(smellAnalyser.getSmellyFiles());

        System.out.println(secFiles);
        System.out.println(secFiles.size());
    }

}
