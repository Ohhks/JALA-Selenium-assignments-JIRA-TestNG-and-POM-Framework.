package com.jala.magnus.pages;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.utils.ConfigReader;
import com.jala.magnus.utils.LocatorReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * PageObjects class for Magnus Dashboard and Left Navigation Menu.
 */
public class HomePage {
    private final WebDriver driver;
    private final PageActions actions;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new PageActions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("/Home/Index"));
            return actions.isDisplayed(LocatorReader.getBy("nav.logout.linkText"));
        } catch (Exception e) {
            return false;
        }
    }

    public String getWelcomeHeading() {
        return actions.getText(By.cssSelector("div.content-wrapper h1, center h1, .content-wrapper"));
    }

    private void navigateViaSidebar(By parentLoc, By childLoc, String expectedUrlPart) {
        try {
            actions.jsClick(parentLoc);
        } catch (Exception ignored) {
        }
        actions.jsClick(childLoc);
        wait.until(ExpectedConditions.urlContains(expectedUrlPart));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section.content")));
    }

    public EmployeeCreatePage goToEmployeeCreate() {
        navigateViaSidebar(LocatorReader.getBy("nav.employee.linkText"),
                LocatorReader.getBy("nav.employeeCreate.xpath"), "/Employee/Create");
        return new EmployeeCreatePage(driver);
    }

    public EmployeeSearchPage goToEmployeeSearch() {
        navigateViaSidebar(LocatorReader.getBy("nav.employee.linkText"),
                LocatorReader.getBy("nav.employeeSearch.xpath"), "/Employee/Search");
        return new EmployeeSearchPage(driver);
    }

    public MoreFeaturesPage goToMoreSubPage(String navKey) {
        String rawXpath = LocatorReader.getRaw(navKey);
        String expectedPart = "/Home/";
        int idx = rawXpath.indexOf("/Home/");
        if (idx >= 0) {
            int end = rawXpath.indexOf("'", idx);
            if (end > idx) expectedPart = rawXpath.substring(idx, end);
        }
        navigateViaSidebar(LocatorReader.getBy("nav.more.linkText"),
                LocatorReader.getBy(navKey), expectedPart);
        return new MoreFeaturesPage(driver);
    }

    public LoginPage logout() {
        actions.click(LocatorReader.getBy("nav.logout.linkText"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(LocatorReader.getBy("login.signin.id")));
        return new LoginPage(driver);
    }
}
