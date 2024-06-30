package ReadExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import TestCases.TestCases;

public class ReadExcel {
	//private static final Logger logger = LogManager.getLogger(ReadExcel.class);
	private static final Logger logger = LogManager.getLogger(ReadExcel.class);
	
    public void readExcelFile() {
        // Utiliza el ClassLoader para obtener el archivo desde la carpeta test/resources/datos
        ClassLoader classLoader = ReadExcel.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("TestCases.xlsx");
             Workbook workbook = new XSSFWorkbook(is)) {

            // Obtén la primera hoja del libro de Excel
            Sheet sheet = workbook.getSheetAt(0);

            // Itera sobre las filas, comenzando desde la segunda fila (índice 1)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    // Obtén el nombre del test case de la primera columna
                    Cell cell = row.getCell(1);
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String testCaseName = cell.getStringCellValue();
                        executeTestCase(testCaseName, row);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void executeTestCase(String testCaseName, Row row) {
        try {
            // Asume que los métodos de test case están en la clase TestCases
            Method method = TestCases.class.getMethod(testCaseName, Row.class);
            method.invoke(null, row); // Llama al método estático
        } catch (Exception e) {
            logger.error("Error al ejecutar el test case: " + testCaseName, e);
        }
    }
}