package com.example.demo.util.data_reader.reader;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>xlsx, xls, csv 파일을 읽어서 특정 단위로 saveAction 처리할 수 있도록 인터페이스를 제공한다.
 * 
 * 인터페이스 기능 제공을 위한 util 성 클래스로 기본 기능을 제공한다.
 * 아직 인터페이스에 가깝지만... 데이터 reader 처리를 위한 기본 기능을 구현함.
 * 
 * 하위 구현체에서는 각 reader 를 추상 클래스로 구현해서 업무 전단계를 구성해서 업무 단 로직을 최소화한다.
 * </pre>
 * 
 * @param <E>
 */
@Slf4j
public abstract class AbsDataFileReader<E> implements SheetContentsHandler, DataFileReader<E> {

	public AbsDataFileReader(int saveRowSize) {
		this.saveActionSize = saveRowSize; // 모든 데이터를 출력.
	}
	/**
	 * 보통 배치성이고, DB 응답이 10초 제한이 있어서, 빠른 응답이 가능한 개수로 기본값을 설정함.
	 * 기본값을 설정하도록 업무 클래스를 만들어서 사용하면 되므로 상관이 없음.
	 */
	private int saveActionSize = 1000; // 10000개 단위로 saveAction 호출.
	protected int getActionRowSize() {
		return saveActionSize;
	}


	/** 전체 처리 개수, 구현체에서 호출해줘야 한다. */
	private int totalCount = 0;
	/**
	 * save data total count
	 * 
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
	

	/**
	 * 셀명으로 데이터가 들어오면... ex(A1) A 로 컬럼명을 분리해서 사용자가 참조할 수 있도록 한다.
	 * row 단위로 처리하기전에 각 row 의 컬럼 분류작업 처리.
	 */
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
	
	
	/**
	 * 현재 컬럼 index 값을 기준으로.. 컬럼명을 구성하는 처리.
	 * xls 형식에서 참조를 index 로 처리하므로, 이를 인터페이스에 맞게 맞춰주는 작업.
	 * 
	 * 기본 구성을 xlsx 구현체의 interface 를 사용하므로, 이렇게 만들게 되었다.
	 * 
	 * @param thisRow
	 * @param thisColumn
	 * @return
	 */
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
	
	
	protected void close(String logName, Closeable object) {
		closeObject(logName, object);
	}
	
	
	public static void closeObject(String logName, Closeable object) {
		if ( object != null ) {
			try {
				object.close();
			} catch (Exception e) {
				log.error("Closeable object = {} is close error::", logName, e);
			}
		}
	}
}
