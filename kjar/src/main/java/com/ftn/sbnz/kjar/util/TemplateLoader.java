package com.ftn.sbnz.kjar.util;

import org.drools.template.ObjectDataCompiler;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;

public class TemplateLoader {

    public static String generateDrl(InputStream templateStream, InputStream excelStream) throws Exception {
        Workbook workbook = new XSSFWorkbook(excelStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Čitaj header
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(cell.getStringCellValue());
        }

        // Čitaj redove podataka
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Map<String, Object> rowData = new HashMap<>();
            for (int j = 0; j < headers.size(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) continue;

                switch (cell.getCellType()) {
                    case STRING:
                        rowData.put(headers.get(j), cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.put(headers.get(j), (int) cell.getNumericCellValue());
                        break;
                    default:
                        break;
                }
            }
            rows.add(rowData);
        }

        workbook.close();

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        return compiler.compile(rows, templateStream);
    }
}