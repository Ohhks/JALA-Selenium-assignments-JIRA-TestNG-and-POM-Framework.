package com.jala.magnus.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Framework Step 17: Parameterizes test scripts using Apache POI Excel (.xlsx).
 */
public class ExcelUtil {

    public static Object[][] getSheetData(String excelPath, String sheetName) {
        List<Object[]> rows = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + excelPath);
            }
            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) {
                return new Object[0][0];
            }
            Row header = sheet.getRow(0);
            int colCount = header.getLastCellNum();

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Object[] rowData = new Object[colCount];
                boolean hasValue = false;
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String val = formatter.formatCellValue(cell).trim();
                    if (!val.isEmpty()) hasValue = true;
                    rowData[j] = val;
                }
                if (hasValue) {
                    rows.add(rowData);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel test data from: " + excelPath, e);
        }
        return rows.toArray(new Object[0][0]);
    }
}
