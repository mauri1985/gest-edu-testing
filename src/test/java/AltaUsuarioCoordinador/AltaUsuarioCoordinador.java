package AltaUsuarioCoordinador;

import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import BaseTest.BaseTest;

public class AltaUsuarioCoordinador extends BaseTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		//Eliminar usuario
		
	}

	@Test
	public void test() {
		//TODO: Login con usuario Admin
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        try {
            // Esperar hasta que el enlace "Ingresar" sea visible y hacer clic en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Ingresar"))).click();
            System.out.println("Hizo clic en 'Ingresar'");

            // Esperar hasta que el campo de email sea visible y escribir en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("PedroPeAdmin@mail.com");
            System.out.println("Ingresó el email");

            // Esperar hasta que el campo de contraseña sea visible y escribir en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys("1234");
            System.out.println("Ingresó la contraseña");

            // Esperar hasta que el botón "btn-primary" sea visible y hacer clic en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary"))).click();
            System.out.println("Hizo clic en el botón de inicio de sesión");

            //Ir a usuarios
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Usuarios')]"))).click();            
            System.out.println("Verificó la visibilidad del enlace 'Usuarios'");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Usuarios')]"))).click();            
            System.out.println("Verificó la visibilidad del titulo 'Usuarios'");
            
    		//Click en agregar usuario
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Agregar usuario']"))).click();            
            System.out.println("Verificó la visibilidad del boton 'Agregar usuarios'");
    		
    		//Cargar datos
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre"))).sendKeys("John");
            System.out.println("Ingresó el nombre");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("apellido"))).sendKeys("Doe");
            System.out.println("Ingresó el apellido");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("johndoe1@gmail.com");
            System.out.println("Ingresó el email");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ci"))).sendKeys("11111113");
            System.out.println("Ingresó el ci");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fechaNac"))).sendKeys("01/01/2000");
            System.out.println("Ingresó el fecha nacimiento");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("domicilio"))).sendKeys("Calle Falsa 1234");
            System.out.println("Ingresó el domicilio");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("telefono"))).sendKeys("095111222");
            System.out.println("Ingresó el telefono");
            
            Select drpTipoUsuario = new Select(driver.findElement(By.name("tipoUsuario")));
            drpTipoUsuario.selectByVisibleText("FUNCIONARIO");
            System.out.println("Ingresó el tipo de usuario");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys("1234");
            System.out.println("Ingresó el contraseña");
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword"))).sendKeys("1234");
            System.out.println("Ingresó el confirmar contraseña");            
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Agregar']"))).click();            
            System.out.println("Verificó la visibilidad del boton 'Agregar'");            
          
            //Esperar hasta que el enlace "Salir" sea visible
            boolean isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Agregar usuario']"))) != null;
            System.out.println("Verificó la visibilidad del enlace 'Agregar usuario'");
            
            assertTrue("El enlace 'Agregar usuario' no es visible", isVisible);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }	
	
		
	}

}
