package com.jala.magnus.assignments.step1_main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

/**
 * Assignment 1 & Framework Step 1 - Script 3 (main method):
 * Empty Credentials Login validation using XPath locators.
 */
public class LoginScript3_EmptyFieldsMain {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1920,1080");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get("https://magnus.jalatechnologies.com/");
            driver.findElement(By.xpath("//button[@id='btnLogin']")).click();
            Thread.sleep(1000);
            System.out.println("Script 3 URL after blank Sign In: " + driver.getCurrentUrl());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
