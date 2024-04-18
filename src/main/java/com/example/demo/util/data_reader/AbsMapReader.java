package com.example.demo.util.data_reader;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbsMapReader implements Closeable {
	
	private DataFileReader<?> reader;
	protected ReadDataTypeEnum readDataTypeEnum;
	
	public void initReader(int rowSize, ReadDataTypeEnum readDataTypeEnum) {
		if ( readDataTypeEnum == null ) {
			throw new RuntimeException("not supported dataType! ReadDataTypeEnum is null");
		}
		
		this.reader = null;
		this.readDataTypeEnum = readDataTypeEnum;
		
		@SuppressWarnings("resource")
		AbsMapReader parent = this;
		
		switch(readDataTypeEnum) {
		case CSV:
			reader = new MapCsvReader(rowSize) {
				@Override
				public void saveAction(List<Map<String, String>> rows) {
					parent.saveAction(rows);
				}
			};
			break;
		case XLS:
			reader = new MapXlsStreamReader(rowSize) {
				@Override
				public void saveAction(List<Map<String, String>> rows) {
					parent.saveAction(rows);
				}
			};
			break;
		case XLSX:
			reader = new MapXlsxReader(rowSize) {
				@Override
				public void saveAction(List<Map<String, String>> rows) {
					super.saveAction(rows);
					parent.saveAction(rows);
				}
			};
			break;
		default:
			break;
		}
		if ( reader == null ) {
			throw new RuntimeException("not supported dataType! ReadDataTypeEnum=" + readDataTypeEnum);
		}
	}

	public AbsMapReader(int rowsSize, ReadDataTypeEnum readDataTypeEnum) {
		this.initReader(rowsSize, readDataTypeEnum);
	}

	
	private void parse(FileInputStream fileInputStream) throws IOException {
		reader.readData(fileInputStream);
		reader.parse();
	}
	
	public abstract void saveAction(List<Map<String, String>> rows);

	@Override
	public void close() throws IOException {
		AbsDataFileReader.closeObject("SamplePinNumMapReader", reader);
	}
	
	
	public static void main(String[] args) throws Exception {
		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		main21(args);
	}
	public static void main21(String[] args) throws Exception {
		final int rowsSize = 1000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		List<String> filePaths = new ArrayList<>();

//		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample_xls_down/file_example_XLS_5000.xls"); // 0.3초만에 다 읽어버리네... 307 M 까지... 더 많은데???

		
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns.csv"); // 0.3초만에 다 읽어버리네... 307 M 까지... 더 많은데???
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns_20.xlsx"); // 0.3초만에 다 읽어버리네... 307 M 까지... 더 많은데???
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample_xls_down/file_example_XLS_10.xls"); // 0.3초만에 다 읽어버리네... 307 M 까지... 더 많은데???
		
		filePaths.forEach(filePath -> {
			log.debug("\n\n\n\n\n{}", filePath);
			try (
					AbsMapReader dataFileReader = new AbsMapReader(rowsSize, ReadDataTypeEnum.getReadDataTypeEnum(filePath)) {
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
