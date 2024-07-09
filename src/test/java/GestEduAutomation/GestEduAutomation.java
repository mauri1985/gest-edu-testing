package GestEduAutomation;
import static org.junit.Assert.assertTrue;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import ReadExcel.ReadExcel;

public class GestEduAutomation {
	
	private static WebDriver driver;
    //private static final String DRIVER_PATH = "./src/test/resources/chromedriver/chromedriver.exe";
	private static final String DRIVER_PATH = "./src/test/resources/geckodriver/geckodriver.exe";
    private static final String PATH = "https://gestedu.works/";    
    private static Map<String, Row> testCasesToRun;
    private static List<String> datosTestCase = new ArrayList<>();
    private static JavascriptExecutor javascriptExecutor;
    private static WebDriverWait webDriverWait;
    
    public static void initializeDriver() {
        if (driver == null) {
            //System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
            //driver = new ChromeDriver();
            //driver.manage().window().maximize();
        	System.setProperty("webdriver.gecko.driver", DRIVER_PATH);

            FirefoxOptions options = new FirefoxOptions();
            //options.addArguments("--start-maximized"); // Maximiza la ventana del navegador

            driver = new FirefoxDriver(options);
            driver.manage().window().maximize();
        }   
    }
    
    public static void initializeJavascriptExecutor() {
    	javascriptExecutor = (JavascriptExecutor) driver;
    }
    
    public static void initializeWait() {
    	webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
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
                    	datos.add(cell.getStringCellValue());
                    	break;
                    case NUMERIC:
                        //System.out.println("Número: " + cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        //System.out.println("Booleano: " + cell.getBooleanCellValue());
                        break;
                    case BLANK:
                        //System.out.println("Celda vacía");
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
        initializeJavascriptExecutor();
        initializeWait();
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
                case "altaCarrera":
                	altaCarrera();
                    break;
                case "altaAsignatura":
                	altaAsignatura();
                    break;
                case "agregarPlanEstudio":
                	agregarPlanEstudio();
                    break;
                default:
                    System.out.println("Test case no reconocido: " + testCase);
                    break;
            }
        }
    }

	
	public void login() {
		
		//Se obtiene el nombre del metodo
	    String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
        try {
        	PrintTestCase(sMethodName);
        	
        	ClickBurgerMenu(webDriverWait);
        	
            // Esperar hasta que el enlace "Ingresar" sea visible y hacer clic en él
        	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Iniciar Sesión"))).click();
            System.out.println("Clic en enlance 'Iniciar sesion'");

            // Esperar hasta que el campo de email sea visible y escribir en él
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(datosTestCase.get(0));
            System.out.println("Ingresó el email");

            // Esperar hasta que el campo de contraseña sea visible y escribir en él
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(datosTestCase.get(1));
            System.out.println("Ingresó la contraseña");

            // Esperar hasta que el botón "btn-primary" sea visible y hacer clic en él
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary"))).click();
            System.out.println("Hizo clic en el botón de inicio de sesión");

            ClickBurgerMenu(webDriverWait);
            
            // Esperar hasta que el enlace "Salir" sea visible
            boolean isVisible = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Salir')]"))) != null;
            System.out.println("Verificó la visibilidad del enlace 'Salir'");
            
            CloseBurgerMenu(webDriverWait);
            
            assertTrue("El enlace 'Salir' no es visible", isVisible);
            
            PrintMessage(sMethodName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintError(sMethodName, e);        	
        }
	}
	

	private void CloseBurgerMenu(WebDriverWait wait) {		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[3]/div/button"))).click();
        System.out.println("Clic en boton 'Cerrar menu'");
	}

	public void logout() {
		
		
		//Se obtiene el nombre del metodo
	    String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
		try {
			PrintTestCase(sMethodName);
			
			ClickBurgerMenu(webDriverWait);
        	
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Salir')]"))).click();
	        System.out.println("Hizo clic en 'Salir'");
	        
	        assertTrue(true);			
	        PrintMessage(sMethodName);
		} catch (Exception e) {
            PrintError(sMethodName, e);            
        }
	}	
	
	public void agregarUsuario() {
		
		//Se obtiene el nombre del metodo
	    String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
        try {
        	PrintTestCase(sMethodName);
        	
        	ClickBurgerMenu(webDriverWait);
        	
            //Ir a usuarios
        	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Usuarios')]"))).click();            
            System.out.println("Verificó la visibilidad del enlace 'Usuarios'");
            
            //Espera a entrar a la pagina
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Usuarios')]"))).click();            
            System.out.println("Verificó la visibilidad del titulo 'Usuarios'");
            
    		//Click en agregar usuario
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Agregar usuario']"))).click();            
            System.out.println("Verificó la visibilidad del boton 'Agregar usuarios'");
    		
    		//Cargar datos
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre"))).sendKeys(datosTestCase.get(2));
            System.out.println("Ingresó el nombre");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("apellido"))).sendKeys(datosTestCase.get(3));
            System.out.println("Ingresó el apellido");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(datosTestCase.get(0));
            System.out.println("Ingresó el email");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ci"))).sendKeys(datosTestCase.get(4));
            System.out.println("Ingresó el ci");

            javascriptExecutor.executeScript("document.querySelector(\"input[id='fechaNac']\").setAttribute('value', '" + datosTestCase.get(5) + "')");
            System.out.println("Ingresó el fecha nacimiento");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("domicilio"))).sendKeys(datosTestCase.get(6));
            System.out.println("Ingresó el domicilio");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("telefono"))).sendKeys(datosTestCase.get(7));
            System.out.println("Ingresó el telefono");
            
            Select drpTipoUsuario = new Select(driver.findElement(By.name("tipoUsuario")));
            drpTipoUsuario.selectByVisibleText(datosTestCase.get(8));
            System.out.println("Ingresó el tipo de usuario");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(datosTestCase.get(1));
            System.out.println("Ingresó el contraseña");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword"))).sendKeys(datosTestCase.get(1));
            System.out.println("Ingresó el confirmar contraseña");            
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Agregar']"))).click();            
            System.out.println("Verificó la visibilidad del boton 'Agregar'");            
          
            //Esperar hasta que el enlace "Salir" sea visible
            boolean isVisible = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Agregar usuario']"))) != null;
            System.out.println("Verificó la visibilidad del enlace 'Agregar usuario'");

            assertTrue("El enlace 'Agregar usuario' no es visible", isVisible);
            
            
            PrintMessage(sMethodName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintError(sMethodName, e);        	
        }
		
	}

	
	@SuppressWarnings("deprecation")
	public void altaCarrera() {
	    String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
        try {
        	PrintTestCase(sMethodName);
        	
        	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Coordinador')]")));            
            System.out.println("Verificó la visibilidad del titulo 'Coordinador'");
            
            ClickBurgerMenu(webDriverWait);
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Carreras"))).click();
            System.out.println("Clic en enlance 'Carreras'");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'1–')]")));            
            System.out.println("Verificó la visibilidad de la 'Tabla'");
            
            WebElement btnAgregarCarreras = driver.findElement(By.xpath("//button[contains(text(),'Agregar Carrera')]"));
            btnAgregarCarreras.click();
            System.out.println("Verificó la visibilidad del boton 'Agregar carrera'");             
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id=\"nombre\"]"))).sendKeys(datosTestCase.get(9));            
            System.out.println("Verificó la visibilidad input Nombre");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@id='descripcion']"))).sendKeys(datosTestCase.get(10));             
            System.out.println("Verificó la visibilidad input Descripcion");
            
            WebElement btnAgregar = driver.findElement(By.xpath("//button[contains(text(),'Agregar')]"));
            btnAgregar.click();
            System.out.println("Se hace clic en 'Agregar'");
            
            boolean isVisible = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Agregar Carrera')]"))) != null;
            System.out.println("Verificó la visibilidad boton 'Agregar carreras'");
            
            assertTrue("El enlace 'Agregar carrera' no es visible", isVisible);
            
        	PrintMessage(sMethodName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintError(sMethodName, e);        	
        }
		
	}
	
	
	public void altaAsignatura() {
		String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
        try {
        	PrintTestCase(sMethodName);
        	
        	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Coordinador')]")));            
            System.out.println("Verificó la visibilidad del titulo 'Coordinador'");
            
            ClickBurgerMenu(webDriverWait);
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Carreras"))).click();
            System.out.println("Clic en enlance 'Carreras'");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]")));  
            System.out.println("Espera visibilidad columna 'Nombre'");
                    
            WebElement hoverable = driver.findElement(By.xpath("//div[contains(text(), 'Nombre')]"));
            new Actions(driver)
                    .moveToElement(hoverable)
                    .perform();
       
            
    		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button"))).click();  
            System.out.println("Click en menu columna 'Nombre'");            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Filter')]"))).click();  
            System.out.println("Click en filter columna 'Nombre'");            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r1e:\"]"))).sendKeys(datosTestCase.get(9));
            System.out.println("Ingresó el nombre de la carrera");
            

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + datosTestCase.get(9) + "')]/following-sibling::div/following-sibling::div/following-sibling::div"))).click();  
            System.out.println("Selecciona carrara " + datosTestCase.get(9));         

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Agregar asignatura')]"))).click();
            System.out.println("Verificó la visibilidad boton 'Agregar asignatura'");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre"))).sendKeys(datosTestCase.get(11));
            System.out.println("Ingresó el email");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("descripcion"))).sendKeys(datosTestCase.get(12));
            System.out.println("Ingresó el email");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("creditos"))).sendKeys(datosTestCase.get(15));
            System.out.println("Ingresó el email");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Agregar')]"))).click();
            System.out.println("Verificó la visibilidad boton 'Agregar'");            
            
            boolean isVisible = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Agregar asignatura')]"))) != null;
            System.out.println("Verificó la visibilidad boton 'Agregar asignatura'");
            
            assertTrue("El enlace 'Agregar asignatura' no es visible", isVisible);
            
        	PrintMessage(sMethodName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintError(sMethodName, e);        	
        }
		
	}
	
	
	public void agregarPlanEstudio() {
		String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
        try {
        	PrintTestCase(sMethodName);
        	
        	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Coordinador')]")));            
            System.out.println("Verificó la visibilidad del titulo 'Coordinador'");
            
            ClickBurgerMenu(webDriverWait);
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Carreras"))).click();
            System.out.println("Clic en enlance 'Carreras'");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]")));  
            System.out.println("Espera visibilidad columna 'Nombre'");
                    
            //Se mueve el mouse hasta el boton Menu de la columna nombre, para acceder al filtro.
            WebElement hoverable = driver.findElement(By.xpath("//div[contains(text(), 'Nombre')]"));
            new Actions(driver)
                    .moveToElement(hoverable)
                    .perform();       
            
    		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button"))).click();  
            System.out.println("Click en menu columna 'Nombre'");            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Filter')]"))).click();  
            System.out.println("Click en filter columna 'Nombre'");            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r1e:\"]"))).sendKeys(datosTestCase.get(9));
            System.out.println("Ingresó el nombre de la carrera");            

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + datosTestCase.get(9) + "')]/following-sibling::div/following-sibling::div/following-sibling::div"))).click();  
            System.out.println("Selecciona carrara " + datosTestCase.get(9));         

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Registrar Plan de estudio')]"))).click();
            System.out.println("Click en boton 'Registrar Plan de estudio'");
            
            Thread.sleep(1500);

            String[] planDeEstudio = datosTestCase.get(16).split(";");  
            for (String plan : planDeEstudio) {
            	
            	String[] planActual = plan.split(":");
            	String asignatura = planActual[0];
            	String semestre = planActual[1];
            	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + asignatura +"')]/following-sibling::div/following-sibling::div/following-sibling::div/following-sibling::div")));
            	WebElement btnEditar = driver.findElement(By.xpath("//div[contains(text(),'" + asignatura + "')]/following-sibling::div/following-sibling::div/following-sibling::div/following-sibling::div"));
            	
            	btnEditar.click();
            	System.out.println("Click en Editar");
            	
            	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + asignatura + "')]/following-sibling::div/following-sibling::div/following-sibling::div/div/input")));
            	WebElement inputSemestre = driver.findElement(By.xpath("//div[contains(text(),'" + asignatura + "')]/following-sibling::div/following-sibling::div/following-sibling::div/div/input"));
            	
            	Thread.sleep(500);
            	inputSemestre.clear();
            	Thread.sleep(500);
            	
            	inputSemestre.sendKeys(semestre);
            	System.out.println("Se ingresa semestre: " + semestre);
            	Thread.sleep(500);
            	
            	inputSemestre.sendKeys(Keys.ENTER);
            	System.out.println("Se presionta tecla Enter");
            	Thread.sleep(500);
            }                       
   
            driver.findElement(By.xpath("//button[contains(text(),'Agregar Plan de Estudio')]")).click();
            System.out.println("Click en Agregar Plan de Estudio");
            
            boolean isVisible = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Ver Plan de estudio')]"))) != null;
            System.out.println("Verificó la visibilidad boton 'Ver Plan de estudio'");
            
            assertTrue("El enlace 'Ver Plan de estudio' no es visible", isVisible);
            
        	PrintMessage(sMethodName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintError(sMethodName, e);        	
        }
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
	
	private void ClickBurgerMenu(WebDriverWait wait) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@aria-label, 'open drawer')]"))).click();
    	System.out.println("Clic en boton 'Burger Menu'");
	}
	
	private void PrintTestCase(String sMethodName) {
		// Secuencia de escape ANSI para texto en negrita
		String bold = "\u001B[1m";
		// Secuencia de escape ANSI para reiniciar el formato de color
		String reset = "\u001B[0m";
		System.out.println(bold + "TESTCASE: " + sMethodName.toUpperCase() + reset);
	}

	private void PrintMessage(String sMethodName) {
		// Secuencia de escape ANSI para establecer el color de fondo en verde
        String greenBackground = "\u001B[42m";
        // Secuencia de escape ANSI para reiniciar el formato de color
        String reset = "\u001B[0m";
        // Mensaje a imprimir con fondo verde
        String message = greenBackground +  sMethodName.toUpperCase() + " - OK" + reset;
        // Imprimir el mensaje en consola
        System.out.println(message);
	}

	public void PrintError(String sMethodName, Exception e) {
		// Secuencia de escape ANSI para establecer el color de fondo en rojo
		String redBackground = "\u001B[41m";
		// Secuencia de escape ANSI para reiniciar el formato de color
		String reset = "\u001B[0m";
		// Mensaje a imprimir con fondo rojo
		String message = redBackground + sMethodName.toUpperCase() + " - ERROR" + reset;
		// Imprimir el mensaje en consola
		System.out.println(message);
		// Imprimir mensaje excepcion
		System.out.println(e.getMessage());
	}
}
