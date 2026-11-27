package com.jala.magnus.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.jala.magnus.driver.DriverFactory;
import com.jala.magnus.utils.ExtentManager;
import com.jala.magnus.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Framework Step 20: TestNG listener for Extent Reports and failure screenshots.
 */
public class ExtentTestListener implements ITestListener {
    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> tlTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getRealClass().getSimpleName() + " :: " + result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName, result.getMethod().getDescription());
        for (String group : result.getMethod().getGroups()) {
            test.assignCategory(group);
        }
        tlTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        tlTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = tlTest.get();
        test.log(Status.FAIL, result.getThrowable());
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            String shotPath = ScreenshotUtil.capture(driver, result.getMethod().getMethodName());
            if (!shotPath.isEmpty()) {
                test.addScreenCaptureFromPath(shotPath);
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        tlTest.get().log(Status.SKIP, "Test Skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
