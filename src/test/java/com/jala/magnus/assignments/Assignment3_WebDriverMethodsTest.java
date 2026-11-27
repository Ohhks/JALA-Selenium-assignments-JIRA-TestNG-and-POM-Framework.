package com.jala.magnus.assignments;

import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.EmployeeCreatePage;
import com.jala.magnus.pages.HomePage;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

/**
 * Assignment 3: Web Driver Methods
 * Covers all 27 listed WebDriver and WebElement methods on Magnus Application.
 */
public class Assignment3_WebDriverMethodsTest extends BaseTest {

    @Test(priority = 1, groups = {"assignments", "webdriver"}, description = "Verify all 27 WebDriver and WebElement API methods on Magnus")
    public void testAllWebDriverMethods() throws InterruptedException {
        // 1. get()
        driver.get("https://magnus.jalatechnologies.com/");
        // 2. manage()
        driver.manage().window().maximize();
        // 3. getCurrentUrl()
        Assert.assertTrue(driver.getCurrentUrl().contains("magnus.jalatechnologies.com"));
        // 4. getTitle()
        Assert.assertEquals(driver.getTitle(), "Login");
        // 5. getPageSource()
        Assert.assertTrue(driver.getPageSource().contains("UserName"));

        // 6. findElement() & 7. sendKeys() & 8. getAttribute() & 9. getTagName()
        WebElement userField = driver.findElement(By.id("UserName"));
        Assert.assertEquals(userField.getTagName(), "input");
        Assert.assertEquals(userField.getAttribute("placeholder"), "Email or Mobile No");
        userField.sendKeys("training@jalaacademy.com");

        // 10. getSize() & 11. getLocation() & 12. isDisplayed() & 13. isEnabled()
        Dimension size = userField.getSize();
        Point loc = userField.getLocation();
        Assert.assertTrue(size.getWidth() > 0 && size.getHeight() > 0);
        Assert.assertTrue(loc.getX() >= 0 && loc.getY() >= 0);
        Assert.assertTrue(userField.isDisplayed());
        Assert.assertTrue(userField.isEnabled());

        // 14. isSelected()
        driver.findElement(By.cssSelector("label[for='RememberMe']")).click();
        Assert.assertTrue(driver.findElement(By.id("RememberMe")).isSelected());

        // 15. click()
        driver.findElement(By.id("Password")).sendKeys("jobprogram");
        driver.findElement(By.id("btnLogin")).click();
        // 16. Thread.sleep() + explicit URL wait
        Thread.sleep(2000);
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/Home/Index"));

        // 17. findElements()
        List<WebElement> navLinks = driver.findElements(By.tagName("a"));
        Assert.assertTrue(navLinks.size() > 5);

        // 18. navigate().to(), back(), forward(), refresh()
        org.openqa.selenium.support.ui.WebDriverWait navWait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        driver.navigate().to("https://magnus.jalatechnologies.com/Employee/Create");
        navWait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/Employee/Create"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/Employee/Create"));
        driver.navigate().back();
        navWait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/Home/Index"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/Home/Index"));
        driver.navigate().forward();
        navWait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/Employee/Create"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/Employee/Create"));
        driver.navigate().refresh();
        Assert.assertEquals(driver.getTitle(), "Create Employee");

        // 19. getText()
        String heading = driver.findElement(By.cssSelector("h3.box-title")).getText();
        Assert.assertEquals(heading, "Employee Details");

        // 20-23. getWindowHandle(), getWindowHandles(), switchTo(), close()
        driver.navigate().to("https://magnus.jalatechnologies.com/Home/Popup");
        navWait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/Home/Popup"));
        String parentHandle = driver.getWindowHandle();
        driver.findElement(By.id("btn-one")).click();
        navWait.until(d -> d.getWindowHandles().size() >= 2);
        Set<String> allHandles = driver.getWindowHandles();
        Assert.assertTrue(allHandles.size() >= 2);
        for (String h : allHandles) {
            if (!h.equals(parentHandle)) {
                driver.switchTo().window(h);
                driver.close();
            }
        }
        driver.switchTo().window(parentHandle);
        Assert.assertEquals(driver.getTitle(), "Popup");
        // driver.quit() is handled by BaseTest @AfterMethod
    }
}
