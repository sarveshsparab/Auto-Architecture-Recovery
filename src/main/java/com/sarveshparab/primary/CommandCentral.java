package com.sarveshparab.primary;

import com.sarveshparab.analysers.imports.FileImportsPopulator;
import com.sarveshparab.analysers.smells.SmellAnalyser;
import com.sarveshparab.clustering.SecurityClustering;
import com.sarveshparab.config.Conf;
import com.sarveshparab.util.FileHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandCentral {

    public CommandCentral() {
    }

    public static void main(String args[]) {
        SmellAnalyser smellAnalyser = new SmellAnalyser();
        SecurityClustering securityClustering = new SecurityClustering();
        FileImportsPopulator fileImportsPopulator = new FileImportsPopulator();

        smellAnalyser.runSmellAnalyser(Conf.ZK_PRE_SMELL_FILE);
        List<String> secFiles = securityClustering.generateCluster(smellAnalyser.getSmellyFiles(), true);

        Set<String> importsDiff = new HashSet<>();

        for (String secFile : secFiles) {

            Set<String> importsFromPost = fileImportsPopulator.getFileImports(secFile, false);
            Set<String> importsFromPre = fileImportsPopulator.getFileImports(secFile, true);

            importsFromPost.removeAll(importsFromPre);
            importsDiff.addAll(importsFromPost);
        }

        importsDiff.addAll(secFiles);

        FileHandler.writeSetToFile(Conf.SECURITY_CLUSTER_DUMP_WITH_IMPORTS_DIFF, importsDiff);
    }

}
