package com.sarveshparab.primary;

import com.sarveshparab.analysers.semantic.SemanticAnalyser;

public class BaseLogic {

    public static void main(String args[]){
        SemanticAnalyser semanticAnalyser = new SemanticAnalyser();

        semanticAnalyser.analyze();
    }
}
