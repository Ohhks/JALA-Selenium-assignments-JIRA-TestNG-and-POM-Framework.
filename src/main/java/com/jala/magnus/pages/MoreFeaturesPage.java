package com.jala.magnus.pages;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.utils.LocatorReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * PageObjects class covering all 'More' module pages in Magnus:
 * Multiple Tabs, Menu, Autocomplete, Collapse, Images, Slider, Tooltip,
 * Popups, Links, CSS Properties, and iFrames.
 */
public class MoreFeaturesPage {
    private final WebDriver driver;
    private final PageActions actions;

    public MoreFeaturesPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new PageActions(driver);
    }

    // --- Multiple Tabs ---
    public void openTab(String linkKey) {
        actions.click(LocatorReader.getBy(linkKey));
    }

    // --- Menu ---
    public void clickSingleMenuTesting() {
        actions.click(LocatorReader.getBy("menu.testingSingle.id"));
    }

    public String getMenuResultText() {
        return actions.getText(LocatorReader.getBy("menu.outputLabel.id"));
    }

    public void hoverSubMenuTestingAndClickSelenium() {
        actions.click(LocatorReader.getBy("menu.subMenusTab.linkText"));
        actions.hover(LocatorReader.getBy("menu.subTestingHover.xpath"));
        actions.click(LocatorReader.getBy("menu.subSelenium.id"));
    }

    // --- Autocomplete (Ajax) ---
    public String selectSingleAutoComplete(String prefix, String expectedMatch) {
        By inputLoc = LocatorReader.getBy("auto.singleInput.id");
        actions.type(inputLoc, prefix);
        List<WebElement> items = actions.findAll("auto.suggestions.css");
        for (WebElement item : items) {
            if (item.getText().trim().equalsIgnoreCase(expectedMatch)) {
                item.click();
                break;
            }
        }
        return actions.getValue(inputLoc);
    }

    // --- Collapse ---
    public void toggleFirstCollapsePanel() {
        actions.click(LocatorReader.getBy("collapse.firstPanel.css"));
    }

    // --- Slider ---
    public String moveSliderByOffset(int xOffset) {
        actions.dragAndDropByOffset(LocatorReader.getBy("slider.handle.css"), xOffset, 0);
        return actions.getText(By.id("ex6SliderVal"));
    }

    // --- Tooltip ---
    public String getTooltipTextOnHover() {
        By btn = LocatorReader.getBy("tooltip.btn.id");
        actions.hover(btn);
        return actions.getAttribute(btn, "data-original-title");
    }

    // --- Popups / Alerts / Windows ---
    public String triggerAndAcceptAlert() {
        actions.click(LocatorReader.getBy("popup.alertBox.id"));
        return actions.acceptAlertAndGetText();
    }

    public String triggerAndDismissConfirm() {
        actions.click(LocatorReader.getBy("popup.confirmBox.id"));
        return actions.dismissAlertAndGetText();
    }

    public void triggerPromptAndEnterText(String text) {
        actions.click(LocatorReader.getBy("popup.promptBox.id"));
        actions.sendKeysAndAcceptPrompt(text);
    }

    public String openPopupOneAndGetChildTitle() {
        String parent = driver.getWindowHandle();
        actions.click(LocatorReader.getBy("popup.one.id"));
        actions.switchToChildWindow(parent);
        String title = driver.getTitle();
        driver.close();
        driver.switchTo().window(parent);
        return title;
    }

    public boolean openInWindowModalAndCheckVisible() {
        actions.click(LocatorReader.getBy("popup.inWindow.id"));
        return actions.isDisplayed(By.id("myModal1"));
    }

    // --- Links ---
    public int getTotalLinksCount() {
        return driver.findElements(LocatorReader.getBy("links.allLinks.tagName")).size();
    }

    public String getBrokenLinkHref() {
        actions.click(LocatorReader.getBy("links.brokenTab.linkText"));
        return driver.findElement(By.cssSelector("#tab_2 a")).getAttribute("href");
    }

    // --- iFrames ---
    public void switchToIframe(String iframeKey) {
        actions.switchToFrame(LocatorReader.getBy(iframeKey));
    }

    public void switchBackFromIframe() {
        actions.switchToDefaultContent();
    }
}
