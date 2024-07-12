package GestEduAutomation;
import static org.junit.Assert.assertTrue;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
import org.openqa.selenium.Alert;
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
                case "altaCurso":
                	altaCurso();
                    break;
                case "altaExamen":
                	altaExamen();
                    break;
                case "registrarEstudiante":
                	registrarEstudiante();
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
	
	
	@SuppressWarnings("deprecation")
	public void altaCurso() {
		String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
        try {
        	PrintTestCase(sMethodName);
        	
        	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Funcionario')]")));            
            System.out.println("Verificó la visibilidad del titulo 'Funcionario'");
            
            ClickBurgerMenu(webDriverWait);
            
            //BUSCA LA CARRERA
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Calendario"))).click();
            System.out.println("Clic en enlance 'Calendario'");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]")));  
            System.out.println("Espera visibilidad columna 'Nombre'");
                    
            //Se mueve el mouse hasta el boton Menu de la columna nombre, para acceder al filtro.
            WebElement hoverable = driver.findElement(By.xpath("//div[contains(text(), 'Nombre')]"));
            Actions action = new Actions(driver);
            action.moveToElement(hoverable).perform();       
            Thread.sleep(1000);
    		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button"))).click();  
            System.out.println("Click en menu columna 'Nombre'");            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Filter')]"))).click();  
            System.out.println("Click en filter columna 'Nombre'");
            
            System.out.println("Carrera " + datosTestCase.get(9));            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r17:\"]"))).sendKeys(datosTestCase.get(9));
            System.out.println("Ingresó el nombre de la carrera");            

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + datosTestCase.get(9) + "')]/following-sibling::div/following-sibling::div/following-sibling::div"))).click();  
            System.out.println("Selecciona carrara " + datosTestCase.get(9));         

            //BUSCA ASIGNATURA
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/section/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/div[3]")));  
            System.out.println("Espera visibilidad columna 'Nombre'");
                    
            //Se mueve el mouse hasta el boton Menu de la columna nombre, para acceder al filtro.
            WebElement hoverable2 = driver.findElement(By.xpath("/html/body/main/section/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/div[3]"));
           
            action = new Actions(driver);
            action.moveToElement(hoverable2).perform();
            System.out.println("Hover Nombre OK");            
                        
            driver.findElement(By.xpath("//div[contains(text(), 'Nombre')]/parent::*/parent::*/following-sibling::*/button")).click();
    		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]/parent::*/parent::*/following-sibling::*/button"))).click();  
            System.out.println("Click en menu columna 'Nombre'");            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Filter')]"))).click();  
            System.out.println("Click en filter columna 'Nombre'");
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Filter value']"))).sendKeys(datosTestCase.get(11));
            System.out.println("Ingresó el nombre de la asignatura");            
			
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + datosTestCase.get(11) + "')]/following-sibling::div/following-sibling::div/following-sibling::div/following-sibling::div"))).click();  
            System.out.println("Selecciona asignatura " + datosTestCase.get(9));        
                        
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Registrar Fecha de Curso')]"))).click();
            System.out.println("Click en boton 'Registrar Fecha de Curso'");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(),'Docente')]")));
            System.out.println("Espera visibilidad 'Docente'");
             
            
            Robot robot = new Robot(); 
            WebElement dateInputElement = driver.findElement(By.xpath("/html/body/main/section/div/div/label[1]"));
            
            robot.mouseMove(dateInputElement.getLocation().getX(), dateInputElement.getLocation().getY()+120);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            String expectedDate = datosTestCase.get(17);
            for (char c : expectedDate.toCharArray()) {
              robot.keyPress(Character.toUpperCase(c));
              robot.delay(50); // Adjust delay if needed
              robot.keyRelease(Character.toUpperCase(c));
            }

            // Press Tab or Enter
            robot.keyPress(KeyEvent.VK_TAB); // Or KeyEvent.VK_ENTER if needed
            robot.delay(100); // Adjust delay if needed
            robot.keyRelease(KeyEvent.VK_TAB); // Or KeyEvent.VK_ENTER if needed
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("fechaInicio")));
            System.out.println("Espera visibilidad 'fechaInicio'");
            
            WebElement dateInputElementFin = driver.findElement(By.name("fechaInicio"));
            
            // Click the input element
            robot.mouseMove(dateInputElementFin.getLocation().getX(), dateInputElementFin.getLocation().getY()+170);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            // Type the date
            expectedDate = datosTestCase.get(18);
            for (char c : expectedDate.toCharArray()) {
              robot.keyPress(Character.toUpperCase(c));
              robot.delay(50); // Adjust delay if needed
              robot.keyRelease(Character.toUpperCase(c));
            }

            // Press Tab or Enter
            robot.keyPress(KeyEvent.VK_TAB); // Or KeyEvent.VK_ENTER if needed
            robot.delay(100); // Adjust delay if needed
            robot.keyRelease(KeyEvent.VK_TAB); // Or KeyEvent.VK_ENTER if needed
            
            Thread.sleep(500);
            System.out.println("Ingresó el fecha de inicio: " + datosTestCase.get(17));
            Thread.sleep(500);
            
            javascriptExecutor.executeScript("document.querySelector(\"input[name='fechaFin']\").setAttribute('value', '" + datosTestCase.get(18) + "')");            
            Thread.sleep(500);            
            System.out.println("Ingresó el fecha de fin: " + datosTestCase.get(18));
            Thread.sleep(500);
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("demo-simple-select"))).click();
            System.out.println("Click 'demo-simple-select'");
            
            String docente = datosTestCase.get(23);          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'" + docente + "')]"))).click();
            System.out.println("Click 'Docente' " + docente);
            Thread.sleep(500);
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Registrar')]"))).click();
            System.out.println("Click 'Registrar'");            
           
            Alert alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert: " + alert.getText());
            
            assertTrue(alert.getText().contains("200"));            
            
        	PrintMessage(sMethodName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintError(sMethodName, e);        	
        }
	}
	
	
	public void altaExamen() {
		String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
        try {
        	PrintTestCase(sMethodName);
        	
        	webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Funcionario')]")));            
            System.out.println("Verificó la visibilidad del titulo 'Funcionario'");
            
            ClickBurgerMenu(webDriverWait);
            
            //BUSCA LA CARRERA
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Calendario"))).click();
            System.out.println("Clic en enlance 'Calendario'");
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]")));  
            System.out.println("Espera visibilidad columna 'Nombre'");
                    
            //Se mueve el mouse hasta el boton Menu de la columna nombre, para acceder al filtro.
            WebElement hoverable = driver.findElement(By.xpath("//div[contains(text(), 'Nombre')]"));
            Actions action = new Actions(driver);
            action.moveToElement(hoverable).perform();       
            Thread.sleep(1000);
    		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button"))).click();  
            System.out.println("Click en menu columna 'Nombre'");            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Filter')]"))).click();  
            System.out.println("Click en filter columna 'Nombre'");
            
            System.out.println("Carrera " + datosTestCase.get(9));            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r17:\"]"))).sendKeys(datosTestCase.get(9));
            System.out.println("Ingresó el nombre de la carrera");            

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + datosTestCase.get(9) + "')]/following-sibling::div/following-sibling::div/following-sibling::div"))).click();  
            System.out.println("Selecciona carrara " + datosTestCase.get(9));         

            //BUSCA ASIGNATURA
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/main/section/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/div[3]")));  
            System.out.println("Espera visibilidad columna 'Nombre'");
                    
            //Se mueve el mouse hasta el boton Menu de la columna nombre, para acceder al filtro.
            WebElement hoverable2 = driver.findElement(By.xpath("/html/body/main/section/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/div[3]"));
           
            action = new Actions(driver);
            action.moveToElement(hoverable2).perform();
            System.out.println("Hover Nombre OK");            
                        
            driver.findElement(By.xpath("//div[contains(text(), 'Nombre')]/parent::*/parent::*/following-sibling::*/button")).click();
    		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Nombre')]/parent::*/parent::*/following-sibling::*/button"))).click();  
            System.out.println("Click en menu columna 'Nombre'");            
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Filter')]"))).click();  
            System.out.println("Click en filter columna 'Nombre'");
          
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Filter value']"))).sendKeys(datosTestCase.get(11));
            System.out.println("Ingresó el nombre de la asignatura");            
			
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + datosTestCase.get(11) + "')]/following-sibling::div/following-sibling::div/following-sibling::div/following-sibling::div"))).click();  
            System.out.println("Selecciona asignatura " + datosTestCase.get(9));        
                        
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Registrar Fecha de Examen')]"))).click();
            System.out.println("Click en boton 'Registrar Fecha de Examen'");
            
            Thread.sleep(500);
             
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"demo-simple-select-label\"]/parent::*/parent::*")));
            Thread.sleep(500);
            WebElement sPeriodo = driver.findElement(By.xpath("//*[@id=\"demo-simple-select-label\"]/parent::*/parent::*"));
            sPeriodo.click();
            System.out.println("Click 'Periodo'");            
            Thread.sleep(1000);
            
            String periodo = datosTestCase.get(24); 
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + periodo + "')]")));
            WebElement inputPeriodo = driver.findElement(By.xpath("//*[contains(text(),'" + periodo +"')]"));
            inputPeriodo.click();
            System.out.println("Selecciona Periodo " + periodo);
            Thread.sleep(500);
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(),'Fecha y Hora')]/following-sibling::*/input")));
            WebElement inputFechaHora = driver.findElement(By.xpath("//label[contains(text(),'Fecha y Hora')]/following-sibling::*/input"));
            Thread.sleep(500);
            
            String expectedDate = datosTestCase.get(25);
            for (char c : expectedDate.toCharArray()) {
            	inputFechaHora.sendKeys(String.valueOf(c));
            }
            inputFechaHora.sendKeys(Keys.TAB);
            Thread.sleep(1000);
            
            String diasPrevios = datosTestCase.get(26);
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("diasPrevInsc")));
            WebElement inputDiasPrev = driver.findElement(By.name("diasPrevInsc"));
            inputDiasPrev.sendKeys(datosTestCase.get(26));
            System.out.println("Se ingresa dias previos inscripcion: " + diasPrevios);
            Thread.sleep(1000);       
            
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("demo-multiple-checkbox")));
            WebElement selectDocente = driver.findElement(By.id("demo-multiple-checkbox"));
            selectDocente.click();
            System.out.println("Click en select 'Docente'");
            Thread.sleep(1000);
            
            String docente = datosTestCase.get(23); 
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + docente + "')]")));
            WebElement selectOption = driver.findElement(By.xpath("//*[contains(text(),'" + docente +"')]"));
            selectOption.click();
            System.out.println("Selecciona docente: " + docente);
            Thread.sleep(1000);     

               
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Registrar')]")));
            WebElement btnRegistrar = driver.findElement(By.xpath("//button[contains(text(),'Registrar')]"));
            btnRegistrar.click();
            System.out.println("Click en boton 'Registrar'");
            
            boolean isVisible = FindByXpath("//div[contains(text(),'Examen registrado con exito')]") != null;
            System.out.println("Verificó la visibilidad boton 'Examen registrado con exito'");
            
            assertTrue("El enlace 'Examen registrado con exito' no es visible", isVisible);
                        
        	PrintMessage(sMethodName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintError(sMethodName, e);        	
        }
	}
	
	
	public void registrarEstudiante() {
		String sMethodName = new String (Thread.currentThread().getStackTrace()[1].getMethodName());
	    
        try {
        	PrintTestCase(sMethodName);
        	
        	String nombre = datosTestCase.get(2);
        	String apellido = datosTestCase.get(3);
        	String email = datosTestCase.get(0);
        	String ci = datosTestCase.get(4);
        	String fechaNac = datosTestCase.get(5);
        	String domicilio = datosTestCase.get(6);
        	String telefono = datosTestCase.get(7);
        	String password = datosTestCase.get(1);
        	
        	ClickBurgerMenu(webDriverWait);
        	
        	FindByLinktext("Registrarse").click();
        	
        	FindById("nombre").sendKeys(nombre);
        	
        	FindById("apellido").sendKeys(apellido);
        	
        	FindById("email").sendKeys(email);
        	
        	FindById("ci").sendKeys(ci);
        	
        	WebElement inputFechaNac = FindById("fechaNac");
        	inputFechaNac.sendKeys(fechaNac);        	
        	
        	FindById("domicilio").sendKeys(domicilio);
        	
        	FindById("telefono").sendKeys(telefono);
        	
        	FindById("password").sendKeys(password);
        	
        	FindById("confirmPassword").sendKeys(password);
        	
        	FindByXpath("//button[contains(text(),'Registrarse')]").click();
        	
        	boolean isVisible = FindByXpath("//h1[contains(text(),'Iniciar sesión')]") != null;
            
            assertTrue("El titulo 'Iniciar sesión' no es visible", isVisible);
        	
        	PrintMessage(sMethodName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintError(sMethodName, e);        	
        }
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
	
	private void CloseBurgerMenu(WebDriverWait wait) {		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div[3]/div/button"))).click();
        System.out.println("Clic en boton 'Cerrar menu'");
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
		//System.out.println(e.getMessage());
	}
	
	public WebElement FindByXpath(String xpath) {
		try {
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement element = driver.findElement(By.xpath(xpath));
			System.out.println("Se busca por xpath: " + xpath);        
			return element;
		} catch (Exception e) {
			PrintError("No se pudo encontrar elemento por xpath: " + xpath, e);
		}
		return null;
	}
	
	public WebElement FindById(String id) {
		try {
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
			WebElement element = driver.findElement(By.id(id));
			System.out.println("Se busca por id: " + id);  
			Thread.sleep(500);
			return element;
		} catch (Exception e) {
			PrintError("No se pudo encontrar elemento por ID: " + id, e);
		}
		return null;
	}
	
	public WebElement FindByName(String name) {
		try {
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
			WebElement element = driver.findElement(By.name(name));
			System.out.println("Se busca por Name: " + name);    
			Thread.sleep(500);
			return element;
		} catch (Exception e) {
			PrintError("No se pudo encontrar elemento por Name: " + name, e);
		}
		return null;
	}
	
	public WebElement FindByLinktext(String linkText) {
		try {
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
			WebElement element = driver.findElement(By.linkText(linkText));
			System.out.println("Se busca por linkText: " + linkText);       
			Thread.sleep(500);
			return element;
		} catch (Exception e) {
			PrintError("No se pudo encontrar elemento por linkText: " + linkText, e);
		}
		return null;
	}
	
}
