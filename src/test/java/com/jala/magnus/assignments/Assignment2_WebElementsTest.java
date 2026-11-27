package com.jala.magnus.assignments;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.EmployeeCreatePage;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.pages.MoreFeaturesPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Assignment 2: Operations on Web Elements (Text Box, Radio Button/Check Box, Dropdown, Link, XPath)
 * Implemented strictly on Magnus Application.
 */
public class Assignment2_WebElementsTest extends BaseTest {

    @Test(priority = 1, groups = {"assignments", "webelements"}, description = "Text Box Operations: Type, Get Value, Placeholder, Clear, isEnabled")
    public void testTextBoxOperations() {
        HomePage home = loginToMagnus();
        EmployeeCreatePage emp = home.goToEmployeeCreate();
        PageActions actions = new PageActions(driver);

        By firstName = By.id("FirstName");
        // 1. Type in text box
        actions.type(firstName, "Siddharth");
        // 2. Get Text Box value
        Assert.assertEquals(actions.getValue(firstName), "Siddharth");
        // 3. Read placeholder using getAttribute("placeholder")
        Assert.assertEquals(actions.getPlaceholder(firstName), "First Name");
        // 4. Clear text from Text Box
        actions.clearText(firstName);
        Assert.assertEquals(actions.getValue(firstName), "");
        // 5. Check if Text Box is enabled
        Assert.assertTrue(actions.isEnabled(firstName));
        log.info("Assignment 2 - TextBox operations verified successfully");
    }

    @Test(priority = 2, groups = {"assignments", "webelements"}, description = "Radio Button & Check Box Operations on Employee Create")
    public void testRadioAndCheckBoxOperations() {
        HomePage home = loginToMagnus();
        EmployeeCreatePage emp = home.goToEmployeeCreate();

        // 1. Selecting Radio Button by value ('F')
        emp.selectGender("F");
        // 2. Number of elements in radio group
        Assert.assertEquals(emp.getGenderRadioCount(), 2);
        // 3. Find all radio button values
        List<String> values = emp.getAllGenderValues();
        Assert.assertTrue(values.contains("M") && values.contains("F"));
        // 4 & 5. Selected radio button value / isSelected
        Assert.assertEquals(emp.getSelectedGender(), "F");
        Assert.assertTrue(driver.findElement(By.id("rdbFemale")).isSelected());
        // 6. Enabled check
        Assert.assertTrue(driver.findElement(By.id("rdbMale")).isEnabled());

        // CheckBox verification on Skills
        Assert.assertEquals(emp.getSkillCheckboxCount(), 6);
        emp.selectSkillQA(true);
        Assert.assertTrue(driver.findElement(By.id("chkSkill_1")).isSelected());
        log.info("Assignment 2 - Radio Button and CheckBox operations verified");
    }

    @Test(priority = 3, groups = {"assignments", "webelements"}, description = "Dropdown Operations: Print all, First Selected, By Value, By VisibleText, By Index")
    public void testDropdownOperations() {
        HomePage home = loginToMagnus();
        EmployeeCreatePage emp = home.goToEmployeeCreate();
        By countryLoc = By.id("CountryId");
        Select countrySelect = new Select(driver.findElement(countryLoc));

        // 1. Print all options available
        List<String> allCountries = emp.getAllCountryOptions();
        log.info("Countries available: {}", allCountries);
        Assert.assertTrue(allCountries.contains("India"));

        // 2. First selected option
        Assert.assertEquals(countrySelect.getFirstSelectedOption().getText().trim(), "--Select Country--");

        // 3. Select by value ("3" -> USA)
        countrySelect.selectByValue("3");
        Assert.assertEquals(countrySelect.getFirstSelectedOption().getText().trim(), "USA");

        // 4. Select by visible text ("India")
        countrySelect.selectByVisibleText("India");
        Assert.assertEquals(countrySelect.getFirstSelectedOption().getText().trim(), "India");

        // 5. Select by index (1 -> Bangladesh)
        countrySelect.selectByIndex(1);
        Assert.assertEquals(countrySelect.getFirstSelectedOption().getText().trim(), "Bangladesh");
    }

    @Test(priority = 4, groups = {"assignments", "webelements"}, description = "Link Operations: partialLinkText, linkText, find all links, image links")
    public void testLinkOperations() {
        HomePage home = loginToMagnus();
        MoreFeaturesPage more = home.goToMoreSubPage("nav.links.xpath");

        // 1. Click using partialLinkText
        driver.findElement(By.partialLinkText("Broken")).click();
        // 2. Click using linkText
        driver.findElement(By.linkText("Image Links")).click();
        // 3. Find all links on page
        List<WebElement> allLinks = driver.findElements(By.tagName("a"));
        log.info("Total links on Links page: {}", allLinks.size());
        Assert.assertTrue(allLinks.size() > 10);
        // 4. Locate image link inside tab_3
        List<WebElement> imgLinks = driver.findElements(By.cssSelector("#tab_3 a img"));
        Assert.assertTrue(imgLinks.size() >= 1, "Expected image links under Image Links tab");
    }

    @Test(priority = 5, groups = {"assignments", "webelements"}, description = "XPath Operations: text color, id/name/class, contains, text, starts-with, axes, OR/AND")
    public void testXPathOperations() {
        HomePage home = loginToMagnus();
        home.goToMoreSubPage("nav.cssProperty.xpath");

        // 1. Read label text and its CSS color
        driver.findElement(By.linkText("Labels")).click();
        WebElement labelEl = driver.findElement(By.xpath("//div[@id='tab_2']//span[contains(@class,'label')]"));
        log.info("Label text: '{}' | CSS Color: {}", labelEl.getText(), labelEl.getCssValue("background-color"));

        // Go to Employee Create for full XPath axis checks
        driver.get("https://magnus.jalatechnologies.com/Employee/Create");
        // 2. XPath with id, name, className
        Assert.assertTrue(driver.findElement(By.xpath("//input[@id='FirstName']")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//input[@name='LastName']")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//input[contains(@class,'mydatepicker')]")).isDisplayed());
        // 3. contains()
        Assert.assertTrue(driver.findElement(By.xpath("//button[contains(text(),'Save')]")).isDisplayed());
        // 4. text()
        Assert.assertTrue(driver.findElement(By.xpath("//h3[text()='Employee Details']")).isDisplayed());
        // 5. starts-with()
        Assert.assertTrue(driver.findElement(By.xpath("//input[starts-with(@id,'Email')]")).isDisplayed());
        // 6. following-sibling & preceding
        WebElement emailInput = driver.findElement(By.xpath("//label[@for='EmailId']/following-sibling::input"));
        Assert.assertEquals(emailInput.getAttribute("id"), "EmailId");
        WebElement precedingLabel = driver.findElement(By.xpath("//input[@id='EmailId']/preceding::label[1]"));
        Assert.assertEquals(precedingLabel.getText().trim(), "Email");
        // 7. ancestor, parent, child, descendant
        WebElement parentDiv = driver.findElement(By.xpath("//input[@id='FirstName']/parent::div"));
        Assert.assertTrue(parentDiv.getAttribute("class").contains("form-group"));
        WebElement ancestorForm = driver.findElement(By.xpath("//input[@id='FirstName']/ancestor::form"));
        Assert.assertNotNull(ancestorForm);
        // 8. OR and AND in XPath
        WebElement andEl = driver.findElement(By.xpath("//input[@id='MobileNo' and @name='MobileNo']"));
        Assert.assertTrue(andEl.isDisplayed());
        List<WebElement> orEls = driver.findElements(By.xpath("//input[@id='FirstName' or @id='LastName']"));
        Assert.assertEquals(orEls.size(), 2);
    }
}
