package com.example.demo.util.xls;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class SimpleXlxFileWriter {
	
	private static String basePath = "/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample_xls_down/";

	public static void main(String[] args) {
		boolean isFull = true;
		boolean isNotFull = false;
		
		mainCpnPinSample12full(0, isFull);
		mainCpnPinSample12full(20, isFull);
		mainCpnPinSample12full(20, isNotFull);
		mainCpnPinSample12full(65001, isFull);
		mainCpnPinSample12full(65001, isNotFull);
		
	}
	
	
	public static void mainCpnPinSample12full(int createRowSize, boolean isFull) {
		
		try (Workbook workbook = new HSSFWorkbook()) {
            // 새 시트 생성
            Sheet sheet = workbook.createSheet("Sheet1");

            // 데이터 생성 (예시: 5x5 크기의 랜덤 데이터)
            for (int rowNum = 0; rowNum < createRowSize; rowNum++) {
                Row row = sheet.createRow(rowNum);
                
                BigInteger val = new BigInteger("20241237000000");
                val = val.add(new BigInteger(rowNum+""));

                Cell cell = row.createCell(0); // pin num
                cell.setCellValue(val.toString()); // 랜덤 값 채우기

                if ( isFull ) {
                	Cell staDtm = row.createCell(1); // pin num
                	staDtm.setCellValue(20241221000000L); // 랜덤 값 채우기
                	
                	Cell endDtm = row.createCell(2); // pin num
                	endDtm.setCellValue(20241221123456L); // 랜덤 값 채우기
                	
                	Cell linkUrl = row.createCell(3); // pin num
                	linkUrl.setCellValue("https://www.outlink.com/test/val="+val.toString()); // 랜덤 값 채우기
                }
            }

            // 파일로 저장
            String full = isFull ? "-full" : "";
            try (FileOutputStream fileOut = new FileOutputStream(basePath + "xls-"+createRowSize+"-pinNum"+full+".xls")) {
                workbook.write(fileOut);
                System.out.println("Excel 파일이 생성되었습니다.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	
}
