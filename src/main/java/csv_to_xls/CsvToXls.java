package csv_to_xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvToXls {

    public static void main(String[] args) {
        // 폴더 경로 지정
    	String folderPath = "";
        folderPath = "/Users/P170355/Downloads/0000-ticket-temp/9541-issueCustom-v2-testXlsx"; // 실제 폴더 경로로 변경하세요
        folderPath = "/Users/P170355/Downloads/0000-ticket-temp/9541-issueCustom-v2-testXlsx/reissue"; // 실제 폴더 경로로 변경하세요

        // 폴더 내의 모든 파일을 가져옵니다.
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                // CSV 파일을 XLS로 변환
                try {
                	log.debug("file={}", file);
                    convertCsvToXls(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("지정된 폴더가 비어있거나 접근할 수 없습니다.");
        }
    }

    private static void convertCsvToXls(File csvFile) throws IOException {
        try (
            FileInputStream fis = new FileInputStream(csvFile);
            CSVParser parser = new CSVParser(new InputStreamReader(fis, StandardCharsets.UTF_8), CSVFormat.DEFAULT);
            Workbook workbook = new HSSFWorkbook();  // HSSFWorkbook을 사용하여 XLS 파일 생성
            FileOutputStream outputStream = new FileOutputStream(getXlsFilePath(csvFile))
        ) {
            Sheet sheet = workbook.createSheet("Sheet1");
            // 헤더 생성 -- 해더도 똑같던가? 까먹었네.
            int rowNum = 0;
            List<String> headers = parser.getHeaderNames();
            if ( headers.size() > 0 ) {
            	rowNum++;
            	Row headerRow = sheet.createRow(0);
            	for (int i = 0; i < headers.size(); i++) {
            		Cell cell = headerRow.createCell(i);
            		cell.setCellValue(headers.get(i));
            		
            		// test case 추가. 값이 없는 셀을 만든다.
            		// test case 추가. 셀을 건너뛰어서 만든다.
            	}
            }

            // 데이터 기록
            for (CSVRecord record : parser) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < record.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(record.get(i));
                }
        		
        		// test case 추가. 값이 없는 열을 만든다.
        		// test case 추가. 열을 건너뛰어서 만든다.
            }

            workbook.write(outputStream);
        }
    }

    private static String getXlsFilePath(File csvFile) {
        String filePath = csvFile.getAbsolutePath();
        return filePath.substring(0, filePath.lastIndexOf('.')) + ".xls";
    }
}