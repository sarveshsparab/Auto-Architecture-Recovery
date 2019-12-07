package com.sarveshparab.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarveshparab.analysers.git.CommitContent;
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

    public static void saveToJson(String filePath, Map<String, CommitContent> jsonObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(new ArrayList<>(jsonObject.values()));
            Files.write(Paths.get(filePath), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, CommitContent> loadFromJson(String filePath) {
        String jsonObjectString = FileHandler.readLineByLine(filePath);
        List<CommitContent> ccl = new ArrayList<>();
        try {
           ccl = new ObjectMapper().readValue(jsonObjectString, new TypeReference<List<CommitContent>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ccl.stream().collect(Collectors.toMap(CommitContent::getId, v->v));
    }
}
