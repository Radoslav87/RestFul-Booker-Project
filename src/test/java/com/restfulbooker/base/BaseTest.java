package com.restfulbooker.base;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class BaseTest {

    private static final Properties CONFIG_PROPS = new Properties();

    @BeforeSuite(alwaysRun = true)
    public void initSuiteBase() {
        loadFromClasspath("dataPropertyFiles/config.properties", CONFIG_PROPS);
    }

    protected String cfg(String key) {
        String sys = System.getProperty(key);
        return (sys != null && !sys.isBlank()) ? sys : CONFIG_PROPS.getProperty(key);
    }

    protected String cfg(String key, String def) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) return sys;
        String val = CONFIG_PROPS.getProperty(key);
        return (val != null && !val.isBlank()) ? val : def;
    }

    @AfterSuite(alwaysRun = true)
    public void writeAllureEnvironment() {

        String dir = System.getProperty("allure.results.directory", "target/allure-results");
        try {
            Path outDir = Path.of(dir);
            Files.createDirectories(outDir);
            Path env = outDir.resolve("environment.properties");


            String envVal = cfg("webUrl", cfg("baseUri", "n/a"));

            try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(env))) {
                w.println("environment=" + envVal);
            }
        } catch (Exception ignored) {}
    }

    private static void loadFromClasspath(String resource, Properties into) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            if (is == null) throw new IllegalStateException(resource + " not found on test classpath");
            into.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + resource, e);
        }
    }
}