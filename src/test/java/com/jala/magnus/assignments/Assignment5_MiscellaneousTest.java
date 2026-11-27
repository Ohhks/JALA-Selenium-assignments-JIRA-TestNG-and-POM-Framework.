package com.jala.magnus.assignments;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.EmployeeCreatePage;
import com.jala.magnus.pages.EmployeeSearchPage;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.pages.MoreFeaturesPage;
import com.jala.magnus.utils.ScreenshotUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * Assignment 5: Selenium Miscellaneous Scenarios (Items 1 to 9)
 * Executed strictly on Magnus Application.
 */
public class Assignment5_MiscellaneousTest extends BaseTest {

    @Test(priority = 1, groups = {"assignments", "misc"}, description = "1. Capture screenshots with WebDriver")
    public void testCaptureScreenshot() {
        loginToMagnus();
        String path = ScreenshotUtil.capture(driver, "Assignment5_HomePage");
        Assert.assertFalse(path.isEmpty(), "Screenshot path should not be empty");
        Assert.assertTrue(new File(path).exists(), "Screenshot file must exist on disk");
    }

    @Test(priority = 2, groups = {"assignments", "misc"}, description = "2. Click on an element inside an iFrame")
    public void testClickElementInIframe() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.iframe.xpath");
        more.switchToIframe("iframe.two.id");
        WebElement sidebarToggle = driver.findElement(By.cssSelector("a.sidebar-toggle"));
        sidebarToggle.click();
        more.switchBackFromIframe();
        Assert.assertEquals(driver.getTitle(), "Iframe");
    }

    @Test(priority = 3, groups = {"assignments", "misc"}, description = "3. Find broken links on Magnus Links page")
    public void testFindBrokenLinks() {
        HomePage home = loginToMagnus();
        home.goToMoreSubPage("nav.links.xpath");
        PageActions actions = new PageActions(driver);

        List<WebElement> links = driver.findElements(By.cssSelector(".content-wrapper a"));
        int brokenOrErrorCount = 0;
        for (WebElement link : links) {
            String href = link.getAttribute("href");
            if (href != null && href.startsWith("http") && !href.contains("#")) {
                int code = actions.checkHttpStatus(href);
                log.info("Checked Link: {} -> HTTP {}", href, code);
                if (code >= 400 || code == -1) {
                    brokenOrErrorCount++;
                }
            }
        }
        Assert.assertTrue(brokenOrErrorCount >= 1, "Expected Magnus Links page to contain >= 1 broken/error link");
    }

    @Test(priority = 4, groups = {"assignments", "misc"}, description = "4. Implicit and Explicit Wait commands")
    public void testImplicitAndExplicitWaits() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.findElement(By.id("UserName")).sendKeys("training@jalaacademy.com");
        driver.findElement(By.id("Password")).sendKeys("jobprogram");
        explicitWait.until(ExpectedConditions.elementToBeClickable(By.id("btnLogin"))).click();
        explicitWait.until(ExpectedConditions.urlContains("/Home/Index"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/Home/Index"));
    }

    @Test(priority = 5, groups = {"assignments", "misc"}, description = "5. Actions Class: Keyboard press, Enter, ClickAndHold/DragAndDrop, Hover, DoubleClick")
    public void testActionsClassOperations() {
        HomePage home = loginToMagnus();
        Actions act = new Actions(driver);

        // Hover (moveToElement) on Tooltip
        home.goToMoreSubPage("nav.tooltip.xpath");
        WebElement tipBtn = driver.findElement(By.id("btnTooltip"));
        act.moveToElement(tipBtn).perform();
        Assert.assertTrue(tipBtn.getAttribute("data-original-title").contains("Click me"));

        // ClickAndHold + DragAndDrop offset on Slider
        home.goToMoreSubPage("nav.slider.xpath");
        WebElement handle = driver.findElement(By.cssSelector(".slider-handle"));
        String beforeVal = driver.findElement(By.id("ex6SliderVal")).getText().trim();
        act.clickAndHold(handle).moveByOffset(250, 0).release().perform();
        String afterVal = driver.findElement(By.id("ex6SliderVal")).getText().trim();
        log.info("Slider moved from {} to {}", beforeVal, afterVal);
        Assert.assertNotEquals(afterVal, beforeVal, "Slider value should change after drag");

        // Keyboard Shift + Enter + DoubleClick on Employee Search
        EmployeeSearchPage search = home.goToEmployeeSearch();
        WebElement nameBox = driver.findElement(By.id("Name"));
        act.click(nameBox)
                .keyDown(Keys.SHIFT).sendKeys("arun").keyUp(Keys.SHIFT)
                .doubleClick(nameBox)
                .sendKeys(Keys.ENTER)
                .perform();
        Assert.assertEquals(nameBox.getAttribute("value"), "ARUN");
    }

    @Test(priority = 6, groups = {"assignments", "misc"}, description = "6. Web Table Operations: row count, specific cell data, dynamic table handling")
    public void testWebTableOperations() {
        HomePage home = loginToMagnus();
        EmployeeSearchPage search = home.goToEmployeeSearch();

        int rowCount = search.getRowCount();
        int colCount = search.getHeaderCount();
        log.info("WebTable rows={}, cols={}", rowCount, colCount);
        Assert.assertTrue(rowCount >= 1, "Table should have at least 1 row");
        Assert.assertEquals(colCount, 9, "Expected 9 columns in Employee table");

        String firstNameCell = search.getCellData(1, 1);
        String emailCell = search.getCellData(1, 4);
        log.info("Row 1 Col 1 = '{}', Col 4 = '{}'", firstNameCell, emailCell);
        Assert.assertFalse(firstNameCell.isEmpty());
    }

    @Test(priority = 7, groups = {"assignments", "misc"}, description = "7. Handling Ajax Auto Suggestion on Autocomplete page")
    public void testAjaxAutoSuggestion() {
        HomePage home = loginToMagnus();
        home.goToMoreSubPage("nav.autocomplete.xpath");
        WebElement autoInput = driver.findElement(By.id("txtSingleAutoComplete"));
        autoInput.sendKeys("Java");
        Assert.assertEquals(autoInput.getAttribute("value"), "Java");
        List<WebElement> lists = driver.findElements(By.cssSelector("ul.ui-autocomplete"));
        Assert.assertTrue(lists.size() >= 1, "jQuery UI autocomplete container should exist");
    }

    @Test(priority = 8, groups = {"assignments", "misc"}, description = "8. Select specific date from Calendar DatePicker on Employee Create")
    public void testCalendarDateSelection() {
        HomePage home = loginToMagnus();
        home.goToEmployeeCreate();
        WebElement dob = driver.findElement(By.id("DOB"));
        dob.click();
        // Select a specific visible day cell from the Bootstrap DatePicker popup if opened
        List<WebElement> days = driver.findElements(By.cssSelector("div.datepicker td.day:not(.old):not(.new)"));
        if (!days.isEmpty()) {
            days.get(14).click(); // 15th of current month
            Assert.assertFalse(dob.getAttribute("value").isEmpty(), "DOB input should be populated by calendar click");
        } else {
            dob.sendKeys("15/08/1995");
            Assert.assertEquals(dob.getAttribute("value"), "15/08/1995");
        }
    }

    @Test(priority = 9, groups = {"assignments", "misc"}, description = "9. Cookies operations: Add, Read, and Delete cookies")
    public void testCookiesOperations() {
        loginToMagnus();
        PageActions actions = new PageActions(driver);

        actions.addCookie("jala_qa_cookie", "magnus_verified");
        Cookie added = driver.manage().getCookieNamed("jala_qa_cookie");
        Assert.assertNotNull(added, "Added cookie must exist");
        Assert.assertEquals(added.getValue(), "magnus_verified");

        Set<Cookie> cookiesBeforeDelete = driver.manage().getCookies();
        Assert.assertTrue(cookiesBeforeDelete.size() >= 1);

        actions.deleteCookie("jala_qa_cookie");
        Assert.assertNull(driver.manage().getCookieNamed("jala_qa_cookie"), "Deleted cookie must be null");
    }
}
