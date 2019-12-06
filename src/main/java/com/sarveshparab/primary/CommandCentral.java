package com.sarveshparab.primary;

import com.sarveshparab.analysers.smells.SmellAnalyser;
import com.sarveshparab.clustering.SecurityClustering;
import com.sarveshparab.config.Conf;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandCentral {



    public CommandCentral() {
    }

    public static void main(String args[]){
        SmellAnalyser smellAnalyser = new SmellAnalyser();
        SecurityClustering securityClustering = new SecurityClustering();

        smellAnalyser.runSmellAnalyser(Conf.ZK_PRE_SMELL_FILE);
        Set<String> smellyFiles = smellAnalyser.getSmellyFiles();
//
//        smellyFiles.forEach(System.out::println);


//        smellyFiles = smellyFiles.stream().filter(s->!s.contains("org.apache.zookeeper.cli.")).collect(Collectors.toSet());

        List<String> secFiles = securityClustering.generateCluster(smellyFiles);

        System.out.println(secFiles);
    }


}
