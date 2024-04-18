package com.example.demo.util.data_reader.sample;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.util.data_reader.ReadFileTypeEnum;
import com.example.demo.util.data_reader.multi.AbsMultiFileObjectMapReader;

import lombok.extern.slf4j.Slf4j;


/**
 * <pre>데이터 파일을 읽어서 동일한 인터페이스로 업무영역을 streaming 처리를 할수 있도록 기본 설정을 하는 Reader 클래스
 * 
 * SamplePinNumDto 를 매핑정보 등을 정의하고, 실제 업무(main 부분 참조)는 업무 처리 로직에서 saveAction 을 override 해서 로직내에서 처리하도록 한다.
 * 
 * 참조 : {@link MultiFileHashMapReader} : List<HashMap> 으로 saveAction 을 받아서 처리하는 샘플. 처리 샘플로 제공하는 것으로 가급적 Dto 를 만들고, 해당 Dto 를 처리하도록 Reader 를 생성해서 사용하자.
 * </pre>
 */
@Slf4j
public abstract class PinNumMultiFileObjectMapReader extends AbsMultiFileObjectMapReader<SamplePinNumDto> {
	
	
	@Override
	protected Class<SamplePinNumDto> getRowClass() {
		return SamplePinNumDto.class;
	}
	

	private static Map<String, String> cellToField = new HashMap<>();
	static {
		Map<String, String> temp = new HashMap<>();
		temp.put("A", "pinNum");
		temp.put("B", "staDtm");
		temp.put("C", "endDtm");
		temp.put("D", "pinUrl");
		cellToField = Collections.unmodifiableMap(temp);
	}
	@Override
	protected Map<String, String> getCellToField() {
		return cellToField;
	}
	
	
	public PinNumMultiFileObjectMapReader(int rowsSize, ReadFileTypeEnum readDataTypeEnum) {
		super(rowsSize, readDataTypeEnum);
	}
	
	
	public static void main(String[] args) throws Exception {
		log.debug("구현 테스트. "+PinNumMultiFileObjectMapReader.class.getSimpleName());
		TestLocalMultiFileReader.mainPinNumObjectMapReader(args);
	}
}
