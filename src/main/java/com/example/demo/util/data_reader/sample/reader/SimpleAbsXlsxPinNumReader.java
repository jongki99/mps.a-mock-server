package com.example.demo.util.data_reader.sample.reader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.util.data_reader.reader.xlsx.AbsXlsxObjectReader;
import com.example.demo.util.data_reader.sample.base.SamplePinNumDto;
import com.example.demo.util.data_reader.sample.test.TestLocalReader;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.global.util.StringUtil;


/**
 * <pre>Dto 를 이용한 saveAction 처리를 위한 구현체...
 * saveAction 을 동적으로 구성하는게 좋을 듯한데..
 * 샘플링해보자... 이게 좋은거 맞나? 업무 가독성은 좋을 듯한데...
 * </pre>
 */
@Slf4j
public abstract class SimpleAbsXlsxPinNumReader extends AbsXlsxObjectReader<SamplePinNumDto> {
	
	public SimpleAbsXlsxPinNumReader(int rowSize) {
		super(rowSize);
	};
	
	@Override
	protected Class<SamplePinNumDto> getRowClass() {
		return SamplePinNumDto.class;
	}
	
	/**
	 * Cell fieldType mapping
	 * 요걸로 엑셀 cell to field 로 매핑해준다.
	 * 
	 * Dto 방식으로 작업을 하는 것을 목표로 했는데... 자동화는 쉽지 않구만...
	 */
	private static Map<String, String> cellToField = new HashMap<>();
	static {
		log.debug("static 을 상속받아서 해야 되므로 주의해서 사용한다. reader 는 Map 을 구현해서 MultiFile 에서 convertObject 에서 직접 변환 처리하는 방향으로 한다.");
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
	
	/**
	 * 저장 처리 업무는 여기서...
	 */
	@Override public abstract void saveAction(List<SamplePinNumDto> rows);
//	public void saveAction(List<SamplePinNumDto> rows) {
//		log.debug("보통은 abstract 로 구현하고, 업무 단에서 구현해서 사용하는 방식으로 작업하도록 한다.");
//		SaveActionUtil.saveActionDto(rows);
//	}
	
	@Override
	protected boolean isValidationObject(SamplePinNumDto pin) {
		if ( StringUtil.isBlank(pin.pinNum()) ) {
			return false;
		}
		
		return true;
	}

	public static void main(String[] args) throws Exception {
		TestLocalReader.mainXlsxReaderAll(args);
	}

	
}
