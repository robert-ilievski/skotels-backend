package skotels.test.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class AbstractPage {

    public WebDriver driver;
    public final WebDriverWait wait;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }
}
