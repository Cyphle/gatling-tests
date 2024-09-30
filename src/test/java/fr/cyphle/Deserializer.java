package fr.cyphle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Deserializer {
    public static Deserializer deserializer;
    private ObjectMapper jacksonMapper = new ObjectMapper();

    private Deserializer() {
        jacksonMapper
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Deserializer get() {
        if (deserializer == null) {
            deserializer = new Deserializer();
        }
        return deserializer;
    }

    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return jacksonMapper.readValue(content, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
