package com.example.demo.util.data_reader.multi;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.example.demo.util.data_reader.ReadFileTypeEnum;
import com.example.demo.util.data_reader.reader.AbsDataFileReader;
import com.example.demo.util.data_reader.reader.DataFileReader;
import com.example.demo.util.data_reader.reader.csv.MapCsvReader;
import com.example.demo.util.data_reader.reader.xls.MapXlsStreamReader;
import com.example.demo.util.data_reader.reader.xlsx.MapXlsxReader;
import com.example.demo.util.data_reader.sample.multi.MultiFileHashMapReader;
import com.example.demo.util.data_reader.sample.multi.MultiFilePinNumMapReader;

import lombok.extern.slf4j.Slf4j;


/**
 * <pre>xlsx, xls, csv 파일 통합 reader.saveAction 처리.
 * 
 * 파일 타입을 구현하고, 필요한 업무단위로 단일 인터페이스로 처리하기 위한 베이스 클래스.
 * 
 * 이 클래스를 구현하고, 필요한 정보를 생성하고, 필요한 곳에서 saveAction 만 필요에 따라서 생성과 동시에 업무 처리를 하도록 작업.
 * 
 * 샘플 구현체 : {@link MultiFileHashMapReader} : List<HashMap> 으로 saveAction 을 받아서 처리하는 샘플. 처리 샘플로 제공하는 것으로 가급적 Dto 를 만들고, 해당 Dto 를 처리하도록 Reader 를 생성해서 사용하자.
 * 샘플 구현체 : {@link MultiFilePinNumMapReader} : SamplePinNumDto 으로 saveAction 을 받아서 처리하는 샘플. 매핑 샘플로 제공.
 * </pre>
 * 
 * @param <E>
 */
@Slf4j
public abstract class AbsMultiFileObjectTypeReader<E> implements Closeable {
	
	
	public AbsMultiFileObjectTypeReader(int rowsSize, ReadFileTypeEnum readDataTypeEnum) {
		this.initReader(rowsSize, readDataTypeEnum);
	}
	
	
	private DataFileReader<Map<String, String>> reader;
	protected ReadFileTypeEnum readDataTypeEnum;
	
	
	public void initReader(int rowSize, ReadFileTypeEnum readDataTypeEnum) {
		log.debug("rowSize={}, readDataTypeEnum={}", rowSize, readDataTypeEnum != null ? readDataTypeEnum.name() : null);
		if ( readDataTypeEnum == null ) {
			throw new RuntimeException("not supported dataType! ReadDataTypeEnum is null");
		}
		
		this.reader = null;
		this.readDataTypeEnum = readDataTypeEnum;
		
		switch(readDataTypeEnum) {
		case CSV:
			reader = getCsvReader(rowSize);
			break;
		case XLS:
			reader = getXlsReader(rowSize);
			break;
		case XLSX:
			reader = getXlsxReader(rowSize);
			break;
		default:
			break;
		}
		if ( reader == null ) {
			throw new RuntimeException("not supported dataType! ReadDataTypeEnum=" + readDataTypeEnum);
		}
	}
	
	
	/**
	 * CsvReader 만 갈아끼울때 사용하기 위해서 분리. 이럴 필요가???
	 * 
	 * @param rowSize
	 * @return
	 */
	public DataFileReader<Map<String, String>> getCsvReader(int rowSize) {
		@SuppressWarnings("resource")
		AbsMultiFileObjectTypeReader<E> parent = this;
		return new MapCsvReader(rowSize) {
			@Override
			public void saveAction(List<Map<String, String>> rows) {
				parent.saveAction(parent.convertObject(rows));
			}
		};
	}
	
	
	/**
	 * XlsReader 만 갈아끼울때 사용하기 위해서 분리. 이럴 필요가???
	 * 
	 * @param rowSize
	 * @return
	 */
	public DataFileReader<Map<String, String>> getXlsReader(int rowSize) {
		@SuppressWarnings("resource")
		AbsMultiFileObjectTypeReader<E> parent = this;
		return new MapXlsStreamReader(rowSize) {
			@Override
			public void saveAction(List<Map<String, String>> rows) {
				parent.saveAction(parent.convertObject(rows));
			}
		};
	}
	
	
	/**
	 * XlsxReader 만 갈아끼울때 사용하기 위해서 분리. 이럴 필요가???
	 * 
	 * @param rowSize
	 * @return
	 */
	public DataFileReader<Map<String, String>> getXlsxReader(int rowSize) {
		@SuppressWarnings("resource")
		AbsMultiFileObjectTypeReader<E> parent = this;
		return new MapXlsxReader(rowSize) {
			@Override
			public void saveAction(List<Map<String, String>> rows) {
				parent.saveAction(parent.convertObject(rows));
			}
		};
	}
	
	
	public abstract List<E> convertObject(List<Map<String, String>> rows);

	
	public void parse(InputStream inputStream) throws IOException {
		reader.readData(inputStream);
		reader.parse();
	}

	
	@Override
	public void close() throws IOException {
		AbsDataFileReader.closeObject("SamplePinNumMapReader", reader);
	}
	
	
	public abstract void saveAction(List<E> rows);
	
	
	public int getTotalCount() {
		return reader.getTotalCount();
	}
}
