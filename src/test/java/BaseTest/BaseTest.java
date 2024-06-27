package BaseTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;

import DriverManager.DriverManager;

public class BaseTest {
	
	protected static WebDriver driver;
    protected static String driverPath;
    protected static String path;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DriverManager.initializeDriver();
        driver = DriverManager.getDriver();
        path = "https://gestedu.works/";
        driverPath = "./src/test/resources/chromedriver/chromedriver.exe";        
        driver.get(path);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DriverManager.quitDriver();
	}
	
}


