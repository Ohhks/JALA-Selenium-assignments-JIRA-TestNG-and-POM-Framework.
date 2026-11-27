package com.jala.magnus.base;

import com.jala.magnus.driver.DriverFactory;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.pages.LoginPage;
import com.jala.magnus.utils.ConfigReader;
import com.jala.magnus.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

/**
 * Framework Step 12 & TestNG Lifecycle:
 * BaseTest holds common setup, teardown, browser initialization, and login helper.
 * All Test Suites inherit BaseTest.
 */
public class BaseTest {
    protected WebDriver driver;
    protected final Logger log = LoggerUtil.getLogger(getClass());

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info("=== Starting JALA Academy Magnus TestNG Suite ===");
    }

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("") String browser) {
        driver = DriverFactory.initDriver(browser);
        String url = ConfigReader.get("app.url");
        log.info("Navigating to Magnus URL: {}", url);
        driver.get(url);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info("=== Finished JALA Academy Magnus TestNG Suite ===");
    }

    protected HomePage loginToMagnus() {
        LoginPage loginPage = new LoginPage(driver);
        HomePage home = loginPage.loginAs(
                ConfigReader.get("app.username"),
                ConfigReader.get("app.password")
        );
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(ConfigReader.getInt("explicit.wait")))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/Home/Index"));
        return home;
    }
}
