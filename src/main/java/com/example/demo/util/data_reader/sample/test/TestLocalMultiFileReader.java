package com.example.demo.util.data_reader.sample.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.example.demo.util.data_reader.ReadFileTypeEnum;
import com.example.demo.util.data_reader.multi.AbsMultiFileMapTypeReader;
import com.example.demo.util.data_reader.sample.base.SampleFileConstant;
import com.example.demo.util.data_reader.sample.base.SamplePinNumDto;
import com.example.demo.util.data_reader.sample.base.SaveActionUtil;
import com.example.demo.util.data_reader.sample.multi.MultiFileHashMapReader;
import com.example.demo.util.data_reader.sample.multi.MultiFilePinNumMapReader;
import com.example.demo.util.data_reader.sample.multi.MultiFilePinNumReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLocalMultiFileReader {
	public static void main(String[] args) throws Exception {
		mainPinNumMultiFileObjectMapReader(args);
		mainMultiFileHashMapReader(args);
	}

	public static void mainPinNumMultiFileObjectMapReader(String[] args) throws Exception {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
		final int rowsSize = 1000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		List<String> filePaths = new ArrayList<>();

		filePaths.add(SampleFileConstant.CSV.temp_data_6_columns);
		filePaths.add(SampleFileConstant.XLSX.temp_data_6_columns_20);
		filePaths.add(SampleFileConstant.XLS.file_example_XLS_10);
		
		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					MultiFilePinNumMapReader dataFileReader = new MultiFilePinNumMapReader(rowsSize, ReadFileTypeEnum.getReadDataTypeEnum(filePath)) {
						@Override
						public void saveAction(List<SamplePinNumDto> rows) {
							SaveActionUtil.saveActionDto(rows);
						}
					};
			) {
				dataFileReader.parse(new FileInputStream(filePath));
				log.debug("dataFileReader.getTotalCount", dataFileReader.getTotalCount());
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

		filePaths.add(SampleFileConstant.CSV.temp_data_6_columns);
		filePaths.add(SampleFileConstant.XLSX.temp_data_6_columns_20);
		filePaths.add(SampleFileConstant.XLS.file_example_XLS_10);
		
		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					AbsMultiFileMapTypeReader<Map<String, String>> dataFileReader = new MultiFileHashMapReader(rowsSize, ReadFileTypeEnum.getReadDataTypeEnum(filePath)) {
						@Override
						public void saveAction(List<Map<String, String>> rows) {
							SaveActionUtil.saveAction(rows);
						}

					};
			) {
				dataFileReader.parse(new FileInputStream(filePath));
				log.debug("dataFileReader.getTotalCount", dataFileReader.getTotalCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static void mainPinNumMultiFileObjectReader(String[] args) {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
		final int rowsSize = 1000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		List<String> filePaths = new ArrayList<>();

		filePaths.add(SampleFileConstant.CSV.temp_data_6_columns);
		filePaths.add(SampleFileConstant.XLSX.temp_data_6_columns_20);
		filePaths.add(SampleFileConstant.XLS.file_example_XLS_10);
		
		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					MultiFilePinNumReader dataFileReader = new MultiFilePinNumReader(rowsSize, ReadFileTypeEnum.getReadDataTypeEnum(filePath)) {
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
				log.debug("dataFileReader.getTotalCount", dataFileReader.getTotalCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
