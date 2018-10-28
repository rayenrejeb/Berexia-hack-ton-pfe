package com.zhuinden.sparkexperiment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Iterator;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ConvertXLXSToCSV {

    static void convertSelectedSheetInXLXSFileToCSV(String xlsxFile, int sheetIdx) throws Exception {

        FileInputStream fileInStream = new FileInputStream(xlsxFile);

        // change path of file to : xlsxFile
        File file = new File("/home/rayen/Documents/spring-spark-example/src/main/resources/EA_result_1351.csv");

        CsvWriterSettings settings = new CsvWriterSettings();
        settings.setExpandIncompleteRows(true);
        settings.getFormat().setLineSeparator("\n");

        CsvWriter writer = new CsvWriter(new FileWriter(file, true), settings);
        // Open the xlsx and get the requested sheet from the workbook
        XSSFWorkbook workBook = new XSSFWorkbook(fileInStream);
        XSSFSheet selSheet = workBook.getSheetAt(sheetIdx);
        // Iterate through all the rows in the selected sheet

        Iterator<Row> rowIterator = selSheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Iterate through all the columns in the row and build ","
            // separated string
            Iterator<Cell> cellIterator = row.cellIterator();
            StringBuffer sb = new StringBuffer();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (sb.length() != 0) {
                    sb.append(";");
                }

                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        sb.append(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        sb.append(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        sb.append(cell.getBooleanCellValue());
                        break;
                    default:
                }
            }
            writer.writeRow(sb.toString());
        }
        workBook.close();
        writer.close();
    }

}