package com.jala.magnus.pages;

import com.jala.magnus.actions.PageActions;
import com.jala.magnus.utils.LocatorReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Framework Step 6: PageObjects class for Magnus Login, Forgot Password, and Admin Login pages.
 */
public class LoginPage {
    private final WebDriver driver;
    private final PageActions actions;

    @FindBy(id = "UserName")
    private WebElement txtUserName;

    @FindBy(id = "Password")
    private WebElement txtPassword;

    @FindBy(id = "RememberMe")
    private WebElement chkRememberMe;

    @FindBy(id = "btnLogin")
    private WebElement btnSignIn;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new PageActions(driver);
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String username) {
        actions.type(LocatorReader.getBy("login.username.id"), username);
    }

    public void enterPassword(String password) {
        actions.type(LocatorReader.getBy("login.password.id"), password);
    }

    public void toggleRememberMe(boolean check) {
        if (chkRememberMe.isSelected() != check) {
            actions.click(By.cssSelector("label[for='RememberMe']"));
        }
    }

    public void clickSignIn() {
        actions.click(LocatorReader.getBy("login.signin.id"));
    }

    public HomePage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignIn();
        return new HomePage(driver);
    }

    public String getUsernamePlaceholder() {
        return actions.getPlaceholder(LocatorReader.getBy("login.username.id"));
    }

    public String getPasswordPlaceholder() {
        return actions.getPlaceholder(LocatorReader.getBy("login.password.id"));
    }

    public String getUsernameValue() {
        return actions.getValue(LocatorReader.getBy("login.username.id"));
    }

    public void clearUsername() {
        actions.clearText(LocatorReader.getBy("login.username.id"));
    }

    public boolean isSignInEnabled() {
        return btnSignIn.isEnabled();
    }

    public boolean isRememberMeSelected() {
        return chkRememberMe.isSelected();
    }

    public void clickForgotPassword() {
        actions.click(LocatorReader.getBy("login.forgotPassword.linkText"));
    }

    public void clickAdminLogin() {
        actions.click(LocatorReader.getBy("login.adminLogin.linkText"));
    }

    public String getFooterLinkHref() {
        return driver.findElement(LocatorReader.getBy("login.footerLink.partialLinkText")).getAttribute("href");
    }

    public int getTotalLinksCount() {
        return driver.findElements(By.tagName("a")).size();
    }

    public boolean isOnLoginPage() {
        return actions.isDisplayed(LocatorReader.getBy("login.signin.id"));
    }
}
