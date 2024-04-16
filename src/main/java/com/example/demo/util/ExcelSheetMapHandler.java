package com.example.demo.util;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelSheetMapHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

	private int currentCol = -1;
	private int currRowNum = 0;

	private List<List<String>> rows = new ArrayList<>();	//실제 엑셀을 파싱해서 담아지는 데이터
	private List<String> row = new ArrayList<>();
	private List<String> header = new ArrayList<String>();
	
	public List<String> getHeader() {
		return this.header;
	}

	public static ExcelSheetMapHandler readExcel(File file) throws Exception {

		ExcelSheetMapHandler sheetHandler = new ExcelSheetMapHandler();
		try {

			OPCPackage opc = OPCPackage.open(file);
			XSSFReader xssfReader = new XSSFReader(opc);
			StylesTable styles = xssfReader.getStylesTable();
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);

			//엑셀의 시트를 하나만 가져오기.
			//여러개일경우 iter문으로 추출해야 함. (iter문으로)
			InputStream inputStream = xssfReader.getSheetsData().next();
			InputSource inputSource = new InputSource(inputStream);
			ContentHandler handle = new XSSFSheetXMLHandler(styles, strings, sheetHandler, false);

//			XMLReader xmlReader = SAXHelper.newXMLReader();
			XMLReader xmlReader = XMLHelper.newXMLReader();
			xmlReader.setContentHandler(handle);
			
			xmlReader.parse(inputSource);
			inputStream.close();
			opc.close();
		} catch (Exception e) {
			log.debug("", e);
			//에러 발생했을때
		}

		return sheetHandler;
	}

	public List<List<String>> getRows() {
		return rows;
	}

	@Override
	public void startRow(int arg0) {
		this.currentCol = -1;
		this.currRowNum = arg0;
	}

	@Override
	public void cell(String columnName, String value, XSSFComment var3) {
		int iCol = (new CellReference(columnName)).getCol();
		int emptyCol = iCol - currentCol - 1;

		for (int i = 0; i < emptyCol; i++) {
			row.add("");
		}
		currentCol = iCol;
		row.add(value);
	}

	@Override
	public void headerFooter(String arg0, boolean arg1, String arg2) {
		//사용 X
		log.debug("arg0={}", arg0);
		log.debug("arg1={}", arg1);
		log.debug("arg2={}", arg2);
	}

	private int dataRowCount = 0;
	@Override
	public void endRow(int rowNum) {
//		if (rowNum == 0) {
//			header = new ArrayList(row);
//		} else {
			if (row.size() < header.size()) {
				for (int i = row.size(); i < header.size(); i++) {
					row.add("");
				}
			}
			rows.add(new ArrayList<>(row));
//		}
		row.clear();
		if ( ++dataRowCount % 10000 == 0 ) {
			log.debug("dataRowCount={}", dataRowCount);
			System.out.println("dataRowCount="+dataRowCount);
			rows.clear();
		}
	}

	public void hyperlinkCell(String arg0, String arg1, String arg2, String arg3, XSSFComment arg4) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) throws Exception {
		// 엑셀 데이터 양식 example
		/*    A열				B열
		1행   nero@nate.com		Seoul
		2행   jijeon@gmail.com	Busan
		3행   jy.jeon@naver.com	Jeju
		*/

		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
//		String filePath = "/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns.xlsx";
//		String filePath = "/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns_20.xlsx";
//		String filePath = "/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20.xlsx";
		
//		String filePath = "/Users/jyjeon/Downloads/정의서/복사본.xlsx";
		List<String> filePaths = new ArrayList<>();
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns.xlsx");
//		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_20.xlsx");
//		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20.xlsx");
		
		filePaths.forEach(filePath -> {
			log.debug(filePath);
			
			File file = new File(filePath);
			
			ExcelSheetMapHandler excelSheetHandler;
			try {
				excelSheetHandler = ExcelSheetMapHandler.readExcel(file);
				
				// excelDatas >>> [[nero@nate.com, Seoul], [jijeon@gmail.com, Busan], [jy.jeon@naver.com, Jeju]]
//				List<List<String>> excelDatas = excelSheetHandler.getRows();
//				int dataRowCount = 0;
//				for(List<String> dataRow : excelDatas) { // row 하나를 읽어온다.
//					if ( ++dataRowCount % 100000 == 0 ) {
//						log.debug("dataRowCount={}", dataRowCount);
//					}
//					
//					for(String str : dataRow){ // cell 하나를 읽어온다.
////						System.out.println(str);
//					}
//				}
			} catch (Exception e) {
				log.error("", e);
			}
		});
		Thread.sleep(10_000);
	}

}