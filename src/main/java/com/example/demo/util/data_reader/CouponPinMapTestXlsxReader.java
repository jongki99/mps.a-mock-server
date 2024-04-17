package com.example.demo.util.data_reader;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CouponPinMapTestXlsxReader extends MapXlsxReader {

	public CouponPinMapTestXlsxReader() {
		super(1000);
	}
	public CouponPinMapTestXlsxReader(int rowSize) {
		super(rowSize);
	}

	@Override
	public void saveAction(List<Map<String, String>> rows) {
		log.debug("saveAction rows.size={}", rows.size());
	}

	@Override
	protected boolean isValidationObject(Map<String, String> map) {
		// first cell is required
		return super.isValidationObject(map);
	}
	
	public static void main(String[] args) {
		TestLocalXlsxReaderMain.mainMap(args);
	}
	
}
