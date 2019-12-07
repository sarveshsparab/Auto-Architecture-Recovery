package com.sarveshparab.util;

import com.sarveshparab.config.Conf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public static String[] splitFileLineByLine(String fileContent) {
        String returnArray[] = fileContent.split("\n");
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

    public static Set<String> readSetFromFile(String filePath) {

        Set<String> lines = new HashSet<>();
        try {
            lines = new HashSet<>(Files.readAllLines(new File(filePath).toPath(), Charset.defaultCharset()));
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

    public static List<String> readListFromFile(String filePath) {

        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(new File(filePath).toPath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static boolean doesFileExists(String filePath) {

        return Files.exists(Paths.get(filePath));
    }

    public static <T> void writeMapToFile(String filePath, Map<String, T> mapData){
        try {
            Files.write(Paths.get(filePath), () -> mapData.entrySet().stream()
                    .<CharSequence>map(e -> e.getKey() + Conf.MAP_SEPARATOR + e.getValue().toString())
                    .iterator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> Map<String, T> readMapFromFile(String filePath, Function<String, T> mapper){
        Path path = FileSystems.getDefault().getPath(filePath);
        Map<String, T> stringTMap = new HashMap<>();
        try {
            stringTMap = Files.lines(path)
                    .collect(Collectors.toMap(k -> k.split(Conf.MAP_SEPARATOR)[0], v -> mapper.apply(v.split(Conf.MAP_SEPARATOR)[1])));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringTMap;
    }
}
