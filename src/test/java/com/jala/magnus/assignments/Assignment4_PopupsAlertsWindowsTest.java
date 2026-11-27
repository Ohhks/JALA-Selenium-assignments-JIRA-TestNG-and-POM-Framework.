package com.jala.magnus.assignments;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.pages.MoreFeaturesPage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Assignment 4: Popups/Alerts and Windows (Steps 1 to 10)
 * Executed on Magnus /Home/Popup and /Home/Iframe.
 */
public class Assignment4_PopupsAlertsWindowsTest extends BaseTest {

    @Test(priority = 1, groups = {"assignments", "alerts"}, description = "Steps 1-5 & 10: Alert getText, Confirm OK/Cancel, Prompt Alert input, switchTo().alert()")
    public void testAlertsAndPrompts() {
        HomePage home = loginToMagnus();
        home.goToMoreSubPage("nav.popup.xpath");
        PageActions actions = new PageActions(driver);

        // 1, 4, 10: Simple Alert -> switchTo().alert(), getText(), accept()
        driver.findElement(By.id("alertBox")).click();
        Alert simpleAlert = actions.waitForAlert();
        String alertMsg = simpleAlert.getText();
        Assert.assertEquals(alertMsg, "This is an alert Box!");
        simpleAlert.accept();

        // 3, 5: Confirmation Alert -> dismiss()
        driver.findElement(By.id("confirmBox")).click();
        Alert confirmAlert = actions.waitForAlert();
        Assert.assertEquals(confirmAlert.getText(), "Confirm Message Box");
        confirmAlert.dismiss();

        // 2: Prompt Alert with text box
        driver.findElement(By.id("promptBtn")).click();
        Alert promptAlert = actions.waitForAlert();
        Assert.assertTrue(promptAlert.getText().contains("Enter Your Name"));
        promptAlert.sendKeys("JALA Academy QA");
        promptAlert.accept();
        String echoed = driver.findElement(By.id("promptBox")).getText().trim();
        Assert.assertEquals(echoed, "JALA Academy QA");
    }

    @Test(priority = 2, groups = {"assignments", "windows"}, description = "Steps 6-9: getWindowHandle, getWindowHandles, switchTo().window(), switchTo().frame()")
    public void testWindowsAndFrames() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.popup.xpath");

        // 6. Handle single window
        String mainWindow = driver.getWindowHandle();
        Assert.assertFalse(mainWindow.isEmpty());

        // 7 & 8. Handle multiple windows and switchTo().window()
        driver.findElement(By.id("btn-one")).click();
        PageActions actions = new PageActions(driver);
        String childWindow = actions.switchToChildWindow(mainWindow);
        Set<String> handles = driver.getWindowHandles();
        Assert.assertTrue(handles.size() >= 2);
        Assert.assertNotEquals(childWindow, mainWindow);
        driver.close();
        driver.switchTo().window(mainWindow);

        // 9. Switch to frame using driver.switchTo().frame()
        home.goToMoreSubPage("nav.iframe.xpath");
        more.switchToIframe("iframe.two.id");
        Assert.assertTrue(driver.getPageSource().contains("Welcome to JALA Academy"));
        more.switchBackFromIframe();
    }
}
