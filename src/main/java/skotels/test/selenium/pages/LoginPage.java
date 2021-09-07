package skotels.test.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends AbstractPage{

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void openLogin() {
        driver.get("https://skotels-refactored.netlify.app/login");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("login-container"))).isDisplayed();
    }

    public void doLogin(String email, String password) throws InterruptedException {
        driver.findElement(By.className("username-input")).clear();
        driver.findElement(By.className("username-input")).sendKeys(email);
        Thread.sleep(5000);
        driver.findElement(By.className("password-input")).sendKeys(password);
        Thread.sleep(5000);
        driver.findElement(By.className("log-in-button")).click();
        Thread.sleep(10000);
    }
}
