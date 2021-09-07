package skotels.test.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends AbstractPage{
    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void openHome() {
        driver.get("https://skotels-refactored.netlify.app/home");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("home-content"))).isDisplayed();
    }
}
