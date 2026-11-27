package com.jala.magnus.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Assignment 5 Step 1 & Framework Step 20: Captures WebDriver screenshots.
 */
public class ScreenshotUtil {

    public static String capture(WebDriver driver, String name) {
        if (driver == null) return "";
        String dir = ConfigReader.get("screenshots.dir", "screenshots");
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String safeName = name.replaceAll("[^a-zA-Z0-9_-]", "_");
        Path destDir = Paths.get(dir);
        try {
            Files.createDirectories(destDir);
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dest = destDir.resolve(safeName + "_" + timestamp + ".png");
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toAbsolutePath().toString();
        } catch (IOException e) {
            return "";
        }
    }
}
