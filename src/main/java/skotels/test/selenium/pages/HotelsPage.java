package skotels.test.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HotelsPage extends AbstractPage{
    public HotelsPage(WebDriver driver) {
        super(driver);
    }

    public void openHotels() {
        driver.get("https://skotels-refactored.netlify.app/hotels");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("search"))).isDisplayed();
    }
}
