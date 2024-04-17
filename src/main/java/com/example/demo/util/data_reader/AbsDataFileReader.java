package com.example.demo.util.data_reader;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import lombok.extern.slf4j.Slf4j;

/**
 * xlsx, xls, csv 파일을 읽어서 특정 단위로 saveAction 처리할 수 있도록 인터페이스를 제공한다.
 * 
 * 저장처리를 할 부분에서 saveAction 을 구현하여, 해당 메소드가 처리를 하도록 해서, 일관된 처리방식을 구현할 수 있도록 한다.
 * 
 * 저장한 데이터의 total 카운트 처리.
 * 엑셀, csv 에서 데이터를 읽을때 skip 처리할 row isValidationObject 에서 처리한다.
 * 오류를 내야될 경우는 안에서 exception 을 내주면 되겠지? 종료처리는 정의하지 않았네... -_-;; 이건 쉽지 않겠구만..
 * 
 * AutoCloseable 을 구현해서 자원해제처리를 포함하도록 하자.
 * xls 를 csv 로 변환하는 샘플을 이용해서 이 인터페이스를 구현했는데...
 * 잘 안되면 그냥. 전체를 메모리에 올리고, 순차로 읽으면서 하위 인터페이스를 구현해도 된다.
 * 
 * S3 에 직접 stream 으로 받아서 처리하도록 InputStream 만 만들었다. 추가해도 됨. 구현을 FileInputStream 으로 바꿔주면 되니까...
 * 
 * @param <E>
 */
@Slf4j
public abstract class AbsDataFileReader<E> implements SheetContentsHandler, DataFileReader<E> {

	public AbsDataFileReader(int saveRowSize) {
		this.saveActionSize = saveRowSize; // 모든 데이터를 출력.
	}
	private int saveActionSize = 3; // 10000개 단위로 saveAction 호출.
	protected int getActionRowSize() {
		return saveActionSize;
	}


	/** 전체 처리 개수 카운트 하위에서 호출해줘야 한다. */
	private int totalCount = 0;
	/**
	 * save data total count
	 * @param count
	 */
	protected void addTotalCount(int count) {
		totalCount += count;
	}
	/**
	 * 전체 저장 개수를 조회하는 메소드.
	 * saveAction 으로 처리된 개수이다.
	 * isValidationObject
	 * 		true 이면, 저장객체
	 * 		false 이면, skip 처리하도록 구현한다.
	 * 
	 * @return
	 */
	@Override
	public int getTotalCount() {
		return this.totalCount;
	}
	
	
	private Map<String, String> rowDataMap = new HashMap<>();
	protected Map<String, String> getRowDataMap() {
		return rowDataMap;
	}
	@Override
	public void startRow(int rowNum) {
		rowDataMap.clear();		
	}
	

	@Override
	public void cell(String cellReference, String formattedValue, XSSFComment comment) {
		String col = getColumnName(cellReference);
		if ( col != null ) {
			if ( formattedValue != null ) {
				formattedValue = formattedValue.trim();
			}
			rowDataMap.put(col.trim(), formattedValue);
		}
	}
	

	static Pattern columnNamePattern = Pattern.compile("(\\D+)(\\d+)");
	protected String getColumnName(String columnName) {
		if ( columnName != null && columnName.length() > 1 ) {
			Matcher matcher = columnNamePattern.matcher(columnName);
			try {
				if (matcher.matches()) {
					return matcher.group(1).toUpperCase();
				}
			} catch (Exception e ) {
				log.error("columnName={}", columnName);
			} finally {
				matcher = null;
			}
		}
		return null;
	}
	
	
	protected String getColumnRefName(int thisRow, int thisColumn) {
		char startChar = 'A';
		return (char)(startChar+thisColumn)+String.valueOf(thisRow);
	}


	/**
	 * 읽어들인 row 가 유효한 row 인지 판단하여 결과를 리턴한다.
	 * 이 메소드를 업무단으로 구현하여 필수여부를 판단하거나, 데이터의 유효성을 검증하여, Exception 을 유발하면 중단되도록 구현한다.
	 * 
	 * @param row
	 * @return true 이며, 저장처리를 하도록 하고, false 이면 skip 처리를 한다.
	 */
	protected abstract boolean isValidationObject(E row); // 단위 row 가  객체로 공백 row 등을 skip 처리.
}
