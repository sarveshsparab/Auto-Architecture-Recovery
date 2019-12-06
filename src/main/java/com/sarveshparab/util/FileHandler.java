package com.sarveshparab.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class FileHandler {

    public static String readLineByLine(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    public static String[] splitFileLineByLine(String fileContent){
        String returnArray[]=fileContent.split("\n");
        return returnArray;
    }

    public static void writeSetToFile(String filePath, Set<String> stringList) {

        Path out = Paths.get(filePath);
        try {
            Files.write(out, stringList, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Set<String> readSetFromFile(String filePath){

        Set<String> lines = new HashSet<>();
        try {
         lines = new HashSet<>(Files.readAllLines(new File(filePath).toPath(), Charset.defaultCharset() ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void writeListToFile(String filePath, List<String> stringList) {

        Path out = Paths.get(filePath);
        try {
            Files.write(out, stringList, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<String> readListFromFile(String filePath){

        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(new File(filePath).toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static boolean doesFileExists(String filePath){

        return Files.exists(Paths.get(filePath));
    }
}
