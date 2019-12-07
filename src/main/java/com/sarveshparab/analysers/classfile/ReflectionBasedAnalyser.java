package com.sarveshparab.analysers.classfile;

import com.sarveshparab.analysers.semantic.SemanticAnalyser;
import com.sarveshparab.config.Conf;
import com.sarveshparab.config.Params;
import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.StringManipulator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionBasedAnalyser {

    private ClassLoader classLoader;
    private SemanticAnalyser semanticAnalyser;

    public ReflectionBasedAnalyser(SemanticAnalyser semanticAnalyser, boolean usePre){
        File file;
        if (usePre) {
            file = new File(Conf.ZK_PRE_BUILD_PATH + "classes/");
        } else {
            file = new File(Conf.ZK_POST_BUILD_PATH + "classes/");
        }
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL[] urls = new URL[]{url};

        classLoader = new URLClassLoader(urls);

        this.semanticAnalyser = semanticAnalyser;
    }

    private Set<String> getMethodFeatures(String classname, boolean removeCamelCase) throws ClassNotFoundException {
        Set<String> mNames = new HashSet<>();

        Class cls = classLoader.loadClass(classname);

        Method[] methods = cls.getMethods();

        for (Method method : methods){
            mNames.add(method.getName());
            mNames.addAll(Arrays.stream(method.getExceptionTypes()).map(Class::getSimpleName).collect(Collectors.toList()));
            mNames.addAll(Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.toList()));
        }

        Constructor[] cons = cls.getConstructors();
        for (Constructor con : cons){
            mNames.add(con.getName());
            mNames.addAll(Arrays.stream(con.getExceptionTypes()).map(Class::getSimpleName).collect(Collectors.toList()));
            mNames.addAll(Arrays.stream(con.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.toList()));
        }

        if (removeCamelCase){
            List<List<String>> postRemoval = mNames.stream().map(StringManipulator::removeCamelCase).collect(Collectors.toList());
            mNames.addAll(postRemoval.stream().flatMap(List::stream).collect(Collectors.toList()));

            mNames = new HashSet<>(semanticAnalyser.removePLWords(semanticAnalyser.removeStopWords(mNames)));
        }

        return mNames;
    }

    private Set<String> getVariableFeatures(String classname, boolean removeCamelCase) throws ClassNotFoundException {
        Set<String> vNames = new HashSet<>();

        Class cls = classLoader.loadClass(classname);
        Field[] fields = cls.getFields();

        for (Field field : fields){
            vNames.add(field.getName());
            vNames.add(field.getType().getSimpleName());
        }

        fields = cls.getDeclaredFields();

        for (Field field : fields){
            vNames.add(field.getName());
            vNames.add(field.getType().getSimpleName());
        }

        if (removeCamelCase){
            List<List<String>> postRemoval = vNames.stream().map(StringManipulator::removeCamelCase).collect(Collectors.toList());
            vNames.addAll(postRemoval.stream().flatMap(List::stream).collect(Collectors.toList()));

            vNames = new HashSet<>(semanticAnalyser.removePLWords(semanticAnalyser.removeStopWords(vNames)));
        }

        return vNames;
    }

    private Set<String> getInheritanceFeatures(String classname, boolean removeCamelCase) throws ClassNotFoundException {
        Set<String> iNames = new HashSet<>();

        Class cls = classLoader.loadClass(classname);
        Class[] inheritences = cls.getInterfaces();

        for (Class inherit : inheritences){
            iNames.add(inherit.getSimpleName());
        }

        inheritences = cls.getDeclaredClasses();

        for (Class inherit : inheritences){
            iNames.add(inherit.getSimpleName());
        }

        if (cls.getSuperclass() != null)
            iNames.add(cls.getSuperclass().getSimpleName());

        if (removeCamelCase){
            List<List<String>> postRemoval = iNames.stream().map(StringManipulator::removeCamelCase).collect(Collectors.toList());
            iNames.addAll(postRemoval.stream().flatMap(List::stream).collect(Collectors.toList()));

            iNames = new HashSet<>(semanticAnalyser.removePLWords(semanticAnalyser.removeStopWords(iNames)));
        }

        return iNames;
    }

    public Set<String> extractAllFeatures(String className, boolean pre){
        Set<String> features = new HashSet<>();

        String preReflectDataDumpLoc = Conf.REFLECT_ANALYSIS_PRE_COMMIT_DUMP_DIR + StringManipulator.changeDotToUnderscore(className) + ".data";
        String postReflectDataDumpLoc = Conf.REFLECT_ANALYSIS_POST_COMMIT_DUMP_DIR + StringManipulator.changeDotToUnderscore(className) + ".data";

        if(pre && Params.LOAD_PRE_COMMIT_REFLECTION_ANALYSIS && FileHandler.doesFileExists(preReflectDataDumpLoc)){
            System.out.println("Loaded pre-commit reflection analysis files");
            return FileHandler.readSetFromFile(preReflectDataDumpLoc);
        }else if(!pre && Params.LOAD_POST_COMMIT_REFLECTION_ANALYSIS && FileHandler.doesFileExists(postReflectDataDumpLoc)){
            System.out.println("Loaded post-commit reflection analysis files");
            return FileHandler.readSetFromFile(postReflectDataDumpLoc);
        }

        try {
            features.addAll(getMethodFeatures(className, true));
            features.addAll(getVariableFeatures(className, true));
            features.addAll(getInheritanceFeatures(className, true));

            if (pre)
                FileHandler.writeSetToFile(preReflectDataDumpLoc, features);
            else
                FileHandler.writeSetToFile(postReflectDataDumpLoc, features);
        } catch (ClassNotFoundException e){
            System.err.println("Failed for : " + className + "   |   -> " + e.getMessage());
        }

        return features;
    }
}
