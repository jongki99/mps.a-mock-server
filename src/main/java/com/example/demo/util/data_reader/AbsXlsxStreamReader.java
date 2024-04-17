package com.example.demo.util.data_reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
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
public abstract class AbsXlsxStreamReader<E> extends AbsDataFileReader<E> {

	protected AbsXlsxStreamReader() {
		this(10000);
	};
	
	/**
	 * @param saveActionSize saveAction 메소드 호출 되는 객체 개수 사이즈. 한번에 저장처리할 객체 개수 단위 지정.
	 */
	protected AbsXlsxStreamReader(int saveActionSize) {
		super(saveActionSize);
	};
	
	
	protected List<E> rows = new ArrayList<>();	//실제 엑셀을 파싱해서 담아지는 데이터
	private OPCPackage opc;
	private InputStream inputStream;
	private boolean isAll;

	
	/**
	 * 한 열의 파싱이 끝나는 시점에서 후처리를 위해 구현한다.
	 * list 처리를 하던지, row 단위에서 카운트를 이용해서 단위 데이터당 작업을 하려는 경우, 여기서 처리를 한다.
	 */
	@Override
	public abstract void endRow(int rowNum);
	
	protected void addRow(E newObject) {
		rows.add(newObject);
		addTotalCount(1);
		if ( rows.size() % getActionRowSize() == 0 ) {
			if (rows != null && rows.size() > 0) {
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
		log.debug("totalCounted={}", getTotalCount());
	}
	
	private AbsXlsxStreamReader<E> readExcel(InputStream inputStream) throws IOException {
		this.inputStream = inputStream;
		try {
			opc = OPCPackage.open(inputStream);
		} catch (Exception e) {
			log.debug("", e);
			//에러 발생했을때
		}

		return this;
	}
	

	@Override
	public void close() throws IOException {
		if ( this.opc != null ) {
			try {
				this.opc.close();
			} catch (Exception e) {
				log.error("excel file close error:", e);
			}
		}
		if ( this.inputStream != null ) {
			try {
				this.inputStream.close();
			} catch (Exception e) {
				log.error("excel inputStream close error:", e);
			}
		}
	}

	@Override
	public void readData(InputStream inputStream, boolean isAll) throws IOException {
		this.isAll = isAll;
		this.readExcel(inputStream);
	}

	@Override
	public void readData(InputStream inputStream) throws IOException {
		this.isAll = false;
		this.readExcel(inputStream);
	}

	@Override
	public void parse() throws IOException {

		try {
			XSSFReader xssfReader = new XSSFReader(opc);
			StylesTable styles = xssfReader.getStylesTable();
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			ContentHandler handle = new XSSFSheetXMLHandler(styles, strings, this, false);

			//엑셀의 시트를 하나만 가져오기.
			//여러개일경우 iter문으로 추출해야 함. (iter문으로)
			XMLReader xmlReader = XMLHelper.newXMLReader();
			xmlReader.setContentHandler(handle);
			Iterator<InputStream> sheets = xssfReader.getSheetsData();
			
			if ( this.isAll ) {
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
			} else {
				log.debug("prev parseing");
				InputSource inputSource = new InputSource(sheets.next());
				xmlReader.parse(inputSource);
				log.debug("post parseing");
			}
		} catch (IOException | OpenXML4JException | SAXException | ParserConfigurationException e) {
			log.error("xlsx parsing error::", e);
		}
	}
}