package com.example.demo.util.data_reader.reader.xlsx;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.example.demo.util.data_reader.sample.base.SamplePinNumDto;
import com.example.demo.util.data_reader.sample.reader.TestLocalReader;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.global.util.StringUtil;


/**
 * <pre>Dto 를 이용한 saveAction 처리를 위한 구현체...
 * saveAction 을 동적으로 구성하는게 좋을 듯한데..
 * 샘플링해보자... 이게 좋은거 맞나? 업무 가독성은 좋을 듯한데...
 * </pre>
 */
@Slf4j
public class PinNumXlsxReader extends AbsObjectXlsxReader<SamplePinNumDto> {
	
	public PinNumXlsxReader(int rowSize) {
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
	@Override
	public void saveAction(List<SamplePinNumDto> rows) {
		if ( CollectionUtils.isNotEmpty(rows) ) {
			// apache poi 를 사용하므로 common 꺼를 사용... isNotEmpty 도 있고.
			log.debug("데이터 확인용 샘플. 저장 등의 업무 처리 용으로 사용.");
			log.debug("first={}", rows.get(0));
			log.debug("last={}", rows.get(rows.size()-1));
			log.debug("saveAction rows.size={}", rows.size());
		}
	}
	
	@Override
	protected boolean isValidationObject(SamplePinNumDto pin) {
		if ( StringUtil.isBlank(pin.getPinNum()) ) {
			return false;
		}
		
		return true;
	}

	public static void main(String[] args) throws Exception {
		TestLocalReader.mainXlsxReaderAll(args);
	}

	
}
