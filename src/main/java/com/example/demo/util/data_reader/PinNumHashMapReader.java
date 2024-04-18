package com.example.demo.util.data_reader;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;


/**
 * <pre>데이터 파일을 읽어서 동일한 인터페이스로 업무영역을 streaming 처리를 할수 있도록 기본 설정을 하는 Reader 클래스
 * 
 * 여기서는 매핑정보 등을 정의하고, 실제 업무는 업무 처리 로직에서 saveAction 을 override 해서 로직내에서 처리하도록 한다.
 * 
 * 참조 : {@link PinNumObjectMapReader} : SamplePinNumDto 으로 saveAction 을 받아서 처리하는 샘플. 매핑 샘플로 제공.
 * </pre>
 */
@Slf4j
public abstract class PinNumHashMapReader extends AbsObjectMapReader<Map<String, String>> {
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<Map<String, String>> getRowClass() {
		return (Class<Map<String, String>>) (Class<?>) HashMap.class;
	}

	@Override
	protected Map<String, String> getCellToField() {
		return null;
	}
	
	
	public PinNumHashMapReader(int rowsSize, ReadDataTypeEnum readDataTypeEnum) {
		super(rowsSize, readDataTypeEnum);
	}
	
	
	public static void main(String[] args) throws Exception {
		TestLocalMultiMapReader.mainPinNumHashMapReader(args);
	}
}
