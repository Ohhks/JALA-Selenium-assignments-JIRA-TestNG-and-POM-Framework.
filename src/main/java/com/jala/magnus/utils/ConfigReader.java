package com.jala.magnus.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Framework Step 14: Reads constant configuration values from config.properties.
 */
public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties from: " + CONFIG_PATH, e);
        }
    }

    public static String get(String key) {
        String sysVal = System.getProperty(key);
        if (sysVal != null && !sysVal.trim().isEmpty()) {
            return sysVal.trim();
        }
        String val = properties.getProperty(key);
        if (val == null) {
            throw new RuntimeException("Key '" + key + "' not found in config.properties");
        }
        return val.trim();
    }

    public static String get(String key, String defaultValue) {
        String sysVal = System.getProperty(key);
        if (sysVal != null && !sysVal.trim().isEmpty()) {
            return sysVal.trim();
        }
        return properties.getProperty(key, defaultValue).trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
