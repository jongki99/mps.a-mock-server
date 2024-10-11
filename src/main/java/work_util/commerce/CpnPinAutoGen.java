package work_util.commerce;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CpnPinAutoGen {

    public static void main(String[] args) {
        String prefix = "20240808"; // 접두사
        int rowCount = 1000; // 생성할 행 수
        boolean useIncrement = true; // true: 증분, false: 랜덤 문자
        int columnCount = 4; // 생성할 열 수
        String fileName;
        
        useIncrement = true; // true: 증분, false: 랜덤 문자
        fileName = getFileNames(rowCount, useIncrement);
        boolean includeHeader = false; // 헤더 포함 여부

        generateExcel(fileName, rowCount, columnCount, includeHeader, prefix, useIncrement);

        useIncrement = false; // true: 증분, false: 랜덤 문자
        fileName = getFileNames(rowCount, useIncrement);
        generateExcel(fileName, rowCount, columnCount, includeHeader, prefix, useIncrement);
    }
    
    /**
     * 
     * @param prefix cpnPin 앞자리. 고정.
     * @param rowCount 1 이상. 생성 할 개수.
     * @param useIncrement true: 증분, false: 랜덤 문자
     * @return
     */
    private static String getFileNames(int rowCount, boolean useIncrement) {
    	String pattern = "yyyyMMddHHmmss";
		String prefix = LocalDateTime.now().toString(pattern);
        String randomType;
		if ( useIncrement ) {
			randomType = "S";
        } else {
        	randomType = "R";
        }
        String filePath = "/Users/P170355/Downloads/0000-ticket-temp/cpnPin/" + prefix.substring(0, 8);
        File dir = new File(filePath);
        if ( ! dir.exists() ) {
        	dir.mkdirs();
        }
        String fileName = "cpnPinGen-"+prefix+"_"+rowCount+"_"+randomType+".xlsx";
    	return filePath + "/" + fileName;
    }

    public static void generateExcel(String fileName, int rowCount, int columnCount, 
                                     boolean includeHeader, String prefix, boolean useIncrement) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) { // 100개의 행만 메모리에 유지
            SXSSFSheet sheet = workbook.createSheet("Sheet1");

            int rowNum = 0;
            
            if (includeHeader) {
                Row headerRow = sheet.createRow(rowNum++);
                for (int i = 0; i < columnCount; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue("Column " + (i + 1));
                }
            }

            Random random = new Random();
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.createRow(rowNum++);
//                for (int j = 0; j < columnCount; j++) {
                    Cell cell = row.createCell(0);
                    cell.setCellValue(generateValue(prefix, useIncrement ? i : -1, random));
//                }
                
                if (i % 100000 == 0) {
                	log.debug("Processed {} rows", i);
                }
            }

            File file = new File(fileName);
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }

            workbook.dispose(); // 임시 파일 정리
            log.info("Excel file has been created successfully. file={}", file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateValue(String prefix, int increment, Random random) {
        StringBuilder sb = new StringBuilder(prefix+"_");
        if (increment >= 0) {
            sb.append(String.format("%010d", increment));
        } else {
            for (int i = 0; i < 10; i++) {
                sb.append(randomChar(random));
            }
        }
        return sb.toString();
    }

    private static char randomChar(Random random) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return chars.charAt(random.nextInt(chars.length()));
    }
}