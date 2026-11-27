package com.jala.magnus.pages;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.utils.LocatorReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * PageObjects class for Employee Create (/Employee/Create).
 * Covers TextBoxes, Radio Buttons, Checkboxes, Cascading Dropdowns, and Calendar DatePicker.
 */
public class EmployeeCreatePage {
    private final WebDriver driver;
    private final PageActions actions;

    public EmployeeCreatePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new PageActions(driver);
    }

    public void enterFirstName(String firstName) {
        actions.type(LocatorReader.getBy("emp.firstName.id"), firstName);
    }

    public void enterLastName(String lastName) {
        actions.type(LocatorReader.getBy("emp.lastName.id"), lastName);
    }

    public void enterEmail(String email) {
        actions.type(LocatorReader.getBy("emp.email.id"), email);
    }

    public void enterMobile(String mobile) {
        actions.type(LocatorReader.getBy("emp.mobile.id"), mobile);
    }

    public void enterDob(String dobDdMmYyyy) {
        actions.type(LocatorReader.getBy("emp.dob.id"), dobDdMmYyyy);
    }

    public void selectGender(String genderCode) {
        actions.selectRadioByValue(LocatorReader.getBy("emp.genderRadios.name"), genderCode);
    }

    public int getGenderRadioCount() {
        return driver.findElements(LocatorReader.getBy("emp.genderRadios.name")).size();
    }

    public List<String> getAllGenderValues() {
        return actions.getAllRadioValues(LocatorReader.getBy("emp.genderRadios.name"));
    }

    public String getSelectedGender() {
        return actions.getSelectedRadioValue(LocatorReader.getBy("emp.genderRadios.name"));
    }

    public void enterAddress(String address) {
        actions.type(LocatorReader.getBy("emp.address.id"), address);
    }

    public void selectCountryByVisibleText(String country) {
        actions.selectByVisibleText(LocatorReader.getBy("emp.country.id"), country);
    }

    public void selectCountryByValue(String val) {
        actions.selectByValue(LocatorReader.getBy("emp.country.id"), val);
    }

    public void selectCountryByIndex(int index) {
        actions.selectByIndex(LocatorReader.getBy("emp.country.id"), index);
    }

    public List<String> getAllCountryOptions() {
        return actions.getAllDropdownOptions(LocatorReader.getBy("emp.country.id"));
    }

    public String getSelectedCountry() {
        return actions.getFirstSelectedDropdownOption(LocatorReader.getBy("emp.country.id"));
    }

    public void selectCityByVisibleText(String city) {
        By cityLoc = LocatorReader.getBy("emp.city.id");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(d -> d.findElements(By.cssSelector("#CityId option")).size() > 1);
        actions.selectByVisibleText(cityLoc, city);
    }

    public List<String> getAllCityOptions() {
        By cityLoc = LocatorReader.getBy("emp.city.id");
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(d -> d.findElements(By.cssSelector("#CityId option")).size() > 1);
        return actions.getAllDropdownOptions(cityLoc);
    }

    public void checkOtherCity(boolean check) {
        actions.setCheckbox(LocatorReader.getBy("emp.otherCityChk.id"), check);
    }

    public boolean isOtherCityInputDisplayed() {
        return actions.isDisplayed(LocatorReader.getBy("emp.otherCityTxt.id"));
    }

    public void selectSkillQA(boolean check) {
        actions.setCheckbox(LocatorReader.getBy("emp.skillQA.id"), check);
    }

    public void selectSkillDevOps(boolean check) {
        actions.setCheckbox(LocatorReader.getBy("emp.skillDevOps.id"), check);
    }

    public int getSkillCheckboxCount() {
        return driver.findElements(LocatorReader.getBy("emp.skillsCheckboxes.css")).size();
    }

    public void clickSave() {
        actions.click(LocatorReader.getBy("emp.saveBtn.xpath"));
    }

    public void clickCancel() {
        actions.click(LocatorReader.getBy("emp.cancelBtn.xpath"));
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/Employee/Search"));
    }

    public String getSkillsSectionLabelText() {
        List<WebElement> labels = driver.findElements(By.xpath("//label[contains(text(),'Sk')]"));
        return labels.isEmpty() ? "" : labels.get(0).getText().trim();
    }
}
