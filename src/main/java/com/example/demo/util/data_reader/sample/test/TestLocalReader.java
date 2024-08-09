package com.example.demo.util.data_reader.sample.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo.util.data_reader.reader.csv.MapCsvReader;
import com.example.demo.util.data_reader.reader.xls.MapXlsMemoryReader;
import com.example.demo.util.data_reader.reader.xls.MapXlsStreamReader;
import com.example.demo.util.data_reader.reader.xlsx.MapXlsxReader;
import com.example.demo.util.data_reader.sample.base.SampleFileConstant;
import com.example.demo.util.data_reader.sample.base.SamplePinNumDto;
import com.example.demo.util.data_reader.sample.base.SaveActionUtil;
import com.example.demo.util.data_reader.sample.reader.SimpleAbsXlsxMapReader;
import com.example.demo.util.data_reader.sample.reader.SimpleAbsXlsxPinNumReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLocalReader {
	
	public static void main(String[] args) throws Exception{
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
//		mainXlsxReaderAll(args);
		
//		mainMapXlsxReader(args);
		
//		mainMapCsvReader(args);
		
		mainMapXlsMemoryReader(args);
		
//		mainMapXlsStreamReader(args);
	}
	
	public static void mainXlsxReaderAll(String[] args) {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");

//		log.debug("start main1 large test");
		mainPinNumXlsxReaderLarge(args);
		
		log.debug("start first sheet");
		mainPinNumXlsxReader(args);
		log.debug("end first sheet");
		
//		log.debug("start multi sheet");
		mainPinNumXlsxReaderAllSheet(args);
//		log.debug("end first sheet");
		log.debug("end");
	}
	
	private static void mainPinNumXlsxReaderLarge(String[] args) {
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
//		filePaths.add(SampleFileConstant.XLSX.temp_data_6_columns);
//		filePaths.add(SampleFileConstant.XLSX.temp_data_20);
		filePaths.add(SampleFileConstant.XLSX.test_20);

		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			File file = new File(filePath);
			
			try(SimpleAbsXlsxPinNumReader xlsxReader = new SimpleAbsXlsxPinNumReader(rowsSize) {
				@Override
				public void saveAction(List<SamplePinNumDto> rows) {
					SaveActionUtil.saveActionDto(rows);
				}
			};) {
				xlsxReader.readData(new FileInputStream(file));
				xlsxReader.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});

//		try {
//			Thread.sleep(10_000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		// 메모리 refresh 되는 동안 대기
	}

	private static void mainPinNumXlsxReader(String[] args) {
		final int rowsSize = 10000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add(SampleFileConstant.XLSX.test_20_2);

		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			File file = new File(filePath);
			
			try(SimpleAbsXlsxPinNumReader xlsxReader = new SimpleAbsXlsxPinNumReader(rowsSize) {
				@Override
				public void saveAction(List<SamplePinNumDto> rows) {
					SaveActionUtil.saveActionDto(rows);
				}
			};) {
				xlsxReader.readData(new FileInputStream(file)); // 2개 시트 작은 데이터까지만 테스트. 정상임.
				xlsxReader.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	private static void mainPinNumXlsxReaderAllSheet(String[] args) {
		final int rowsSize = 10000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add(SampleFileConstant.XLSX.test_20_2);

		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			File file = new File(filePath);
			
			try(SimpleAbsXlsxPinNumReader xlsxReader = new SimpleAbsXlsxPinNumReader(rowsSize) {
				@Override
				public void saveAction(List<SamplePinNumDto> rows) {
					SaveActionUtil.saveActionDto(rows);
				}
			};) {
				xlsxReader.readData(new FileInputStream(file), true); // 2개 시트 작은 데이터까지만 테스트. 정상임.
				xlsxReader.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}


	public static void mainMapXlsxReader(String[] args) {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
		mainMapCouponPinTestMapXlsxReader(args);
		mainMapMapXlsxReader(args);
	}
	
	public static void mainMapCouponPinTestMapXlsxReader(String[] args) {
		List<String> filePaths = new ArrayList<>();
		/**
		 * java memory 설정. run configuration.
		 * 
		 */
//		filePaths.add(SampleFileConstant.XLSX.temp_data_6_columns);
		filePaths.add(SampleFileConstant.XLSX.temp_data_20);
		filePaths.add(SampleFileConstant.XLSX.test_20);
		
		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			
			File file = new File(filePath);
			try(SimpleAbsXlsxMapReader excelSheetHandler = new SimpleAbsXlsxMapReader(10000) {
				@Override
				public void saveAction(List<Map<String, String>> rows) {
					SaveActionUtil.saveAction(rows);
				}
			};) {
				excelSheetHandler.readData(new FileInputStream(file));
				excelSheetHandler.parse();
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

	public static void mainMapMapXlsxReader(String[] args) {
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add(SampleFileConstant.XLSX.test_20_2);

		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			File file = new File(filePath);
			try(
					MapXlsxReader xlsxReader = new MapXlsxReader() {
						@Override
						public void saveAction(List<Map<String, String>> rows) {
							SaveActionUtil.saveAction(rows);
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
	

	public static void mainMapCsvReader(String[] args) throws Exception {
		final int rowsSize = 100000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		
		List<String> filePaths = new ArrayList<>();
//		filePaths.add(SampleFileConstant.XLSX.test_20_2);
//		filePaths.add(SampleFileConstant.XLS.file_example_XLS_10);
		filePaths.add(SampleFileConstant.CSV.g_100);
		filePaths.add(SampleFileConstant.CSV.temp_data_6_columns);
		// 5000건 -Xmx1m 까지 동작을 하네...
		// 일단 data 출력 테스트.

		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					MapCsvReader xls2csv = new MapCsvReader(rowsSize);
			) {
				xls2csv.readData(new FileInputStream(filePath));
				xls2csv.parse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	

	public static void mainMapXlsMemoryReader(String[] args) throws Exception {
		final int rowsSize = 1000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		
		List<String> filePaths = new ArrayList<>();
//		filePaths.add(SampleFileConstant.XLS.file_example_XLS_5000);
//		filePaths.add(SampleFileConstant.XLS.file_example_XLS_10);
//		filePaths.add(SampleFileConstant.XLS.file_example_XLS_20000_emptyCell);
//		filePaths.add(SampleFileConstant.XLS.file_example_XLS_20000_emptyRow);
		// 5000건 -Xmx9m 까지 동작을 하네... 8m 부터는 OOM

		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					MapXlsMemoryReader<Map<String, String>> xls2csv = new MapXlsMemoryReader<>(rowsSize);
			) {
				xls2csv.readData(new FileInputStream(filePath));
				xls2csv.parse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	public static void mainMapXlsStreamReader(String[] args) throws Exception {
		final int rowsSize = 1000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		
		List<String> filePaths = new ArrayList<>();
		filePaths.add(SampleFileConstant.XLS.file_example_XLS_10);
		filePaths.add(SampleFileConstant.XLS.file_example_XLS_5000);
//		filePaths.add(SampleFileConstant.XLS.file_example_XLS_20000_emptyRow); // 이거 때문에 사용중지. 천천히 할껄...
		// 5000건 -Xmx1m 까지 동작을 하네...
		// 일단 data 출력 테스트.

		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					MapXlsStreamReader xls2csv = new MapXlsStreamReader(rowsSize);
			) {
				xls2csv.readData(new FileInputStream(filePath));
				xls2csv.parse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Thread.sleep(5000);
	}
}
