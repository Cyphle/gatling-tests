package fr.cyphle.utils;

import java.util.List;
import java.util.stream.Collectors;

public class Helpers {
    public static String collectionToJsonListParam(List<String> collection) {
        return "[" + collection
            .stream()
            .map(s -> "\"" + s + "\"")
            .collect(Collectors.joining(", ")) + "]";
    }
}
