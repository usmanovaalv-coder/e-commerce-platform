package com.ecommerce.productservice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@UtilityClass
public class UtilsTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readAsObject(String filePath, Class<T> valueType) {
        return readJsonAsObject(new File(filePath).toPath(), valueType);
    }

    public static <T> T readAsObject(String filePath, TypeReference<T> valueType) {
        return readJsonAsObject(new File(filePath).toPath(), valueType);
    }

    public static String readAsString(String filePath) {
        return readJsonAsString(new File(filePath).toPath());
    }

    public static <T> T readJsonAsObject(Path path, Class<T> valueType) {
        try {
            return objectMapper.readValue(path.toFile(), valueType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON as object from path: " + path, e);
        }
    }

    public static <T> T readJsonAsObject(Path path, TypeReference<T> valueType) {
        try {
            return objectMapper.readValue(path.toFile(), valueType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON as object from path: " + path, e);
        }
    }

    public static String readJsonAsString(Path path) {
        try {
            return objectMapper.writeValueAsString(objectMapper.readTree(path.toFile()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON as string from path: " + path, e);
        }
    }
}