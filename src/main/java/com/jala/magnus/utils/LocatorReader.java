package com.jala.magnus.utils;

import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Framework Step 5: Separates web element locators from test scripts via locators.properties.
 */
public class LocatorReader {
    private static final Properties locators = new Properties();

    static {
        String path = ConfigReader.get("locators.file", "src/test/resources/locators.properties");
        try (FileInputStream fis = new FileInputStream(path)) {
            locators.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load locators.properties from: " + path, e);
        }
    }

    /**
     * Resolves key naming convention `<page>.<element>.<strategy>` into a Selenium By locator.
     */
    public static By getBy(String key) {
        String value = locators.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Locator key not found in locators.properties: " + key);
        }
        value = value.trim();
        if (key.endsWith(".id")) return By.id(value);
        if (key.endsWith(".name")) return By.name(value);
        if (key.endsWith(".css")) return By.cssSelector(value);
        if (key.endsWith(".xpath")) return By.xpath(value);
        if (key.endsWith(".linkText")) return By.linkText(value);
        if (key.endsWith(".partialLinkText")) return By.partialLinkText(value);
        if (key.endsWith(".className")) return By.className(value);
        if (key.endsWith(".tagName")) return By.tagName(value);
        throw new IllegalArgumentException("Unsupported locator strategy suffix for key: " + key);
    }

    public static String getRaw(String key) {
        return locators.getProperty(key, "").trim();
    }
}
