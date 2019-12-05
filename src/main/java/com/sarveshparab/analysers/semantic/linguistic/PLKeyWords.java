package com.sarveshparab.analysers.semantic.linguistic;

import java.util.*;

public class PLKeyWords {

    private Set<String> javaKeyWords;
    private Set<String> cKeyWords;

    public PLKeyWords() {
        String[] javaWords = {"wait", "notify", "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "string", "null"};
        javaKeyWords = new HashSet<String>(Arrays.asList(javaWords));

        String[] cWords = {"auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "auto", "const", "double", "float", "int", "short", "struct", "unsigned", "break", "continue", "else", "for", "long", "signed", "switch", "void", "case", "default", "enum", "goto", "register", "sizeof", "typedef", "volatile", "char", "do", "extern", "if", "return", "static", "union", "while", "asm", "dynamic_cast", "namespace", "reinterpret_cast", "try", "bool", "explicit", "new", "static_cast", "typeid", "catch", "false", "operator", "template", "typename", "class", "friend", "private", "this", "using", "const_cast", "inline", "public", "throw", "virtual", "delete", "mutable", "protected", "true", "wchar_t"};
        cKeyWords = new HashSet<String>(Arrays.asList(cWords));
    }

    public List<String> removeJavaWords(List<String> words){
        List<String> afterRemoval = new LinkedList<String>();
        Iterator<String> itr = words.iterator();
        while(itr.hasNext()) {
            String element = itr.next();
            if(javaKeyWords.contains(element)){

            }else{
                afterRemoval.add(element);
            }
        }
        return afterRemoval;
    }

    public List<String> removeCWords(List<String> words){
        List<String> afterRemoval = new LinkedList<String>();
        Iterator<String> itr = words.iterator();
        while(itr.hasNext()) {
            String element = itr.next();
            if(cKeyWords.contains(element)){

            }else{
                afterRemoval.add(element);
            }
        }
        return afterRemoval;
    }
}
