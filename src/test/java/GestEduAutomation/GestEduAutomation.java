package GestEduAutomation;
import static org.junit.Assert.assertTrue;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
import org.openqa.selenium.NoSuchElementException;
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
    private static List<Row> testCasesToRun;
    private static List<String> datosTestCase = new ArrayList<>();
    private static JavascriptExecutor javascriptExecutor;
    private static WebDriverWait webDriverWait;
    private static String testCaseId;
    private static String testCaseName;
    private static int waitSeconds = 5;
    
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
    	webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
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
    
    public static List<String> GetDatosTestCase(Row row) {
    	List<String> datos = new ArrayList<>();
    	for (int i = 2; i < row.getLastCellNum(); i++) {
    		Cell cell = row.getCell(i);
    		if (cell != null) {

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

		for (Row testCase : testCasesToRun)  {			
			datosTestCase = GetDatosTestCase(testCase);
			testCaseId = testCase.getCell(0).getStringCellValue();
			testCaseName = testCase.getCell(1).getStringCellValue();
            switch (testCaseName) {
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
                case "inscripcionCarrera":
                	inscripcionCarrera();
                    break;
                case "aprobarInscCarrera":
                	aprobarInscCarrera();
                    break;
                case "altaPreviatura":
                	altaPreviatura();
                    break;
                case "inscripcionCurso":
                	inscripcionCurso();
                    break;
                default:
                	if(testCaseName != "")
                	PrintError("Test case no reconocido: " + testCaseName, null);
                    break;
            }
        }
    }


	public void login() {		    
        try {
        	
        	String rolUsuario = datosTestCase.get(27);
        	
        	PrintTestCase(testCaseName);
        	
        	ClickBurgerMenu(webDriverWait);
        	
        	FindByLinktext("Iniciar Sesión").click();

            FindById("email").sendKeys(datosTestCase.get(0));
            
            FindById("password").sendKeys(datosTestCase.get(1));

            FindByXpath("//button[contains(text(),'Ingresar')]").click();            
            
            WaitVisibility(By.xpath("//h1[contains(text(), '" + rolUsuario.trim() + "')]"));
            
            PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);     
        }
	}
	

	public void logout() {	    
		try {
			PrintTestCase(testCaseName);
			
			ClickBurgerMenu(webDriverWait);
        	
			FindByXpath("//span[contains(text(),'Salir')]").click();
			
			//WaitVisibility(By.xpath("//*[contains(text(),'Administrador de gestión educativa')]"));
			Thread.sleep(500);
			
			CloseBurgerMenu(webDriverWait);
	        			
	        PrintSuccesMessage(testCaseName);
		} catch (Exception e) {
            PrintFailMessage(testCaseId, testCaseName, e);                 
        }
	}	
	

	public void agregarUsuario() {	    
        try {
        	PrintTestCase(testCaseName);
        	
        	ClickBurgerMenu(webDriverWait);
        	
            FindByLinktext("Usuarios").click();
            
            FindByXpath("//h1[contains(text(),'Usuarios')]").click();
            
    		FindByXpath("//button[text()='Agregar usuario']").click();
    		
    		FindById("nombre").sendKeys(datosTestCase.get(2));
    		
    		FindById("apellido").sendKeys(datosTestCase.get(3));
    		
    		FindById("email").sendKeys(datosTestCase.get(0));
    		
    		FindById("ci").sendKeys(datosTestCase.get(4));
    		
    		FindById("fechaNac").sendKeys(datosTestCase.get(5));
    		
    		FindById("domicilio").sendKeys(datosTestCase.get(6));
    		
    		FindById("telefono").sendKeys(datosTestCase.get(7));
    		
            Select drpTipoUsuario = new Select(FindByName("tipoUsuario"));
            drpTipoUsuario.selectByVisibleText(datosTestCase.get(8));
            
            FindById("password").sendKeys(datosTestCase.get(1));
            
            FindById("confirmPassword").sendKeys(datosTestCase.get(1));            
    
            FindByXpath("//button[text()='Agregar']").click();
            
            WaitVisibility(By.xpath("//button[text()='Agregar usuario']"));
            
            PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);        	
        }
		
	}

	
	public void altaCarrera() {	    
	    
        try {
        	PrintTestCase(testCaseName);
        	
            ClickBurgerMenu(webDriverWait);
            
            FindByLinktext("Carreras").click();            
            
            FindByXpath("//button[contains(text(),'Agregar Carrera')]").click();          
          
            FindByXpath("//input[@id='nombre']").sendKeys(datosTestCase.get(9));
            
            FindByXpath("//textarea[@id='descripcion']").sendKeys(datosTestCase.get(10));             
            
            FindByXpath("//button[contains(text(),'Agregar')]").click();
            
            WaitVisibility(By.xpath("//button[contains(text(),'Agregar Carrera')]"));
            
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
		
	}
	
	
	public void altaAsignatura() {
        try {
        	PrintTestCase(testCaseName);
            
            ClickBurgerMenu(webDriverWait);
            
            FindByLinktext("Carreras").click();
                    
            WebElement hoverable = FindByXpath("//div[contains(text(), 'Nombre')]");
            new Actions(driver)
                    .moveToElement(hoverable)
                    .perform();       
            
    		FindByXpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button").click();  
          
            FindByXpath("//span[contains(text(),'Filtrar')]").click();          
          
            FindByXpath("//input[@placeholder='Filter value']").sendKeys(datosTestCase.get(9));            

            FindByXpath("//div[contains(text(),'" + datosTestCase.get(9) + "')]/following-sibling::div/following-sibling::div/following-sibling::div").click();

            FindByXpath("//button[contains(text(),'Agregar asignatura')]").click();
            
            FindById("nombre").sendKeys(datosTestCase.get(11));
            
            FindById("descripcion").sendKeys(datosTestCase.get(12));
            
            FindById("creditos").sendKeys(datosTestCase.get(15));
            
            FindByXpath("//button[contains(text(),'Agregar')]").click();            
            
            WaitVisibility(By.xpath("//button[contains(text(), 'Agregar asignatura')]"));
            
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
		
	}
	
	
	public void agregarPlanEstudio() {
		
	    
        try {
        	
        	String nombreCarrera = datosTestCase.get(9);
        	
        	PrintTestCase(testCaseName);
        	
        	ClickBurgerMenu(webDriverWait);
            
            FindByLinktext("Carreras").click();

            WebElement hoverable = FindByXpath("//div[contains(text(), 'Nombre')]");
            new Actions(driver)
                    .moveToElement(hoverable)
                    .perform();       
            
    		FindByXpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button").click();  
          
            FindByXpath("//span[contains(text(),'Filtrar')]").click();            
          
            FindByXpath("//input[@placeholder='Filter value']").sendKeys(nombreCarrera);
            
            FindByXpath("//div[contains(text(),'" + nombreCarrera + "')]/following-sibling::div/following-sibling::div/following-sibling::div").click();
            
            Thread.sleep(1000);
            FindByXpath("//button[contains(text(),'Registrar Plan de estudio')]").click();
            Thread.sleep(1000);
            
            String[] planDeEstudio = datosTestCase.get(16).split(";");  
            for (String plan : planDeEstudio) {
            	
            	String[] planActual = plan.split(":");
            	String asignatura = planActual[0];
            	String semestre = planActual[1];
            	
            	//FindByXpath("//div[contains(text(),'" + asignatura +"')]/following-sibling::div/following-sibling::div/following-sibling::div/following-sibling::div")));
            	WebElement btnEditar = FindByXpath("//div[contains(text(),'" + asignatura + "')]/following-sibling::div/following-sibling::div/following-sibling::div/following-sibling::div");            	
            	btnEditar.click();
            	
            	//FindByXpath("//div[contains(text(),'" + asignatura + "')]/following-sibling::div/following-sibling::div/following-sibling::div/div/input")));
            	WebElement inputSemestre = FindByXpath("//div[contains(text(),'" + asignatura + "')]/following-sibling::div/following-sibling::div/following-sibling::div/div/input");            	
            	inputSemestre.clear();
            	System.out.println("Se limpia semestre");
            	inputSemestre.sendKeys(semestre);
            	System.out.println("Se ingresa semestre: " + semestre);            	
            	inputSemestre.sendKeys(Keys.ENTER);
            	System.out.println("Se presiona ENTER");            	
            }                       
   
            FindByXpath("//button[contains(text(),'Agregar Plan de Estudio')]").click();
            
            WaitVisibility(By.xpath("//button[contains(text(), 'Ver Plan de estudio')]"));           
            
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
	}
	
	
	@SuppressWarnings("deprecation")
	public void altaCurso() {		
	    
        try {
        	String docente = datosTestCase.get(23); 
        	String fechaInicio = datosTestCase.get(17);
        	String fechaFin = datosTestCase.get(18);
        	String nombreCarrera = datosTestCase.get(9);
        	String nombreAsignatura = datosTestCase.get(11);
        	
        	PrintTestCase(testCaseName);

            ClickBurgerMenu(webDriverWait);
            
            FindByLinktext("Calendario").click();
            
            WebElement hoverable = FindByXpath("//div[contains(text(), 'Nombre')]");
            Actions action = new Actions(driver);
            action.moveToElement(hoverable).perform();       
            
    		FindByXpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button").click();  
          
    		FindByXpath("//span[contains(text(),'Filtrar')]").click();  
            
            FindByXpath("//input[@placeholder='Filter value']").sendKeys(nombreCarrera);

            FindByXpath("//div[contains(text(),'" + nombreCarrera + "')]/following-sibling::div/following-sibling::div/following-sibling::div").click();  


            FindByXpath("/html/body/main/section/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/div[3]");  

            WebElement hoverable2 = FindByXpath("/html/body/main/section/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/div[3]");
           
            action = new Actions(driver);
            action.moveToElement(hoverable2).perform();
                        
            FindByXpath("//div[contains(text(), 'Nombre')]/parent::*/parent::*/following-sibling::*/button").click();  
          
            FindByXpath("//span[contains(text(),'Filtrar')]").click();  
          
            FindByXpath("//input[@placeholder='Filter value']").sendKeys(nombreAsignatura);
            
			
            FindByXpath("//div[contains(text(),'" + nombreAsignatura + "')]/following-sibling::div/following-sibling::div/following-sibling::div/following-sibling::div").click();  
                        
            FindByXpath("//button[contains(text(),'Registrar Fecha de Curso')]").click();
            
            FindByXpath("//label[contains(text(),'Docente')]");           
            
            FindByXpath("//label[contains(text(),'Fecha de Inicio')]/following-sibling::div/input").sendKeys(fechaInicio);
            
            FindByXpath("//label[contains(text(),'Fecha de Fin')]/following-sibling::div/input").sendKeys(fechaFin);
            
            FindById("demo-simple-select").click();
            
            FindByXpath("//span[contains(text(),'" + docente + "')]").click();
            
            FindByXpath("//button[contains(text(),'Registrar')]").click();
            
            Alert alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert: " + alert.getText());
            
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
	}
	
	
	public void altaExamen() {
		
	    
        try {
        	String diasPrevios = datosTestCase.get(26);
        	String periodo = datosTestCase.get(24);
        	String nombreCarrera = datosTestCase.get(9); 
        	String docente = datosTestCase.get(23); 
        	
        	PrintTestCase(testCaseName);
        	
        	FindByXpath("//h1[contains(text(),'Funcionario')]");            
            System.out.println("Verificó la visibilidad del titulo 'Funcionario'");
            
            ClickBurgerMenu(webDriverWait);
            
            //BUSCA LA CARRERA
            FindByLinktext("Calendario").click();
            System.out.println("Clic en enlance 'Calendario'");
            
            FindByXpath("//div[contains(text(), 'Nombre')]");  
            System.out.println("Espera visibilidad columna 'Nombre'");
                    
            //Se mueve el mouse hasta el boton Menu de la columna nombre, para acceder al filtro.
            WebElement hoverable = FindByXpath("//div[contains(text(), 'Nombre')]");
            Actions action = new Actions(driver);
            action.moveToElement(hoverable).perform();       
            
    		FindByXpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button").click();  
          
            FindByXpath("//span[contains(text(),'Filtrar')]").click();                        
          
            FindByXpath("//*[@id=\":r17:\"]").sendKeys(nombreCarrera);

            FindByXpath("//div[contains(text(),'" + nombreCarrera + "')]/following-sibling::div/following-sibling::div/following-sibling::div").click();  

            //BUSCA ASIGNATURA
            FindByXpath("/html/body/main/section/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/div[3]");  
                    
            //Se mueve el mouse hasta el boton Menu de la columna nombre, para acceder al filtro.
            WebElement hoverable2 = FindByXpath("/html/body/main/section/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/div[3]");
           
            action = new Actions(driver);
            action.moveToElement(hoverable2).perform();      
                        
            FindByXpath("//div[contains(text(), 'Nombre')]/parent::*/parent::*/following-sibling::*/button").click();
          
            FindByXpath("//span[contains(text(),'Filtrar')]").click();  
          
            FindByXpath("//input[@placeholder='Filter value']").sendKeys(datosTestCase.get(11));
			
            FindByXpath("//div[contains(text(),'" + datosTestCase.get(11) + "')]/following-sibling::div/following-sibling::div/following-sibling::div/following-sibling::div").click();  
                        
            FindByXpath("//button[contains(text(),'Registrar Fecha de Examen')]").click();
            
            Thread.sleep(1000);
            FindByXpath("//*[contains(@id,'demo-simple-select-label')]/parent::*").click();
            Thread.sleep(1000);
            
            FindByXpath("//*[contains(text(),'" + periodo +"')]").click();
            
            WebElement inputFechaHora = FindByXpath("//label[contains(text(),'Fecha y Hora')]/following-sibling::*/input");
            
            String expectedDate = datosTestCase.get(25);
            for (char c : expectedDate.toCharArray()) {
            	inputFechaHora.sendKeys(String.valueOf(c));
            }
            
            inputFechaHora.sendKeys(Keys.TAB);
            
            FindByName("diasPrevInsc").sendKeys(diasPrevios);
            
            WebElement selectDocente = FindById("demo-multiple-checkbox");
            selectDocente.click();            
            Thread.sleep(1000);
            
            FindByXpath("//*[contains(text(),'" + docente + "')]").click();
            
            selectDocente.sendKeys(Keys.ESCAPE);
            Thread.sleep(1000);
            
            FindByXpath("//button[contains(text(),'Registrar')]").click();            
            Thread.sleep(1000);
            
            WaitVisibility(By.xpath("//div[contains(text(),'Examen registrado con exito')]"));
            
            PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
	}
	
	
	public void registrarEstudiante() {
		
	    
        try {
        	PrintTestCase(testCaseName);
        	
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
        	
        	WaitVisibility(By.xpath("//h1[contains(text(),'Iniciar sesión')]"));
            
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
	}
	
	
	public void inscripcionCarrera() {
		
	    
        try {
        	PrintTestCase(testCaseName);
        	
        	String carrera = datosTestCase.get(9);
        	
        	ClickBurgerMenu(webDriverWait);
        	
        	FindByLinktext("Inscripciones").click();
        	
        	FindByXpath("//button[contains(text(),'Otras carreras')]").click();
        	
        	FindByXpath("//div[contains(text(),'" + carrera + "')]/following-sibling::*/following-sibling::*/following-sibling::*/div/button").click();
        	
        	FindByXpath("//button[contains(text(),'Si')]").click();
        	
        	WaitVisibility(By.xpath("//div[contains(text(),'Su inscripcion ha sido solicidada correctamente')]"));
            
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
	}
	
	
	public void aprobarInscCarrera() {
		
	    
        try {
        	PrintTestCase(testCaseName);
        	
        	String ci = datosTestCase.get(4);
        	String carrera = datosTestCase.get(9);
        	
        	ClickBurgerMenu(webDriverWait);
        	
        	FindByLinktext("Inscripciones").click();
        	
            WebElement hoverable = FindByXpath("//div[contains(text(), 'Cedula')]");
            new Actions(driver)
                    .moveToElement(hoverable)
                    .perform();
                   
    		FindByXpath("//div[contains(text(), 'Cedula')]/parent::div/parent::div/following-sibling::div/button").click();       
          
    		FindByXpath("//span[contains(text(),'Filtrar')]").click();
    		
    		FindByXpath("//label[contains(text(),'Value')]/following-sibling::div/input").sendKeys(ci);
    		
    		FindByXpath("//*[@title='" + carrera + "']/following-sibling::div/following-sibling::div/div/button[1]").click();
    		
    		WaitVisibility(By.xpath("//*[@title='" + carrera + "']/following-sibling::div/following-sibling::div/div/button[1]"));
			
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
	}
	
	
	public void altaPreviatura() {
		try {
        	PrintTestCase(testCaseName);
        	
        	String nombreCarrera = datosTestCase.get(9);
        	String nombreAsignatura = datosTestCase.get(11);
        	String nombrePrevia = datosTestCase.get(13);
        	
        	ClickBurgerMenu(webDriverWait);
        	
        	FindByLinktext("Carreras").click();
        	
        	WebElement hoverable = FindByXpath("//div[contains(text(), 'Nombre')]");
            new Actions(driver)
                    .moveToElement(hoverable)
                    .perform(); 
        	
        	FindByXpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button").click();  
            
            FindByXpath("//span[contains(text(),'Filtrar')]").click();          
          
            FindByXpath("//input[@placeholder='Filter value']").sendKeys(nombreCarrera);            

            FindByXpath("//div[contains(text(),'" + nombreCarrera + "')]/following-sibling::div/following-sibling::div/following-sibling::div").click();
            
            WebElement hoverable2 = FindByXpath("//div[contains(text(), 'Nombre')]");
            
            new Actions(driver)
                    .moveToElement(hoverable2)
                    .perform(); 
    		
            FindByXpath("//div[contains(text(), 'Nombre')]/parent::div/parent::div/following-sibling::div/button").click();  
            
            FindByXpath("//span[contains(text(),'Filtrar')]").click();          
          
            FindByXpath("//input[@placeholder='Filter value']").sendKeys(nombreAsignatura);            

            FindByXpath("//div[contains(text(),'" + nombreAsignatura + "')]/following-sibling::div/following-sibling::div/following-sibling::div").click();         
            
            FindByXpath("//button[contains(text(),'Agregar previa')]").click();
            
            FindByXpath("//*[contains(text(),'" + nombrePrevia + "')]/following-sibling::*/following-sibling::*/div").click();
            
            
            //TODO: Validar previaturas
    		WaitVisibility(By.xpath(""));
			
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
	}
	
	
	public void inscripcionCurso() {
		try {
        	PrintTestCase(testCaseName);
        	
            //TODO: implementar
        	
    		WaitVisibility(By.xpath(""));
			
        	PrintSuccesMessage(testCaseName);
            
        } catch (Exception e) {
            //e.printStackTrace();
        	PrintFailMessage(testCaseId, testCaseName, e);             	
        }
	}
	
	private void ClickBurgerMenu(WebDriverWait wait) {
		FindByXpath("//button[contains(@aria-label, 'open drawer')]").click();
    	System.out.println("Clic en boton 'Burger Menu'");
	}
	
	private void CloseBurgerMenu(WebDriverWait wait) {		
		FindByXpath("/html/body/div/div[3]/div/button").click();
        System.out.println("Clic en boton 'Cerrar menu'");
	}
	
	private void PrintTestCase(String testCaseName) {
		// Secuencia de escape ANSI para texto en negrita
		String bold = "\u001B[1m";
		// Secuencia de escape ANSI para reiniciar el formato de color
		String reset = "\u001B[0m";
		System.out.println(bold + "############# TESTCASE: " + testCaseName.toUpperCase() +" #############" + reset);
	}
	
	private void PrintMessage(String message) {
		System.out.println(message);
	}

	private void PrintSuccesMessage(String testCaseName) {
		// Secuencia de escape ANSI para establecer el color de fondo en verde
        String greenBackground = "\u001B[42m";
        // Secuencia de escape ANSI para reiniciar el formato de color
        String reset = "\u001B[0m";
        // Mensaje a imprimir con fondo verde
        String message = greenBackground +  testCaseName.toUpperCase() + " - OK" + reset;
        // Imprimir el mensaje en consola
        System.out.println(message);
	}
	
	public void PrintFailMessage(String id, String testCase, Exception e) {
		// Secuencia de escape ANSI para establecer el color de fondo en rojo
		String redBackground = "\u001B[41m";
		// Secuencia de escape ANSI para reiniciar el formato de color
		String reset = "\u001B[0m";
		// Mensaje a imprimir con fondo rojo
		String message = redBackground + "TESTCASE FAIL - ID: " + id.toUpperCase() + " - " + testCase.toUpperCase() + reset;
		// Imprimir el mensaje en consola
		System.out.println(message);
	}

	public void PrintError(String message, Exception e) {
		// Código ANSI para el color rojo
	    final String ANSI_RED = "\u001B[31m";
	    // Código ANSI para restablecer el color
	    final String ANSI_RESET = "\u001B[0m";
	    // Imprimir el mensaje en rojo
	    System.out.println(ANSI_RED + message + ANSI_RESET);
	    // Imprimir excepcion en rojo
	    //System.out.println(e.getMessage() + ANSI_RESET);
	}
	
	public WebElement FindByXpath(String xpath) {
		try {
			System.out.println("Se busca por xpath: " + xpath);        
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			WebElement element = driver.findElement(By.xpath(xpath));
			Thread.sleep(500);
			return element;
		} catch (Exception e) {
			PrintError("No se pudo encontrar elemento por xpath: " + xpath, e);
			return null;
		}		
	}
	
	public WebElement FindById(String id) {
		try {
			System.out.println("Se busca por id: " + id);  
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
			WebElement element = driver.findElement(By.id(id));
			Thread.sleep(500);
			return element;
		} catch (Exception e) {
			PrintError("No se pudo encontrar elemento por ID: " + id, e);
			return null;
		}		
	}
	
	public WebElement FindByName(String name) {
		try {
			System.out.println("Se busca por Name: " + name);   
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
			WebElement element = driver.findElement(By.name(name));
			Thread.sleep(500);
			return element;
		} catch (Exception e) {
			PrintError("No se pudo encontrar elemento por Name: " + name, e);
			return null;
		}		
	}
	
	public WebElement FindByLinktext(String linkText) {
		try {
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated((By.linkText(linkText))));
			WebElement element = driver.findElement(By.linkText(linkText));
			System.out.println("Se busca por linkText: " + linkText);
			Thread.sleep(500);
			return element;
		} catch (Exception e) {
			PrintError("No se pudo encontrar elemento por linkText: " + linkText, e);
			return null;
		}		
	}
	
	public boolean WaitVisibility(By by) {
		return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(by)) != null;
	}
	
}
