package skotels.test.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AboutPage extends AbstractPage{
    public AboutPage(WebDriver driver) {
        super(driver);
    }

    public void openAbout() {
        driver.get("https://skotels-refactored.netlify.app/about");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title"))).isDisplayed();
    }
}
