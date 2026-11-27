package com.jala.magnus.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Framework Step 20: Configures Extent Reports HTML output.
 */
public class ExtentManager {
    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportsDir = ConfigReader.get("reports.dir", "reports");
            Path reportPath = Paths.get(reportsDir, "ExtentReport.html");
            try {
                Files.createDirectories(reportPath.getParent());
            } catch (Exception ignored) {
            }
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath.toString());
            spark.config().setDocumentTitle("JALA Academy Magnus - Automation Report");
            spark.config().setReportName("Magnus Selenium TestNG & POM Suite");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "JALA Academy Magnus");
            extent.setSystemInfo("Base URL", ConfigReader.get("app.url"));
            extent.setSystemInfo("Browser", ConfigReader.get("browser", "chrome"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }
}
