package ReadExcel;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	
    public static List<Row> readExcelFile() {
    	List<Row> testCasesToRun = new ArrayList<>();
        // Utiliza el ClassLoader para obtener el archivo desde la carpeta test/resources/datos
        ClassLoader classLoader = ReadExcel.class.getClassLoader();

        try (InputStream is = classLoader.getResourceAsStream("TestCases.xlsx"); 
        		Workbook workbook = new XSSFWorkbook(is)) 
        {
            // Obtén la primera hoja del libro de Excel
            Sheet sheet = workbook.getSheetAt(0);
            // Itera sobre las filas, comenzando desde la segunda fila (índice 0)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                
                if (row != null) {                    
                	testCasesToRun.add(row);
                	Cell cell = row.getCell(1);
                    if (cell.getCellType() == CellType.STRING) {
                        String testCaseName = cell.getStringCellValue();
                        System.out.println(row.getCell(0) + ": " + testCaseName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return testCasesToRun;
    }
}