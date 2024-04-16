package com.example.demo.util.excel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import lombok.extern.slf4j.Slf4j;

/**
 * xlsx 파일 형식인 경우에 해당 클래스를 상속해서 cell 단위로 처리해서 사용한다.
 * 업무당 개별 파일을 만들어서 처리하면 된다.
 * 
 * 기본 대용량 처리를 위해서 첫째줄 부터 읽어서 처리하는 기본 기능.
 * cell, endRow 를 커스터마이징 해서 쓰면 되도록 abstract 로 기능 제공.
 * 
 * 해당 클래스를 extends 해서 reader 구현재로 xlsx 파일과 매핑해서 쓸수 있도록 한다.
 * 아래 3개 메소드를 구현해서 기본 동작을 처리한다. ( 보통 작업을 끝내도록 한다. 대용량이니까. DB insert 까지 또는 list 로 만들어서 반환. )
 * 
 * startRow 로 row 작업전에 객체초기화 작업을 한다.
 * cell 로 초기화된 객체에 값을 설정한다.
 * endRow 로 객체를 list 혹은 DB 에 처리한다. ( 대용량을 다룰때 메모리 올리지 않을거면 여기서 단위처리를 해주던가 한다. )
 * 
 * 수식도 처리해주지만... 대용량일때 느려질수 있다. 순차적으로 처리하지만.. 수식일 경우, 별도 처리해줄 테니...
 * 일단 없는 것으로만 테스트...
 */
@Slf4j
public abstract class AbsXlsxReader<E> implements SheetContentsHandler {

	protected AbsXlsxReader() {
		this(10000);
	};
	
	/**
	 * @param saveActionSize saveAction 메소드 호출 되는 객체 개수 사이즈. 한번에 저장처리할 객체 개수 단위 지정.
	 */
	protected AbsXlsxReader(int saveActionSize) {
		this.saveActionSize = saveActionSize;
	};
	
	private Map<String, String> rowDataMap = new HashMap<>();

	protected Map<String, String> getRowDataMap() {
		return rowDataMap;
	}
	
	/**
	 * 컬럼 단위로 데이터 처리를 하기 위해서 컬럼 기준으로 데이터 작업을 해준다.
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
	
	protected List<E> rows = new ArrayList<>();	//실제 엑셀을 파싱해서 담아지는 데이터
	
	private int saveActionSize; // 10000개 단위로 saveAction 호출.
	
	protected int getActionRowSize() {
		return saveActionSize;
	}
	
	/** 전체 처리 개수 카운트 하위에서 호출해줘야 한다. */
	protected int totalCounted = 0;
	
	public int getTotalCounted() {
		return totalCounted;
	}
	
	/**
	 * save data total count
	 * @param count
	 */
	protected void addTotalCount(int count) {
		totalCounted += count;
	}
	
	static Pattern columnNamePattern = Pattern.compile("(\\D+)(\\d+)");
	
	/**
	 * 객체를 검증해서 해당 row 를 skip 할지 여부를 판단한다.
	 * 공백 row 가 있는 경우, 해당 row 를 무시하기 위한 처리를 담당한다.
	 * 
	 * @param newObject 검증할 객체. 값을 설정했는데.. 특정 필드의 필수값이 있어야 할때... 체크.
	 * @return true 면 저장대상으로 집계, false 면 해당 row 는 skip.
	 */
	protected abstract boolean isValidationObject(E newObject);
	
	/**
	 * 한 열이 시작하는 시점에서 전처리를 하고 싶은 경우 구현한다.
	 * vo 를 사용하는 경우에는 여기서 객체를 초기화해준다.
	 * 
	 * 하위에서 처리하는 경우가 별로 없어서 기본 처리하였다.
	 */
	@Override
	public void startRow(int rowNum) {
		rowDataMap.clear();
	};
	
	/**
	 * 한 열의 파싱이 끝나는 시점에서 후처리를 위해 구현한다.
	 * list 처리를 하던지, row 단위에서 카운트를 이용해서 단위 데이터당 작업을 하려는 경우, 여기서 처리를 한다.
	 */
	@Override
	public abstract void endRow(int rowNum);
	
	public void addRow(E newObject) {
		rows.add(newObject);
		if ( rows.size() % getActionRowSize() == 0 ) {
			if (rows != null && rows.size() > 0) {
				addTotalCount(rows.size());
				saveAction(rows);
			}
			rows.clear();
		}

	}
	
	/**
	 * 시트가 끝나는 시점을 알려준다. 여러 시트가 있는 경우, 계속 돌것인지, 후처리를 어떻게 할지 결정하면 될듯하다.
	 * 보통은 여러시트 읽기 처리를 따로 하므로 꼭 필요한 것은 아니다.
	 * 
	 * 나머지 개수 처리를 위해서 시트 종료 시점에 호출.
	 */
	@Override
	public void endSheet() {
		// endRow 후 잔여 list 처리.
		if (rows != null && rows.size() > 0) {
			saveAction(rows);
		}
		rows.clear();
		log.debug("totalCounted={}", totalCounted);
	}
	
	public abstract void saveAction(List<E> rows);
	
	private AbsXlsxReader<E> readExcel(File file, boolean onlyFirst) throws Exception {
		OPCPackage opc = null;
		InputStream inputStream = null;
		try {
			opc = OPCPackage.open(file);
			XSSFReader xssfReader = new XSSFReader(opc);
			StylesTable styles = xssfReader.getStylesTable();
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			ContentHandler handle = new XSSFSheetXMLHandler(styles, strings, this, false);

			//엑셀의 시트를 하나만 가져오기.
			//여러개일경우 iter문으로 추출해야 함. (iter문으로)
			XMLReader xmlReader = XMLHelper.newXMLReader();
			xmlReader.setContentHandler(handle);
			Iterator<InputStream> sheets = xssfReader.getSheetsData();
			
			if ( onlyFirst ) {
				log.debug("prev parseing");
				InputSource inputSource = new InputSource(sheets.next());
				xmlReader.parse(inputSource);
				log.debug("post parseing");
			} else {
				while(sheets.hasNext()) {
					InputStream sheet = sheets.next();
					log.debug("Processing new sheet::");
					try {
						InputSource sheetSource = new InputSource(sheet);
						log.debug("prev parseing");
						xmlReader.parse(sheetSource);
						log.debug("post parseing");
					} finally {
						sheet.close();
					}
				}
			}

		} catch (Exception e) {
			log.debug("", e);
			//에러 발생했을때
		} finally {
			if ( inputStream != null ) {
				inputStream.close();
			}
			if ( opc != null ) {
				opc.close();
			}
		}

		return this;
	}
	
	/**
	 * 첫번째 시트만 읽어서 처리하는 ...
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public AbsXlsxReader<E> readExcel(File file) throws Exception {
		boolean onlyFirst = true; // 첫째 sheet 만 read.
		return readExcel(file, onlyFirst);
	}
	
	/**
	 * 다중 시트로 더 많은 데이터? 일단 편집도 쉽지 않다.
	 * 대용량 다중 시트 만들지는 못하고, sheet 2개로 정상 동작 테스트만...
	 * 
	 * 대용량 엑실은.. * 맥 numbers 는 계속 멈춰있다.내 메모리가 얼만데... 아니 이정도도 처리 못할 프로그램이라니... 그래서 공짜인거겠지...
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public AbsXlsxReader<E> readExcelAll(File file) throws Exception {
		boolean onlyFirst = false; // 모든 sheet read.
		return readExcel(file, onlyFirst);
	}
	
	/**
	 * 엑셀 컬럼 읽을때 컬럼별로 분류하기 위한 처리.
	 * 
	 * @param columnName
	 * @return
	 */
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
}
