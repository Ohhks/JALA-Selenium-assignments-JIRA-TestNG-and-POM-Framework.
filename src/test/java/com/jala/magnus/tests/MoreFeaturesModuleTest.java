package com.jala.magnus.tests;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.pages.MoreFeaturesPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Framework Steps 8-12:
 * More Features Module Test Suite (10 Test Scripts) inheriting BaseTest.
 */
public class MoreFeaturesModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke", "more"}, description = "TC_MORE_01: Verify Multiple Tabs Switching")
    public void verifyMultipleTabsSwitching() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.tabs.xpath");
        more.openTab("tabs.unlearning.linkText");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.attributeContains(By.id("tab_2"), "class", "active"));
        Assert.assertTrue(driver.findElement(By.id("tab_2")).getAttribute("class").contains("active"));
        more.openTab("tabs.waysOfUnlearning.linkText");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.attributeContains(By.id("tab_3"), "class", "active"));
        Assert.assertTrue(driver.findElement(By.id("tab_3")).getAttribute("class").contains("active"));
    }

    @Test(priority = 2, groups = {"regression", "more"}, description = "TC_MORE_02: Verify Menu Page Tabs & Items")
    public void verifyMenuNavigation() {
        HomePage home = loginToMagnus();
        home.goToMoreSubPage("nav.menu.xpath");
        Assert.assertEquals(driver.getTitle(), "Menu");
        Assert.assertTrue(driver.findElement(By.id("b1")).isDisplayed());
        driver.findElement(By.linkText("Sub Menus")).click();
        Assert.assertTrue(driver.findElement(By.id("tab_2")).getAttribute("class").contains("active"));
    }

    @Test(priority = 3, groups = {"regression", "more"}, description = "TC_MORE_03: Verify Autocomplete Single & Multiple Tabs")
    public void verifyAutocompleteInputs() {
        HomePage home = loginToMagnus();
        home.goToMoreSubPage("nav.autocomplete.xpath");
        WebElement single = driver.findElement(By.id("txtSingleAutoComplete"));
        single.sendKeys("Python");
        Assert.assertEquals(single.getAttribute("value"), "Python");
        driver.findElement(By.linkText("Multiple Values")).click();
        Assert.assertTrue(driver.findElement(By.id("txtMultipleAutoComplete")).isDisplayed());
    }

    @Test(priority = 4, groups = {"regression", "more"}, description = "TC_MORE_04: Verify Collapsible Accordion Panels")
    public void verifyCollapsibleContent() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.collapse.xpath");
        Assert.assertEquals(driver.getTitle(), "Collapse");
        more.toggleFirstCollapsePanel();
        Assert.assertTrue(driver.findElement(By.cssSelector("#tab_1 .panel-title a")).isDisplayed());
    }

    @Test(priority = 5, groups = {"regression", "more"}, description = "TC_MORE_05: Verify Upload Image Page Elements")
    public void verifyImagesPageElements() {
        HomePage home = loginToMagnus();
        home.goToMoreSubPage("nav.images.xpath");
        Assert.assertTrue(driver.findElement(By.id("file")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("fileName")).isDisplayed());
    }

    @Test(priority = 6, groups = {"regression", "more"}, description = "TC_MORE_06: Verify Slider Drag Interaction")
    public void verifySliderDrag() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.slider.xpath");
        String initial = driver.findElement(By.id("ex6SliderVal")).getText().trim();
        more.moveSliderByOffset(250);
        String updated = driver.findElement(By.id("ex6SliderVal")).getText().trim();
        Assert.assertNotEquals(updated, initial);
    }

    @Test(priority = 7, groups = {"ui", "more"}, description = "TC_MORE_07: Verify Tooltip Hover Attribute")
    public void verifyTooltipOnHover() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.tooltip.xpath");
        String tooltipText = more.getTooltipTextOnHover();
        Assert.assertTrue(tooltipText.contains("Click me"), "Tooltip text mismatch: " + tooltipText);
    }

    @Test(priority = 8, groups = {"smoke", "more"}, description = "TC_MORE_08: Verify Alert, Confirm, Prompt & Child Window Popups")
    public void verifyAlertsAndChildWindow() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.popup.xpath");
        Assert.assertEquals(more.triggerAndAcceptAlert(), "This is an alert Box!");
        Assert.assertEquals(more.triggerAndDismissConfirm(), "Confirm Message Box");
        more.triggerPromptAndEnterText("MagnusPOM");
        String childTitle = more.openPopupOneAndGetChildTitle();
        Assert.assertFalse(childTitle.isEmpty());
    }

    @Test(priority = 9, groups = {"regression", "more"}, description = "TC_MORE_09: Verify Links Tabs & CSS Properties Page")
    public void verifyLinksAndCssProperties() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.links.xpath");
        Assert.assertTrue(more.getTotalLinksCount() > 10);

        home.goToMoreSubPage("nav.cssProperty.xpath");
        PageActions actions = new PageActions(driver);
        driver.findElement(By.linkText("Buttons")).click();
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.attributeContains(By.id("tab_3"), "class", "active"));
        List<WebElement> btns = driver.findElements(By.cssSelector("#tab_3 button"));
        Assert.assertTrue(btns.size() >= 4, "Expected styled buttons under CSS Properties");
        String bgColor = actions.getCssValue(By.cssSelector("#tab_3 button.btn-primary"), "background-color");
        Assert.assertFalse(bgColor.isEmpty());
    }

    @Test(priority = 10, groups = {"regression", "more"}, description = "TC_MORE_10: Verify iFrame Switching & Content Verification")
    public void verifyIframeSwitching() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.iframe.xpath");
        more.switchToIframe("iframe.two.id");
        Assert.assertTrue(driver.getPageSource().contains("Welcome to JALA Academy"));
        more.switchBackFromIframe();
        Assert.assertEquals(driver.getTitle(), "Iframe");
    }
}
