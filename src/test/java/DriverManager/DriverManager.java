package DriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {
    private static WebDriver driver;
    private static final String DRIVER_PATH = "./src/test/resources/chromedriver/chromedriver.exe";
    private static final String PATH = "http://localhost:3000/";

    public static void initializeDriver() {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}