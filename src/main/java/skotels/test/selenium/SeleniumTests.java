package skotels.test.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import skotels.test.selenium.pages.AboutPage;
import skotels.test.selenium.pages.HomePage;
import skotels.test.selenium.pages.HotelsPage;
import skotels.test.selenium.pages.LoginPage;

public class SeleniumTests {

    private WebDriver driver;

    public SeleniumTests() {
    }

    @BeforeEach
    public void setup(){
        this.driver = this.getDriver();
    }

    @AfterEach
    public void destroy() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    //Loading page tests
    @Test
    public void testLoadLoginPage() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLogin();
        Assertions.assertTrue(loginPage.isLoaded());
    }

    @Test
    public void testLoadHotelsPage() throws InterruptedException {
        HotelsPage hotelsPage = new HotelsPage(driver);
        hotelsPage.openHotels();
        Assertions.assertTrue(hotelsPage.isLoaded());
    }

    @Test
    public void testLoadAboutPage() throws InterruptedException {
        AboutPage aboutPage = new AboutPage(driver);
        aboutPage.openAbout();
        Assertions.assertTrue(aboutPage.isLoaded());
    }

    @Test
    public void testLoadHomePage() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        homePage.openHome();
        Assertions.assertTrue(homePage.isLoaded());
    }

    //Test successful and unsuccessful login
    @Test
    public void testSuccessfulLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLogin();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.doLogin("admin", "admin");
        Assertions.assertTrue(new HomePage(driver).isLoaded());
    }

    @Test
    public void testInvalidCredentialsLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLogin();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.doLogin("admin", "wrongPassword");
        Assertions.assertFalse(new HomePage(driver).isLoaded());
    }

    private WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        return new ChromeDriver();
    }
}
