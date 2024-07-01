package GestEduAutomation;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ReadExcel.ReadExcel;

public class GestEduAutomation {
	
	private static WebDriver driver;
    private static final String DRIVER_PATH = "./src/test/resources/chromedriver/chromedriver.exe";
    private static final String PATH = "https://gestedu.works/";    
    private static Map<String, Row> testCasesToRun;
    private static List<String> datosTestCase = new ArrayList<>();
    
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
    
    public static List<String> getDatosTestCase(Row row) {
    	List<String> datos = new ArrayList<>();
    	for (int i = 2; i < row.getLastCellNum(); i++) {
    		Cell cell = row.getCell(i);
    		if (cell != null) {
                // Realizar alguna operación con cada celda (por ejemplo, obtener su valor)
                switch (cell.getCellType()) {
                    case STRING:
                        System.out.println("Texto: " + cell.getStringCellValue());
                        datos.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        System.out.println("Número: " + cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        System.out.println("Booleano: " + cell.getBooleanCellValue());
                        break;
                    case BLANK:
                        System.out.println("Celda vacía");
                        break;
                    default:
                        System.out.println("Tipo de celda desconocido");
                }
            }
    	}
    	return datos;
    }


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        initializeDriver();
        driver = getDriver();
        driver.get(PATH);
        testCasesToRun = ReadExcel.readExcelFile();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        quitDriver();
    }

	@Before
	public void setUp() throws Exception {
	}	
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
    public void runTestCases() {		
		for (Map.Entry<String, Row> testCase : testCasesToRun.entrySet())  {
			datosTestCase = getDatosTestCase(testCase.getValue());
            switch (testCase.getKey()) {
                case "login":
                    login();
                    break;
                case "logout":
                    logout();
                    break;
                case "agregarUsuario":
                    agregarUsuario();
                    break;
                default:
                    System.out.println("Test case no reconocido: " + testCase);
                    break;
            }
        }
    }

	
	public void login() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        try {
            // Esperar hasta que el enlace "Ingresar" sea visible y hacer clic en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Ingresar"))).click();
            System.out.println("Hizo clic en 'Ingresar'");

            // Esperar hasta que el campo de email sea visible y escribir en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(datosTestCase.get(0));
            System.out.println("Ingresó el email");

            // Esperar hasta que el campo de contraseña sea visible y escribir en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(datosTestCase.get(1));
            System.out.println("Ingresó la contraseña");

            // Esperar hasta que el botón "btn-primary" sea visible y hacer clic en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary"))).click();
            System.out.println("Hizo clic en el botón de inicio de sesión");

            // Esperar hasta que el enlace "Salir" sea visible
            boolean isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Salir')]"))) != null;
            System.out.println("Verificó la visibilidad del enlace 'Salir'");
            
            System.out.println("Login - OK");
            assertTrue("El enlace 'Salir' no es visible", isVisible);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }
	}
	
	
	public void logout() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		
		try {
			// Esperar hasta que el enlace "Salir" sea visible y hacer clic en él
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Salir')]"))).click();
	        System.out.println("Hizo clic en 'Salir'");

			// Secuencia de escape ANSI para establecer el color de fondo en verde
	        String greenBackground = "\u001B[42m";
	        // Secuencia de escape ANSI para reiniciar el formato de color
	        String reset = "\u001B[0m";

	        // Mensaje a imprimir con fondo verde
	        String message = greenBackground + "Logout - OK" + reset;

	        // Imprimir el mensaje en consola
	        System.out.println(message);
			assertTrue(true);
		} catch (Exception e) {
            e.printStackTrace();
            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }
	}
	
	
	public void agregarUsuario() {
		
	}
	
	
	public void altaCarrera() {
		
	}
	
	
	public void altaAsignatura() {
		
	}
	
	
	public void agregarPlanEstudio() {
		
	}
	
	
	public void altaCurso() {
		
	}
	
	
	public void altaExamen() {
		
	}
	
	
	public void registarEstudiante() {
		
	}
	
	
	public void inscripcionCarrera() {
		
	}
	
	
	public void aprobarInscCarrera() {
		
	}
	
	
	public void altaPreviatura() {
		
	}
	
	
	public void inscripcionCurso() {
		
	}

}
