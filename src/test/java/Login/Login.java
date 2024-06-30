package Login;

import static org.junit.Assert.*;

import java.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import BaseTest.BaseTest;


public class Login extends BaseTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		//eliminar el usuario
	}

	@Test
	public void login() {
		
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        try {
            // Esperar hasta que el enlace "Ingresar" sea visible y hacer clic en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Ingresar"))).click();
            System.out.println("Hizo clic en 'Ingresar'");

            // Esperar hasta que el campo de email sea visible y escribir en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("MauriMu@mail.com");
            System.out.println("Ingresó el email");

            // Esperar hasta que el campo de contraseña sea visible y escribir en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys("1234");
            System.out.println("Ingresó la contraseña");

            // Esperar hasta que el botón "btn-primary" sea visible y hacer clic en él
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary"))).click();
            System.out.println("Hizo clic en el botón de inicio de sesión");

            // Esperar hasta que el enlace "Salir" sea visible
            boolean isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Salir')]"))) != null;
            System.out.println("Verificó la visibilidad del enlace 'Salir'");

            assertTrue("El enlace 'Salir' no es visible", isVisible);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ocurrió una excepción durante la prueba: " + e.getMessage());
        }
	}
}
