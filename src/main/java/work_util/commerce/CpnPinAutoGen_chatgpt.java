package work_util.commerce;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class CpnPinAutoGen_chatgpt {

    public static void main(String[] args) {
        // 엑셀 파일 이름과 경로 설정
        String fileName = "CpnPinData.xlsx";

        // 옵션 설정
        boolean includeHeader = true;
        String prefix = "CPN"; // prefix 설정
        int rowCount = 1000; // 생성할 행의 수
        int colCount = 5; // 생성할 열의 수
        boolean useRandom = false; // 랜덤 생성 여부, true면 랜덤 생성, false면 숫자 증분

        // 엑셀 파일 생성
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(); // 메모리 사용을 최소화하기 위한 SXSSFWorkbook 사용
             FileOutputStream fileOut = new FileOutputStream(fileName)) {

            Sheet sheet = workbook.createSheet("CouponPins");

            int rowIndex = 0;

            // 헤더 추가 옵션이 true일 때 헤더를 추가
            if (includeHeader) {
                Row headerRow = sheet.createRow(rowIndex++);
                for (int colIndex = 0; colIndex < colCount; colIndex++) {
                    Cell cell = headerRow.createCell(colIndex);
                    cell.setCellValue("Header" + (colIndex + 1));
                }
            }

            // 데이터 행 생성
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.createRow(rowIndex++);
                for (int colIndex = 0; colIndex < colCount; colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    String value;
                    if (useRandom) {
                        value = generateRandomValue(prefix);
                    } else {
                        value = generateIncrementalValue(prefix, i, colIndex);
                    }
                    cell.setCellValue(value);
                }
            }

            // 엑셀 파일 저장
            workbook.write(fileOut);
            System.out.println("Excel file generated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateIncrementalValue(String prefix, int rowIndex, int colIndex) {
        return prefix + "_" + (rowIndex + 1) + "_" + (colIndex + 1);
    }

    private static String generateRandomValue(String prefix) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 8; i++) {
            int rand = random.nextInt(36); // 0-9와 A-Z까지 랜덤 생성
            if (rand < 10) {
                sb.append(rand);
            } else {
                sb.append((char) ('A' + rand - 10));
            }
        }
        return sb.toString();
    }
}