package com.jala.magnus.tests;

import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.EmployeeCreatePage;
import com.jala.magnus.pages.EmployeeSearchPage;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.utils.ConfigReader;
import com.jala.magnus.utils.ExcelUtil;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Framework Steps 8-12, 17-18:
 * Employee Module Test Suite (10 Test Scripts) inheriting BaseTest.
 */
public class EmployeeModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke", "employee"}, description = "TC_EMP_01: Navigate to Create Employee Page")
    public void verifyNavigateToCreateEmployee() {
        HomePage home = loginToMagnus();
        home.goToEmployeeCreate();
        Assert.assertEquals(driver.getTitle(), "Create Employee");
    }

    @Test(priority = 2, groups = {"regression", "employee"}, description = "TC_EMP_02: Verify Gender Radio Group Options and Selection")
    public void verifyGenderRadioSelection() {
        HomePage home = loginToMagnus();
        EmployeeCreatePage emp = home.goToEmployeeCreate();
        Assert.assertEquals(emp.getGenderRadioCount(), 2);
        emp.selectGender("M");
        Assert.assertEquals(emp.getSelectedGender(), "M");
        emp.selectGender("F");
        Assert.assertEquals(emp.getSelectedGender(), "F");
    }

    @Test(priority = 3, groups = {"regression", "employee"}, description = "TC_EMP_03: Verify Country Cascading City Dropdown")
    public void verifyCountryAndCityDropdown() {
        HomePage home = loginToMagnus();
        EmployeeCreatePage emp = home.goToEmployeeCreate();
        emp.selectCountryByVisibleText("India");
        List<String> cities = emp.getAllCityOptions();
        Assert.assertTrue(cities.contains("Hyderabad"), "India cities should include Hyderabad");
        emp.selectCityByVisibleText("Hyderabad");
    }

    @Test(priority = 4, groups = {"regression", "employee"}, description = "TC_EMP_04: Verify Skills Multi-Checkboxes Selection")
    public void verifySkillsCheckboxes() {
        HomePage home = loginToMagnus();
        EmployeeCreatePage emp = home.goToEmployeeCreate();
        Assert.assertEquals(emp.getSkillCheckboxCount(), 6);
        emp.selectSkillQA(true);
        emp.selectSkillDevOps(true);
        Assert.assertTrue(driver.findElement(By.id("chkSkill_1")).isSelected());
        Assert.assertTrue(driver.findElement(By.id("chkSkill_3")).isSelected());
    }

    @Test(priority = 5, groups = {"ui", "employee"}, description = "TC_EMP_05: Verify Cancel Button Navigates to Search")
    public void verifyCancelButtonNavigatesToSearch() {
        HomePage home = loginToMagnus();
        EmployeeCreatePage emp = home.goToEmployeeCreate();
        emp.clickCancel();
        Assert.assertTrue(driver.getCurrentUrl().contains("/Employee/Search"));
    }

    @Test(priority = 6, groups = {"smoke", "employee"}, description = "TC_EMP_06: Navigate to Employee Search and Verify Web Table")
    public void verifyEmployeeSearchTableLoads() {
        HomePage home = loginToMagnus();
        EmployeeSearchPage search = home.goToEmployeeSearch();
        Assert.assertEquals(driver.getTitle(), "Search Employee");
        Assert.assertTrue(search.getRowCount() >= 1, "Employee table should display records");
        Assert.assertEquals(search.getHeaderCount(), 9);
    }

    @Test(priority = 7, groups = {"regression", "employee"}, description = "TC_EMP_07: Search Employee by Name and Clear Filter")
    public void verifySearchAndClearFilter() {
        HomePage home = loginToMagnus();
        EmployeeSearchPage search = home.goToEmployeeSearch();
        search.searchByName("ARUN");
        Assert.assertEquals(driver.findElement(By.id("Name")).getAttribute("value"), "ARUN");
        search.clickClear();
        Assert.assertEquals(driver.findElement(By.id("Name")).getAttribute("value"), "");
    }

    @Test(priority = 8, groups = {"navigation", "employee"}, description = "TC_EMP_08: Add Employee Button Navigates to Create Page")
    public void verifyAddEmployeeNavigationFromSearch() {
        HomePage home = loginToMagnus();
        EmployeeSearchPage search = home.goToEmployeeSearch();
        search.clickAddEmployee();
        Assert.assertTrue(driver.getCurrentUrl().contains("/Employee/Create"));
    }

    @Test(priority = 9, groups = {"regression", "employee"}, description = "TC_EMP_09: Verify Dynamic Table Cell Data Retrieval")
    public void verifyDynamicTableCellExtraction() {
        HomePage home = loginToMagnus();
        EmployeeSearchPage search = home.goToEmployeeSearch();
        String firstName = search.getCellData(1, 1);
        String mobile = search.getCellData(1, 3);
        Assert.assertFalse(firstName.isEmpty(), "First row first name should not be blank");
        Assert.assertFalse(mobile.isEmpty(), "First row mobile should not be blank");
    }

    @DataProvider(name = "excelEmployeeData")
    public Object[][] getEmployeeExcelData() {
        return ExcelUtil.getSheetData(ConfigReader.get("testdata.excel"), "EmployeeData");
    }

    @Test(priority = 10, dataProvider = "excelEmployeeData", groups = {"datadriven", "employee"},
            description = "TC_EMP_10: Excel-Driven Employee Form Population via Apache POI & DataProvider")
    public void verifyExcelDrivenEmployeeFormPopulation(String firstName, String lastName, String email,
                                                        String mobile, String dob, String gender,
                                                        String address, String country, String city) {
        HomePage home = loginToMagnus();
        EmployeeCreatePage emp = home.goToEmployeeCreate();
        emp.enterFirstName(firstName);
        emp.enterLastName(lastName);
        emp.enterEmail(email);
        emp.enterMobile(mobile);
        emp.enterDob(dob);
        emp.selectGender(gender);
        emp.enterAddress(address);
        emp.selectCountryByVisibleText(country);
        emp.selectCityByVisibleText(city);
        Assert.assertEquals(driver.findElement(By.id("FirstName")).getAttribute("value"), firstName);
        Assert.assertEquals(emp.getSelectedGender(), gender);
    }
}
