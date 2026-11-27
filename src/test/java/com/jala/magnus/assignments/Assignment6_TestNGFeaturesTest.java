package com.jala.magnus.assignments;

import com.jala.magnus.base.BaseTest;
import com.jala.magnus.pages.HomePage;
import com.jala.magnus.pages.LoginPage;
import com.jala.magnus.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

/**
 * Assignment 6: TestNG Features (Steps 1 to 13)
 * Demonstrates: Annotation Lifecycle Order, Groups, Hard & Soft Assertions,
 * enabled=false / Ignore, dependsOnMethods, priority, invocationCount,
 * @Parameters, and @DataProvider on Magnus Application.
 */
public class Assignment6_TestNGFeaturesTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void beforeClassDemo() {
        log.info("[Annotation Order] @BeforeClass executed");
    }

    @AfterClass(alwaysRun = true)
    public void afterClassDemo() {
        log.info("[Annotation Order] @AfterClass executed");
    }

    // Step 7: Priority attribute & Step 4: Hard Assertions
    @Test(priority = 1, groups = {"assignments", "testng", "smoke"}, description = "Verify Login Page Title with Hard Assertion")
    public void testPriorityOneLoginPageTitle() {
        Assert.assertEquals(driver.getTitle(), "Login", "Login page title mismatch");
        Assert.assertTrue(driver.getCurrentUrl().contains("magnus.jalatechnologies.com"));
    }

    // Step 4: SoftAssert & Step 6: dependsOnMethods
    @Test(priority = 2, dependsOnMethods = {"testPriorityOneLoginPageTitle"}, groups = {"assignments", "testng", "regression"},
            description = "Verify Dashboard Elements using SoftAssert (dependsOnMethods)")
    public void testDependentDashboardWithSoftAssert() {
        HomePage home = loginToMagnus();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(driver.getTitle(), "Magnus", "Dashboard title check");
        softAssert.assertTrue(home.isLoaded(), "Logout link should be displayed");
        softAssert.assertTrue(driver.getCurrentUrl().contains("/Home/Index"), "URL should contain /Home/Index");
        softAssert.assertAll();
    }

    // Step 5: Disable / Ignore a test
    @Test(enabled = false, groups = {"assignments", "testng"}, description = "Disabled test case example")
    public void testDisabledExample() {
        Assert.fail("This disabled test should never execute");
    }

    // Step 8: invocationCount
    @Test(priority = 3, invocationCount = 2, groups = {"assignments", "testng"}, description = "Run Forgot Password navigation check multiple times via invocationCount")
    public void testInvocationCountForgotPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickForgotPassword();
        Assert.assertTrue(driver.getCurrentUrl().contains("ForgotPassword"));
    }

    // Step 9: Passing @Parameters from testng.xml
    @Parameters({"testUser", "testPass"})
    @Test(priority = 4, groups = {"assignments", "testng"}, description = "Verify parameterized login using @Parameters")
    public void testParameterizedLogin(@Optional("training@jalaacademy.com") String testUser,
                                       @Optional("jobprogram") String testPass) {
        LoginPage loginPage = new LoginPage(driver);
        HomePage home = loginPage.loginAs(testUser, testPass);
        Assert.assertTrue(home.isLoaded(), "Parameterized login failed for user: " + testUser);
    }

    // Step 10: Data-Driven testing using @DataProvider
    @DataProvider(name = "magnusLoginProvider")
    public Object[][] provideLoginData() {
        return new Object[][]{
                {ConfigReader.get("app.username"), ConfigReader.get("app.password"), true},
                {"invalid_user@jala.com", "wrong123", false}
        };
    }

    @Test(priority = 5, dataProvider = "magnusLoginProvider", groups = {"assignments", "testng", "datadriven"},
            description = "Data-driven login test using @DataProvider")
    public void testDataDrivenLogin(String user, String pass, boolean shouldSucceed) {
        LoginPage loginPage = new LoginPage(driver);
        HomePage home = loginPage.loginAs(user, pass);
        if (shouldSucceed) {
            Assert.assertTrue(home.isLoaded(), "Expected valid login to succeed");
        } else {
            Assert.assertTrue(loginPage.isOnLoginPage(), "Expected invalid login to stay on Login page");
        }
    }
}
