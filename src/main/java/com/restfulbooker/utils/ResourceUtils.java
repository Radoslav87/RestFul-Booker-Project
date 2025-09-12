package com.restfulbooker.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ResourceUtils {
    private ResourceUtils() {}
    public static String readClasspathResource(String resourcePath) {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (is == null) throw new IllegalArgumentException(resourcePath + " not found on classpath");
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) { throw new RuntimeException("Failed to read " + resourcePath, e); }
    }
}