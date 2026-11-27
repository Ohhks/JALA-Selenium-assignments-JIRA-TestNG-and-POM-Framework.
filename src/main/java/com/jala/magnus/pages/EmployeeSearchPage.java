package com.jala.magnus.pages;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.utils.LocatorReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * PageObjects class for Employee Search (/Employee/Search).
 * Covers Dynamic Web Table Operations: row count, cell text, search, clear, edit, delete.
 */
public class EmployeeSearchPage {
    private final WebDriver driver;
    private final PageActions actions;

    public EmployeeSearchPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new PageActions(driver);
    }

    public void searchByName(String name) {
        actions.type(LocatorReader.getBy("search.name.id"), name);
        actions.click(LocatorReader.getBy("search.searchBtn.id"));
    }

    public void searchByMobile(String mobile) {
        actions.type(LocatorReader.getBy("search.mobile.id"), mobile);
        actions.click(LocatorReader.getBy("search.searchBtn.id"));
    }

    public void clickClear() {
        actions.click(LocatorReader.getBy("search.clearBtn.id"));
    }

    public EmployeeCreatePage clickAddEmployee() {
        actions.click(LocatorReader.getBy("search.addEmployeeBtn.linkText"));
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/Employee/Create"));
        return new EmployeeCreatePage(driver);
    }

    public int getRowCount() {
        return driver.findElements(LocatorReader.getBy("search.tableRows.css")).size();
    }

    public int getHeaderCount() {
        return driver.findElements(LocatorReader.getBy("search.tableHeaders.css")).size();
    }

    public String getCellData(int rowOneBased, int colOneBased) {
        By cellLoc = By.xpath("//table[@id='tblEmployee']/tbody/tr[" + rowOneBased + "]/td[" + colOneBased + "]");
        return actions.getText(cellLoc);
    }

    public int countElementsById(String id) {
        return driver.findElements(By.id(id)).size();
    }

    public List<WebElement> getAllRows() {
        return driver.findElements(LocatorReader.getBy("search.tableRows.css"));
    }
}
