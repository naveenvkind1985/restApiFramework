package com.test.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Platform;

public class ExcelReader {
	
	// Main Directory of the project
	public static final String currentDir = System.getProperty("user.dir");

	public static final String excelfileName = "testdata.xlsx";

	// Location of Test data excel file
	public static String testDataExcelPath = null;

    private static int testIDColumn = 0;
    private static int jsonPathColumn = 11;

    public static String[][] read(String dataSheetName) {

        if (!dataSheetName.trim().endsWith(".xlsx")) {
            System.out.println("Given file is not a supported excel datasheet");
            return null;
        }
        System.out.println("Reading from file " + dataSheetName);

        String[][] data = null;
        try {
            FileInputStream fis = new FileInputStream(new File(dataSheetName));
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet("TestData");
            data = readWorkSheet(sheet);
            try {
                XSSFSheet xpathSheet = workbook.getSheet("JsonPathValidation");
                mapJsonPathDataSheet(data, xpathSheet);
            } catch (Exception e) {

                System.out.println("Json Path Sheet  Not Found");
            }
            fis.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static void mapJsonPathDataSheet(String[][] data, XSSFSheet xpathSheet) {


        String[][] jsonPathData = readWorkSheet(xpathSheet);
        Map<String, String> xPathMap = new HashMap<>();

        /*Puts the jsonPath and expected value into a Map. Key is testID and value is
           jsonPath:expectedValue separated by newline character
        */
        for (String specs[] : jsonPathData) {
            String pathData = specs[1] + ":" + specs[2];
            if (xPathMap.containsKey(specs[testIDColumn])) {
                pathData = xPathMap.get(specs[testIDColumn]) + System.lineSeparator() + pathData;

            }
            xPathMap.put(specs[testIDColumn], pathData);
        }


        /* Matches the TestID of the HashMap and TestId column of First DataSheet and replaces
            the JsonPath column with the Json Path Data from the HashMap
         */
        for (String specs[] : data) {

            String xPathData = "";
            if (xPathMap.containsKey(specs[testIDColumn])) {
                xPathData = xPathMap.get(specs[testIDColumn]);
            }
            specs[jsonPathColumn] = xPathData;

        }


    }


    private static String[][] readWorkSheet(XSSFSheet sheet) {
        int rowCount = sheet.getLastRowNum();

        // get the number of columns
        int columnCount = getColumnCount(sheet.getRow(0));
        String[][] data = new String[rowCount][columnCount];

        // loop through the rows
        for (int i = 1; i < rowCount + 1; i++) {
            try {
                XSSFRow row = sheet.getRow(i);
                for (int j = 0; j < columnCount; j++) { // loop through the
                    // columns
                    try {
                        String cellValue = "";
                        try {

                            DataFormatter formatter = new DataFormatter(Locale.US);
                            cellValue = formatter.formatCellValue(row.getCell(j));

                        } catch (NullPointerException e) {
                            System.out.println("Failed to parse column:" + j + 1 + " at row:" + i + 1);
                        }
                        data[i - 1][j] = cellValue; // add to the data array
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private static Integer getColumnCount(XSSFRow firstRow) {

        DataFormatter formatter = new DataFormatter(Locale.US);
        int count = 0;
        while (!formatter.formatCellValue(firstRow.getCell(count)).isEmpty()) {
            count++;
        }
        return count;
    }
    
    
    
    /**
	 * @param rowName, columnName, sheetName
	 * @return
	 * @throws IOException
	 */
	public static String getTestDataRowColumnData(String sheetName, String rowName, String columnName)
			throws IOException {
		String cellData = "";
		try {
			if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
				testDataExcelPath = currentDir + "//input//" + excelfileName;
			} else if (Platform.getCurrent().toString().contains("WIN")) {
				testDataExcelPath = currentDir + "\\input\\" + excelfileName;
			}
			FileInputStream fis = new FileInputStream(testDataExcelPath);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			 System.out.println("Input sheet: " + sheetName + " is read from location: " + testDataExcelPath);
			XSSFRow row = sheet.getRow(0);
			int col_num = -1;
			int count = 0;
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(columnName)) {
					col_num = i;
					break;
				}
			}
			XSSFCell cell = row.getCell(0);
			row = sheet.getRow(count);
			cell = row.getCell(0);
			while (!cell.getStringCellValue().equalsIgnoreCase(rowName)
					|| cell.getStringCellValue().equalsIgnoreCase("EndOfSheet")) {
				if (cell.getStringCellValue().equalsIgnoreCase("EndOfSheet")) {
					System.out.println("Invalid input.");
					break;
				}
				count++;
				row = sheet.getRow(count);
				cell = row.getCell(0);
			}
			XSSFCell cell1 = row.getCell(col_num);
			row = sheet.getRow(count);
			cell1 = row.getCell(col_num);
			cellData = cell1.getStringCellValue();
			workbook.close();
			return cellData;
		} catch (IllegalArgumentException e1) {
			System.out.println("Invalid Data has been given");
			throw (e1);
		} catch (NullPointerException e) {
			return cellData;
		} catch (Exception e) {
			throw (e);
		}
	}


}