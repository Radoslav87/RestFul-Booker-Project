package com.restfulbooker.helpers;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {


    public static Properties loadProps(String classpathFile) {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(classpathFile)) {

            if (is == null) {
                throw new IllegalStateException(classpathFile + " not found on test classpath");
            }
            Properties p = new Properties();
            p.load(is);
            return p;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + classpathFile, e);
        }
    }
}