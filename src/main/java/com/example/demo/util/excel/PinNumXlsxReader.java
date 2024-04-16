package com.example.demo.util.excel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.global.util.StringUtil;

@Slf4j
public class PinNumXlsxReader extends ObjectXlsxReader<PinNumDto> {
	
	public PinNumXlsxReader(int rowSize) {
		super(rowSize);
	};
	
	@Override
	protected Class<PinNumDto> getRowClass() {
		return PinNumDto.class;
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
	public void saveAction(List<PinNumDto> rows) {
		// TODO DB save action.
		totalCounted += rows.size(); // 
		log.debug("rows.size={}", rows.size());
		log.debug("dto.toString={}", rows.get(0).toString());
	}
	
	@Override
	protected boolean isValidationObject(PinNumDto pin) {
		if ( StringUtil.isBlank(pin.pinNum) ) {
			return false;
		}
		return true;
	}
	
}
