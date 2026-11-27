package com.jala.magnus.actions;

import com.jala.magnus.utils.ConfigReader;
import com.jala.magnus.utils.LocatorReader;
import com.jala.magnus.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Framework Step 7: PageActions class encapsulating all Selenium WebElement, Alert, Window,
 * Frame, Dropdown, Radio/Checkbox, Actions, Wait, Cookie, and Broken-Link operations.
 */
public class PageActions {
    private static final Logger log = LoggerUtil.getLogger(PageActions.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    public PageActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
        this.actions = new Actions(driver);
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement find(String locatorKey) {
        return waitForVisible(LocatorReader.getBy(locatorKey));
    }

    public List<WebElement> findAll(String locatorKey) {
        return driver.findElements(LocatorReader.getBy(locatorKey));
    }

    // --- TextBox Operations ---
    public void type(By locator, String text) {
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
        log.info("Typed '{}' into {}", text, locator);
    }

    public void type(String locatorKey, String text) {
        type(LocatorReader.getBy(locatorKey), text);
    }

    public void clearText(By locator) {
        WebElement el = waitForVisible(locator);
        el.clear();
        log.info("Cleared text in {}", locator);
    }

    public String getValue(By locator) {
        return waitForVisible(locator).getAttribute("value");
    }

    public String getPlaceholder(By locator) {
        return waitForVisible(locator).getAttribute("placeholder");
    }

    public String getAttribute(By locator, String attr) {
        return driver.findElement(locator).getAttribute(attr);
    }

    public String getCssValue(By locator, String propertyName) {
        return waitForVisible(locator).getCssValue(propertyName);
    }

    public String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    public String getTagName(By locator) {
        return driver.findElement(locator).getTagName();
    }

    public Dimension getSize(By locator) {
        return waitForVisible(locator).getSize();
    }

    public Point getLocation(By locator) {
        return waitForVisible(locator).getLocation();
    }

    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        return driver.findElement(locator).isEnabled();
    }

    public boolean isSelected(By locator) {
        return driver.findElement(locator).isSelected();
    }

    // --- Click Operations ---
    public void click(By locator) {
        waitForClickable(locator).click();
        log.info("Clicked {}", locator);
    }

    public void click(String locatorKey) {
        click(LocatorReader.getBy(locatorKey));
    }

    public void jsClick(By locator) {
        WebElement el = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", el);
    }

    // --- Radio Button & Checkbox Operations ---
    public void selectRadioByValue(By groupLocator, String value) {
        List<WebElement> radios = driver.findElements(groupLocator);
        for (WebElement r : radios) {
            if (value.equalsIgnoreCase(r.getAttribute("value"))) {
                if (!r.isSelected()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", r);
                }
                log.info("Selected radio value '{}'", value);
                return;
            }
        }
        throw new NoSuchElementException("Radio option not found with value: " + value);
    }

    public List<String> getAllRadioValues(By groupLocator) {
        List<String> values = new ArrayList<>();
        for (WebElement r : driver.findElements(groupLocator)) {
            values.add(r.getAttribute("value"));
        }
        return values;
    }

    public String getSelectedRadioValue(By groupLocator) {
        for (WebElement r : driver.findElements(groupLocator)) {
            if (r.isSelected()) {
                return r.getAttribute("value");
            }
        }
        return "";
    }

    public void setCheckbox(By locator, boolean check) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        if (el.isSelected() != check) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", el);
        }
    }

    // --- Dropdown Operations ---
    public List<String> getAllDropdownOptions(By selectLocator) {
        return wait.until(d -> {
            try {
                Select select = new Select(d.findElement(selectLocator));
                List<String> options = new ArrayList<>();
                for (WebElement opt : select.getOptions()) {
                    options.add(opt.getText().trim());
                }
                return options;
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
    }

    public String getFirstSelectedDropdownOption(By selectLocator) {
        Select select = new Select(waitForVisible(selectLocator));
        return select.getFirstSelectedOption().getText().trim();
    }

    public void selectByVisibleText(By selectLocator, String visibleText) {
        new Select(waitForVisible(selectLocator)).selectByVisibleText(visibleText);
        log.info("Selected '{}' from {}", visibleText, selectLocator);
    }

    public void selectByValue(By selectLocator, String value) {
        new Select(waitForVisible(selectLocator)).selectByValue(value);
        log.info("Selected value '{}' from {}", value, selectLocator);
    }

    public void selectByIndex(By selectLocator, int index) {
        new Select(waitForVisible(selectLocator)).selectByIndex(index);
        log.info("Selected index {} from {}", index, selectLocator);
    }

    // --- Alerts, Windows & Frames ---
    public Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    public String acceptAlertAndGetText() {
        Alert alert = waitForAlert();
        String text = alert.getText();
        alert.accept();
        log.info("Accepted alert with text: '{}'", text);
        return text;
    }

    public String dismissAlertAndGetText() {
        Alert alert = waitForAlert();
        String text = alert.getText();
        alert.dismiss();
        log.info("Dismissed alert with text: '{}'", text);
        return text;
    }

    public void sendKeysAndAcceptPrompt(String inputText) {
        Alert alert = waitForAlert();
        alert.sendKeys(inputText);
        alert.accept();
        log.info("Submitted prompt alert with text: '{}'", inputText);
    }

    public String switchToChildWindow(String parentHandle) {
        wait.until(d -> d.getWindowHandles().size() > 1);
        Set<String> handles = driver.getWindowHandles();
        for (String h : handles) {
            if (!h.equals(parentHandle)) {
                driver.switchTo().window(h);
                return h;
            }
        }
        return parentHandle;
    }

    public void switchToFrame(By frameLocator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
        log.info("Switched to frame {}", frameLocator);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // --- Actions Class Operations ---
    public void hover(By locator) {
        WebElement el = waitForVisible(locator);
        actions.moveToElement(el).perform();
        log.info("Hovered over {}", locator);
    }

    public void doubleClick(By locator) {
        WebElement el = waitForVisible(locator);
        actions.doubleClick(el).perform();
    }

    public void dragAndDropByOffset(By locator, int xOffset, int yOffset) {
        WebElement el = waitForVisible(locator);
        actions.clickAndHold(el).moveByOffset(xOffset, yOffset).release().perform();
        log.info("Dragged {} by ({}, {})", locator, xOffset, yOffset);
    }

    public void pressEnter(By locator) {
        WebElement el = waitForVisible(locator);
        actions.click(el).sendKeys(Keys.ENTER).perform();
    }

    // --- Broken Link Checker ---
    public int checkHttpStatus(String urlStr) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            conn.connect();
            return conn.getResponseCode();
        } catch (Exception e) {
            return -1;
        }
    }

    // --- Cookie Operations ---
    public void addCookie(String name, String value) {
        driver.manage().addCookie(new Cookie(name, value));
    }

    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }
}
