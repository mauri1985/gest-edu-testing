package TestCases;

import static org.junit.Assert.*;

import org.apache.poi.ss.usermodel.Row;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCases {

	@Before
	public static void setUp() throws Exception {
	}

	@After
	public static void tearDown() throws Exception {
	}

	@Test
	public static void login(Row row) {
		System.out.println("Ejecutando test case login: " + row.toString());
	}
	
	@Test
	public static void logout(Row row) {
		System.out.println("Ejecutando test case logout: " + row.toString());
	}
	
	@Test
	public static void agregarUsuario(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void altaCarrera(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void altaAsignatura(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void agregarPlanEstudio(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void altaCurso(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void altaExamen(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void registarEstudiante(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void inscripcionCarrera(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void aprobarInscCarrera(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void altaPreviatura(Row row) {
		fail("Not yet implemented");
	}
	
	@Test
	public static void inscripcionCurso(Row row) {
		fail("Not yet implemented");
	}
	

}
