package com.jala.magnus.assignments.step1_main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

/**
 * Assignment 1 & Framework Step 1 - Script 2 (main method):
 * Invalid Login using Name locators on Magnus.
 */
public class LoginScript2_InvalidLoginMain {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1920,1080");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get("https://magnus.jalatechnologies.com/");
            driver.findElement(By.name("UserName")).sendKeys("wrong@jalaacademy.com");
            driver.findElement(By.name("Password")).sendKeys("wrongpass");
            driver.findElement(By.cssSelector("button#btnLogin")).click();
            Thread.sleep(1500);
            boolean stayedOnLogin = driver.findElement(By.id("btnLogin")).isDisplayed();
            System.out.println("Script 2 Stayed on Login page for invalid creds: " + stayedOnLogin);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
