package com.jala.magnus.assignments;

import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.pages.LoginPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Assignment 1 & Framework Step 2:
 * All 5 Login Automation scripts combined in a single class using TestNG annotations.
 * Demonstrates ID, Name, XPath, LinkText, PartialLinkText, CSS Selector, and TagName locators.
 */
public class Assignment1_LocatorsTestNG extends BaseTest {

    @Test(priority = 1, groups = {"assignments", "locators"}, description = "Script 1: Valid Login via ID locators")
    public void testScript1_ValidLogin() {
        driver.findElement(By.id("UserName")).sendKeys("training@jalaacademy.com");
        driver.findElement(By.id("Password")).sendKeys("jobprogram");
        driver.findElement(By.id("btnLogin")).click();
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isLoaded(), "Valid login should load Magnus dashboard");
    }

    @Test(priority = 2, groups = {"assignments", "locators"}, description = "Script 2: Invalid Login via Name & CSS locators")
    public void testScript2_InvalidLogin() {
        driver.findElement(By.name("UserName")).sendKeys("wrong@jalaacademy.com");
        driver.findElement(By.name("Password")).sendKeys("wrongpass");
        driver.findElement(By.cssSelector("button#btnLogin")).click();
        Assert.assertTrue(new LoginPage(driver).isOnLoginPage());
    }

    @Test(priority = 3, groups = {"assignments", "locators"}, description = "Script 3: Empty Fields Login via XPath locator")
    public void testScript3_EmptyLogin() {
        driver.findElement(By.xpath("//button[@id='btnLogin']")).click();
        Assert.assertTrue(new LoginPage(driver).isOnLoginPage());
    }

    @Test(priority = 4, groups = {"assignments", "locators"}, description = "Script 4: RememberMe & Forgot Password via LinkText")
    public void testScript4_ForgotPassword() {
        driver.findElement(By.cssSelector("label[for='RememberMe']")).click();
        Assert.assertTrue(driver.findElement(By.id("RememberMe")).isSelected());
        driver.findElement(By.linkText("Forgot Password")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("ForgotPassword"));
    }

    @Test(priority = 5, groups = {"assignments", "locators"}, description = "Script 5: Admin Login via PartialLinkText & TagName count")
    public void testScript5_AdminLoginAndTagName() {
        int inputCount = driver.findElements(By.tagName("input")).size();
        Assert.assertTrue(inputCount >= 4);
        driver.findElement(By.partialLinkText("Admin")).click();
        Assert.assertEquals(driver.getTitle(), "Admin Login");
    }
}
