package com.example.demo.util.data_reader.sample;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.example.demo.util.data_reader.ReadFileTypeEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLocalMultiFileReader {
	public static void main(String[] args) throws Exception {
		mainPinNumObjectMapReader(args);
		mainMultiFileHashMapReader(args);
	}

	public static void mainPinNumObjectMapReader(String[] args) throws Exception {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
		final int rowsSize = 1000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		List<String> filePaths = new ArrayList<>();

		filePaths.add(SampleFileConstant.CSV.temp_data_6_columns_10row);
		filePaths.add(SampleFileConstant.XLSX.temp_data_6_columns_20);
		filePaths.add(SampleFileConstant.XLS.file_example_XLS_10);
		
		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					PinNumMultiFileObjectMapReader dataFileReader = new PinNumMultiFileObjectMapReader(rowsSize, ReadFileTypeEnum.getReadDataTypeEnum(filePath)) {
						@Override
						public void saveAction(List<SamplePinNumDto> rows) {
							if ( CollectionUtils.isNotEmpty(rows) ) {
								// apache poi 를 사용하므로 common 꺼를 사용... isNotEmpty 도 있고.
								log.debug("SamplePinNumMapReader 데이터 확인용 샘플. 저장 등의 업무 처리 용으로 사용. ReadDataTypeEnum={}", this.readDataTypeEnum);
								log.debug("first={}", rows.get(0));
								log.debug("last={}", rows.get(rows.size()-1));
								log.debug("saveAction rows.size={}", rows.size());
							}
						}
					};
			) {
				dataFileReader.parse(new FileInputStream(filePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	

	public static void mainMultiFileHashMapReader(String[] args) throws Exception {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
		final int rowsSize = 1000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		List<String> filePaths = new ArrayList<>();

		filePaths.add(SampleFileConstant.CSV.temp_data_6_columns_10row);
		filePaths.add(SampleFileConstant.XLSX.temp_data_6_columns_20);
		filePaths.add(SampleFileConstant.XLS.file_example_XLS_10);
		
		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					MultiFileHashMapReader dataFileReader = new MultiFileHashMapReader(rowsSize, ReadFileTypeEnum.getReadDataTypeEnum(filePath)) {
						@Override
						public void saveAction(List<Map<String, String>> rows) {
							if ( CollectionUtils.isNotEmpty(rows) ) {
								// apache poi 를 사용하므로 common 꺼를 사용... isNotEmpty 도 있고.
								log.debug("SamplePinNumMapReader 데이터 확인용 샘플. 저장 등의 업무 처리 용으로 사용. ReadDataTypeEnum={}", this.readDataTypeEnum);
								log.debug("first={}", rows.get(0));
								log.debug("last={}", rows.get(rows.size()-1));
								log.debug("saveAction rows.size={}", rows.size());
							}
						}
					};
			) {
				dataFileReader.parse(new FileInputStream(filePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
