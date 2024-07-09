package ReadExcel;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	
    public static Map<String, Row> readExcelFile() {
    	Map<String, Row> testCasesToRun = new LinkedHashMap<>();
        // Utiliza el ClassLoader para obtener el archivo desde la carpeta test/resources/datos
        ClassLoader classLoader = ReadExcel.class.getClassLoader();

        try (InputStream is = classLoader.getResourceAsStream("TestCases.xlsx"); 
        		Workbook workbook = new XSSFWorkbook(is)) 
        {
            // Obtén la primera hoja del libro de Excel
            Sheet sheet = workbook.getSheetAt(0);
            // Itera sobre las filas, comenzando desde la segunda fila (índice 1)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                
                if (row != null) {
                    // Obtén el nombre del test case de la primera columna
                    Cell cell = row.getCell(1);
                    
                    if (cell.getCellType() == CellType.STRING) {
                        String testCaseName = cell.getStringCellValue();
                        testCasesToRun.put(testCaseName, row);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return testCasesToRun;
    }
}