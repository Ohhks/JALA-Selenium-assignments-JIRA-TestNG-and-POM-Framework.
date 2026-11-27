package com.jala.magnus.assignments.step1_main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

/**
 * Assignment 1 & Framework Step 1 - Script 4 (main method):
 * Remember Me checkbox & Forgot Password LinkText navigation on Magnus.
 */
public class LoginScript4_ForgotPasswordMain {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1920,1080");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get("https://magnus.jalatechnologies.com/");
            driver.findElement(By.cssSelector("label[for='RememberMe']")).click();
            System.out.println("RememberMe selected: " + driver.findElement(By.id("RememberMe")).isSelected());
            driver.findElement(By.linkText("Forgot Password")).click();
            Thread.sleep(1000);
            System.out.println("Script 4 Forgot Password Title: " + driver.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
