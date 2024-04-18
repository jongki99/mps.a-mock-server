package com.example.demo.util.data_reader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLocalXlsxReaderMain {
	
	public static void main(String[] args) {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");

//		log.debug("start main1 large test");
		main1(args);
		
		log.debug("start first sheet");
//		main21(args);
		log.debug("end first sheet");
		
//		log.debug("start multi sheet");
//		main22(args);
//		log.debug("end first sheet");
		
		log.debug("end");
	}
	
	public static void main1(String[] args) {
		// 메모리 옵션. 메모리 모니터링. 정상. heap size 를 지정한 거라... 조금 많이 오버됨.
		// 로컬 파일기준이라...
		// S3 stream 메모리 성능도 그렇고, 이것저것 더 잡아 먹을것 같기는 한데... 일단 read 와 분할처리까지는 테스트.
		
		// -Xms64m -Xmx64m
		final int rowsSize = 10000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		// 185, 165 찍혀있고, 마무리됨. 10000개 단위를 더 줄여보자.
		
		// -Xms32m -Xmx32m
//		final int rowsSize = 1000; // 약 7초 로그가 1000개인데 별차이는 없음.
		// 103, 104 찍혀있다가 사라짐. 1000 개단위.
		
		// rowSize 단위로 객체를 만들어서 list 로 할당하니 당연히 메모리를 많이 쓰지... temp 넣는게 1000개 단위였으니, 성능상 단점은 없는 것으로...
		// 할당후 해제가 잘 되도록 만 해주고, 후처리에서도 동일하게 문제가 없도록만 해주면 될듯. 필요시 totalCount 등은 impl 시에 구현해서 사용하면 될듯.
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns.xlsx");
//		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_20.xlsx");
//		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20.xlsx");

		filePaths.forEach(filePath -> {
			log.debug(filePath);
			File file = new File(filePath);
			
			try(PinNumXlsxReader xlsxReader = new PinNumXlsxReader(rowsSize);) {
				xlsxReader.readData(new FileInputStream(file));
				xlsxReader.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});

		try {
			Thread.sleep(10_000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 메모리 refresh 되는 동안 대기
	}

	public static void main21(String[] args) {
		final int rowsSize = 10000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20-2.xlsx");

		filePaths.forEach(filePath -> {
			log.debug(filePath);
			File file = new File(filePath);
			
			try(PinNumXlsxReader xlsxReader = new PinNumXlsxReader(rowsSize);) {
				xlsxReader.readData(new FileInputStream(file)); // 2개 시트 작은 데이터까지만 테스트. 정상임.
				xlsxReader.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	public static void main22(String[] args) {
		final int rowsSize = 10000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20-2.xlsx");

		filePaths.forEach(filePath -> {
			log.debug(filePath);
			File file = new File(filePath);
			
			try(PinNumXlsxReader xlsxReader = new PinNumXlsxReader(rowsSize);) {
				xlsxReader.readData(new FileInputStream(file), true); // 2개 시트 작은 데이터까지만 테스트. 정상임.
				xlsxReader.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}


	public static void mainMap(String[] args) {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
		mainMap1(args);
		mainMap2(args);
	}
	
	public static void mainMap1(String[] args) {
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
			try(CouponPinTestMapXlsxReader excelSheetHandler = new CouponPinTestMapXlsxReader(10000);) {
				excelSheetHandler.readData(new FileInputStream(file));
				excelSheetHandler.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	public static void mainMap2(String[] args) {
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20-2.xlsx");

		filePaths.forEach(filePath -> {
			log.debug(filePath);
			File file = new File(filePath);
			try(
					MapXlsxReader xlsxReader = new MapXlsxReader() {
						@Override
						public void saveAction(List<Map<String, String>> rows) {
							log.debug("rows.size={}", rows.size());
							log.debug("dto.toString={}", rows.get(0).toString());
						}
					};
					) {
				xlsxReader.readData(new FileInputStream(file)); // 2개 시트 작은 데이터까지만 테스트. 정상임.
				xlsxReader.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}
}
