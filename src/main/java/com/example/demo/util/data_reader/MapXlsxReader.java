package com.example.demo.util.data_reader;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.global.util.StringUtil;

/**
 * saveAction 만 추가해주면 동작은 한다.
 */
@Slf4j
public abstract class MapXlsxReader extends AbsXlsxStreamReader<Map<String, String>> {

	public MapXlsxReader() {
		this(1000);
	}
	public MapXlsxReader(int rowSize) {
		super(rowSize);
		log.debug("set rowSize={}", rowSize);
	};

	@Override
	public void endRow(int rowNum) {
		// Map 으로 변환 처리.
		addRow(new HashMap<>(getRowDataMap()));
	}
	
	/**
	 * Map 은 첫 컬럼의 값이 필수.. 이건 샘플. 적절히 수정해서 필수값을 유도하자.
	 */
	@Override
	protected boolean isValidationObject(Map<String, String> map) {
		if ( StringUtil.isBlank(map.get("A")) ) {
			return false;
		}
		// 정상이다.
		return true;
	}
	
}
