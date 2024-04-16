package com.example.demo.util.excel;

import java.util.Map;

import com.example.demo.util.ExcelColumnMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ObjectXlsxReader<E> extends AbsXlsxReader<E> {

	public ObjectXlsxReader() {
		this(10000);
	}
	
	public ObjectXlsxReader(int rowSize) {
		super(rowSize);
	};
	
	protected abstract Class<E> getRowClass();
	
	/**
	 * 엑셀 컬럼과 Dto vo 의 필드를 매핑해주는 설정 정보를 리턴.
	 * Dto 별로 해야 되서... 일단 이렇게...
	 * 
	 * @return
	 */
	protected abstract Map<String, String> getCellToField();

	@Override
	public void endRow(int rowNum) {
		// end row
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
		
		ExcelColumnMapper.mapColumnsToFields(getCellToField(), getRowDataMap(), newObject);
		
		if ( isValidationObject(newObject) ) {
			addRow(newObject);
		}
	}
	
}
