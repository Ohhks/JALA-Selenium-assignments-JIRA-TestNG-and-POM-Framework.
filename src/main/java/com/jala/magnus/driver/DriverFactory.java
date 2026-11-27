package com.jala.magnus.driver;

import com.jala.magnus.utils.ConfigReader;
import com.jala.magnus.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Framework Step 16: Multi-browser ThreadLocal WebDriver Factory (Chrome, Firefox, Edge).
 */
public class DriverFactory {
    private static final Logger log = LoggerUtil.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static WebDriver initDriver(String browserName) {
        String browser = (browserName != null && !browserName.trim().isEmpty())
                ? browserName.trim().toLowerCase()
                : ConfigReader.get("browser", "chrome").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");

        log.info("Initializing {} browser (headless={})", browser, headless);
        WebDriver driver;

        switch (browser) {
            case "firefox":
                FirefoxOptions ffOpts = new FirefoxOptions();
                if (headless) ffOpts.addArguments("-headless");
                driver = new FirefoxDriver(ffOpts);
                break;
            case "edge":
                EdgeOptions edgeOpts = new EdgeOptions();
                if (headless) edgeOpts.addArguments("--headless=new");
                edgeOpts.addArguments("--window-size=1920,1080");
                driver = new EdgeDriver(edgeOpts);
                break;
            case "chrome":
            default:
                ChromeOptions chromeOpts = new ChromeOptions();
                if (headless) chromeOpts.addArguments("--headless=new");
                chromeOpts.addArguments("--window-size=1920,1080");
                chromeOpts.addArguments("--disable-gpu");
                chromeOpts.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOpts);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout")));
        driver.manage().window().maximize();
        tlDriver.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            log.info("Closing WebDriver session");
            try {
                driver.quit();
            } finally {
                tlDriver.remove();
            }
        }
    }
}
