package com.example.demo.util.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CouponPinMapTeestXlsxReader extends MapXlsxReader {

	public CouponPinMapTeestXlsxReader() {
		super(1000);
	}
	public CouponPinMapTeestXlsxReader(int rowSize) {
		super(rowSize);
	}

	@Override
	public void saveAction(List<Map<String, String>> rows) {
		// TODO save
		totalCounted += rows.size(); // 
		log.debug("saveAction rows.size={}", rows.size());
	}

	public static void main(String[] args) {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
		main1(args);
		main2(args);
	}
	
	public static void main1(String[] args) {
		List<String> filePaths = new ArrayList<>();
		/**
		 * java memory 설정. run configuration.
		 * 
		 */
//		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns.xlsx");
//		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_20.xlsx");
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20.xlsx");
		
		filePaths.forEach(filePath -> {
			log.debug(filePath);
			
			File file = new File(filePath);
			CouponPinMapTeestXlsxReader excelSheetHandler = new CouponPinMapTeestXlsxReader(10000);
			try {
				excelSheetHandler.readExcel(file);
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	@Override
	protected boolean isValidationObject(Map<String, String> map) {
		// first cell is required
		return super.isValidationObject(map);
	}

	public static void main2(String[] args) {
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20-2.xlsx");

		filePaths.forEach(filePath -> {
			log.debug(filePath);
			File file = new File(filePath);
			MapXlsxReader xlsxReader = new MapXlsxReader() {
				@Override
				public void saveAction(List<Map<String, String>> rows) {
					// TODO DB save action.
					totalCounted += rows.size(); // 
					log.debug("rows.size={}", rows.size());
					log.debug("dto.toString={}", rows.get(0).toString());
				}
			};
			try {
				xlsxReader.readExcel(file); // 2개 시트 작은 데이터까지만 테스트. 정상임.
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}
}
