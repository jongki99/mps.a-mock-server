package com.example.demo.util.excel_read_ref;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
 * 이걸로 해야 되는데... map 으로 하려고 했는데...
 * 다시 시도해 봐야 겠다. 컬럼을 미리 지정해서 할 필요는 없기는 한데...
 * list index 도 복잡하고... 일단 스킵.
 */
@Slf4j
public class ExcelSheetHandler implements SheetContentsHandler {

	private List<Map<String, String>> rows = new ArrayList<>();	//실제 엑셀을 파싱해서 담아지는 데이터
	private Map<String, String> row = new HashMap<>();
	private List<String> header = new ArrayList<String>();
	
	public List<String> getHeader() {
		return this.header;
	}

	public static ExcelSheetHandler readExcel(File file) throws Exception {

		ExcelSheetHandler sheetHandler = new ExcelSheetHandler();
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

	public List<Map<String, String>> getRows() {
		return rows;
	}

	/**
	 * row 읽기 시작될때 초기화 호출.
	 */
	@Override
	public void startRow(int arg0) {
	}

	@Override
	public void cell(String columnName, String value, XSSFComment var3) {
		String col = getColumnName(columnName);
		if ( col != null ) {
			row.put(col, value);
		}
	}
	
	Pattern columnNamePattern = Pattern.compile("(\\D+)(\\d+)");
	private int dataRowCount = 0;
	private String getColumnName(String columnName) {
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
	 * 엑셀 데이터중에 헤더/푸터 값이 있으면 이를 호출해줌.
	 * odd/even 각각 호출해주는데... 그런 형식은 일단 없는 것으로 하고 무시함.
	 */
	@Override
	public void headerFooter(String arg0, boolean arg1, String arg2) {
		//사용 X
	}

	/**
	 * 한열의 데이터를 다 읽었음을 알려주는 메소드.
	 * 해당 부분에서 한열의 처리를 마무리해주는 방식으로 처리한다.
	 */
	@Override
	public void endRow(int rowNum) {
//		if (rowNum == 0) {
//			header = new ArrayList(row);
//		} else {
//			if (row.size() < header.size()) {
//				for (int i = row.size(); i < header.size(); i++) {
//					row.add("");
//				}
//			}
			rows.add(new HashMap<>(row));
//		}
		row.clear();
		if ( ++dataRowCount % 10000 == 0 ) {
			log.debug("dataRowCount={}", dataRowCount);
			System.out.println("dataRowCount="+dataRowCount);
			rows.clear();
		}
	}

	public static void main(String[] args) throws Exception {
		// 엑셀 데이터 양식 example
		/*    A열				B열
		1행   nero@nate.com		Seoul
		2행   jijeon@gmail.com	Busan
		3행   jy.jeon@naver.com	Jeju
		*/
		

		/** 이게 문제인데... 이것 바꾸는 방법을 ... -_-;;  */
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
			
			ExcelSheetHandler excelSheetHandler;
			try {
				excelSheetHandler = ExcelSheetHandler.readExcel(file);
				
				// excelDatas >>> [[nero@nate.com, Seoul], [jijeon@gmail.com, Busan], [jy.jeon@naver.com, Jeju]]
				List<Map<String, String>> excelDatas = excelSheetHandler.getRows();
				int dataRowCount = 0;
				for(Map<String, String> dataRow : excelDatas) { // row 하나를 읽어온다.
					if ( ++dataRowCount % 10000 == 0 ) {
						log.debug("dataRowCount={}", dataRowCount);
						System.out.println("dataRowCount="+dataRowCount);
						System.out.println(dataRow.toString());
					}
				}
			} catch (Exception e) {
				log.error("", e);
			}
		});
	}

    public static void main2(String[] args) {
        String[] inputs = {"xxx123", "x1"};

        for (String input : inputs) {
            Pattern pattern = Pattern.compile("(\\D+)(\\d+)");
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches()) {
                String letters = matcher.group(1);
                String numbers = matcher.group(2);
                System.out.println("입력 문자열: " + input);
                System.out.println("문자열 부분: " + letters);
                System.out.println("숫자 부분: " + (numbers.isEmpty() ? "없음" : numbers));
            } else {
                System.out.println("일치하는 패턴이 없습니다.");
            }
        }
    }
}