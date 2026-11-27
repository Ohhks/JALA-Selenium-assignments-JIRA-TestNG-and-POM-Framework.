package com.jala.magnus.tests;

import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.pages.LoginPage;
import com.jala.magnus.utils.ConfigReader;
import com.jala.magnus.utils.ExcelUtil;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Framework Steps 8-12, 17-18:
 * Login Module Test Suite (10 Test Scripts) inheriting BaseTest.
 */
public class LoginModuleTest extends BaseTest {

    @Test(priority = 1, groups = {"smoke", "login"}, description = "TC_LOGIN_01: Verify Login Page Title and URL")
    public void verifyLoginPageTitleAndUrl() {
        Assert.assertEquals(driver.getTitle(), "Login");
        Assert.assertTrue(driver.getCurrentUrl().contains("magnus.jalatechnologies.com"));
    }

    @Test(priority = 2, groups = {"smoke", "login"}, description = "TC_LOGIN_02: Verify Valid User Login")
    public void verifyValidUserLogin() {
        HomePage home = loginToMagnus();
        Assert.assertTrue(home.isLoaded(), "Home dashboard should load after valid login");
        Assert.assertEquals(driver.getTitle(), "Magnus");
    }

    @Test(priority = 3, groups = {"regression", "login"}, description = "TC_LOGIN_03: Verify Invalid Credentials Stay on Login Page")
    public void verifyInvalidUserLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("wronguser@jala.com", "invalid123");
        Assert.assertTrue(loginPage.isOnLoginPage(), "Invalid login must stay on Login screen");
    }

    @Test(priority = 4, groups = {"regression", "login"}, description = "TC_LOGIN_04: Verify Empty Credentials Submit")
    public void verifyEmptyCredentialsLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickSignIn();
        Assert.assertTrue(loginPage.isOnLoginPage());
    }

    @Test(priority = 5, groups = {"ui", "login"}, description = "TC_LOGIN_05: Verify Username & Password Placeholders")
    public void verifyLoginFieldPlaceholders() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertEquals(loginPage.getUsernamePlaceholder(), "Email or Mobile No");
        Assert.assertEquals(loginPage.getPasswordPlaceholder(), "Password");
    }

    @Test(priority = 6, groups = {"ui", "login"}, description = "TC_LOGIN_06: Verify Remember Me Checkbox Toggle")
    public void verifyRememberMeToggle() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertFalse(loginPage.isRememberMeSelected());
        loginPage.toggleRememberMe(true);
        Assert.assertTrue(loginPage.isRememberMeSelected());
    }

    @Test(priority = 7, groups = {"navigation", "login"}, description = "TC_LOGIN_07: Verify Forgot Password Link Navigation")
    public void verifyForgotPasswordNavigation() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickForgotPassword();
        Assert.assertTrue(driver.getCurrentUrl().contains("/Account/ForgotPassword"));
        Assert.assertTrue(driver.findElement(By.id("btnForgotPassword")).isDisplayed());
    }

    @Test(priority = 8, groups = {"navigation", "login"}, description = "TC_LOGIN_08: Verify Admin Login Link Navigation")
    public void verifyAdminLoginNavigation() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickAdminLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("/Account/AdminLogin"));
        Assert.assertEquals(driver.getTitle(), "Admin Login");
    }

    @Test(priority = 9, groups = {"smoke", "login"}, description = "TC_LOGIN_09: Verify Logout Flow Redirects Back to Login")
    public void verifyLogoutFlow() {
        HomePage home = loginToMagnus();
        Assert.assertTrue(home.isLoaded());
        LoginPage loginAfterLogout = home.logout();
        Assert.assertTrue(loginAfterLogout.isOnLoginPage(), "Should return to login page after logout");
    }

    @DataProvider(name = "excelLoginData")
    public Object[][] getExcelLoginData() {
        return ExcelUtil.getSheetData(ConfigReader.get("testdata.excel"), "LoginData");
    }

    @Test(priority = 10, dataProvider = "excelLoginData", groups = {"datadriven", "login"},
            description = "TC_LOGIN_10: Excel-Driven Login Parameterization via Apache POI & DataProvider")
    public void verifyExcelDrivenLogin(String username, String password, String expectedResult) {
        LoginPage loginPage = new LoginPage(driver);
        HomePage home = loginPage.loginAs(username, password);
        if ("SUCCESS".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue(home.isLoaded(), "Valid Excel row should login successfully");
        } else {
            Assert.assertTrue(loginPage.isOnLoginPage(), "Invalid Excel row should stay on login page");
        }
    }
}
