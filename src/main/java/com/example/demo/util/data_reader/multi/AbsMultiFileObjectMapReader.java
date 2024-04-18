package com.example.demo.util.data_reader.multi;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo.util.data_reader.ReadFileTypeEnum;
import com.example.demo.util.data_reader.reader.AbsDataFileReader;
import com.example.demo.util.data_reader.reader.DataFileReader;
import com.example.demo.util.data_reader.reader.ExcelColumnMapper;
import com.example.demo.util.data_reader.reader.csv.MapCsvReader;
import com.example.demo.util.data_reader.reader.xls.MapXlsStreamReader;
import com.example.demo.util.data_reader.reader.xlsx.MapXlsxReader;
import com.example.demo.util.data_reader.sample.MultiFileHashMapReader;
import com.example.demo.util.data_reader.sample.PinNumMultiFileObjectMapReader;

import lombok.extern.slf4j.Slf4j;


/**
 * <pre>xlsx, xls, csv 파일 통합 reader.saveAction 처리.
 * 
 * 파일 타입을 구현하고, 필요한 업무단위로 단일 인터페이스로 처리하기 위한 베이스 클래스.
 * 
 * 이 클래스를 구현하고, 필요한 정보를 생성하고, 필요한 곳에서 saveAction 만 필요에 따라서 생성과 동시에 업무 처리를 하도록 작업.
 * 
 * 샘플 구현체 : {@link MultiFileHashMapReader} : List<HashMap> 으로 saveAction 을 받아서 처리하는 샘플. 처리 샘플로 제공하는 것으로 가급적 Dto 를 만들고, 해당 Dto 를 처리하도록 Reader 를 생성해서 사용하자.
 * 샘플 구현체 : {@link PinNumMultiFileObjectMapReader} : SamplePinNumDto 으로 saveAction 을 받아서 처리하는 샘플. 매핑 샘플로 제공.
 * </pre>
 * 
 * @param <E>
 */
@Slf4j
public abstract class AbsMultiFileObjectMapReader<E> implements Closeable {
	
	private DataFileReader<?> reader;
	protected ReadFileTypeEnum readDataTypeEnum;
	
	public void initReader(int rowSize, ReadFileTypeEnum readDataTypeEnum) {
		if ( readDataTypeEnum == null ) {
			throw new RuntimeException("not supported dataType! ReadDataTypeEnum is null");
		}
		
		this.reader = null;
		this.readDataTypeEnum = readDataTypeEnum;
		
		@SuppressWarnings("resource")
		AbsMultiFileObjectMapReader<E> parent = this;
		
		switch(readDataTypeEnum) {
		case CSV:
			reader = new MapCsvReader(rowSize) {
				@Override
				public void saveAction(List<Map<String, String>> rows) {
					parent.saveAction(parent.convertObject(rows));
				}
			};
			break;
		case XLS:
			reader = new MapXlsStreamReader(rowSize) {
				@Override
				public void saveAction(List<Map<String, String>> rows) {
					parent.saveAction(parent.convertObject(rows));
				}
			};
			break;
		case XLSX:
			reader = new MapXlsxReader(rowSize) {
				@Override
				public void saveAction(List<Map<String, String>> rows) {
					parent.saveAction(parent.convertObject(rows));
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

	@SuppressWarnings("unchecked")
	public List<E> convertObject(List<Map<String, String>> rows) {
		try {
			// Map 구현체는 변환처리하면 에러나서 사전체크.
			if ( getRowClass().newInstance() instanceof Map ) {
				return (List<E>) rows;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		List<E> objects = new ArrayList<>();
		
		rows.forEach(row -> {
			E newObject;
			try {
				newObject = getRowClass().newInstance();
				if ( newObject == null ) {
					throw new RuntimeException("row class not initialize!");
				}
			} catch (InstantiationException | IllegalAccessException e) {
				log.error("", e);
				throw new RuntimeException(e);
			}
			
			ExcelColumnMapper.mapColumnsToFields(getCellToField(), row, newObject);
			objects.add(newObject);
		});
		
		return objects;
	}
	
	
	public AbsMultiFileObjectMapReader(int rowsSize, ReadFileTypeEnum readDataTypeEnum) {
		this.initReader(rowsSize, readDataTypeEnum);
	}

	
	public void parse(FileInputStream fileInputStream) throws IOException {
		reader.readData(fileInputStream);
		reader.parse();
	}

	
	@Override
	public void close() throws IOException {
		AbsDataFileReader.closeObject("SamplePinNumMapReader", reader);
	}
	
	
	public abstract void saveAction(List<E> rows);
	
	
	/**
	 * Dto 로 업무를 확실하게 사용하고 싶은 경우, 이 메소드를 구현해서 Dto 를 반환하도록 한다.
	 * 이 메소드가 없어도 하위에 넣으면 되지만... endRow 까지 구현해야 되서...
	 * 아니라면 endRow 업무를 각각의 클래스에서 만들어야 해서 여기서 해두면 업무단이 쉬워진다.
	 * 
	 * @return
	 */
	protected abstract Class<E> getRowClass();
//	protected Class<SamplePinNumDto> getRowClass() {
//		return SamplePinNumDto.class;
//	}
	
	
	/**
	 * 엑셀 컬럼과 Dto 의 필드를 매핑해주는 설정 정보를 리턴.
	 * Dto 별로 해야 되서... 구동로직은 이미 정의되어 있고, 매핑 정보만 하위 클래스에서 구현해서 받아서 처리할 수 있도록 한다.
	 * 
	 * @return
	 */
	protected abstract Map<String, String> getCellToField();
//	protected Map<String, String> getCellToField() {
//		if ( cellToField == null || cellToField.isEmpty() ) {
//			Map<String, String> temp = new HashMap<>();
//			temp.put("A", "pinNum");
//			temp.put("B", "staDtm");
//			temp.put("C", "endDtm");
//			temp.put("D", "pinUrl");
//			cellToField = Collections.unmodifiableMap(temp);
//		}
//		
//		return cellToField;
//	}
	
}
