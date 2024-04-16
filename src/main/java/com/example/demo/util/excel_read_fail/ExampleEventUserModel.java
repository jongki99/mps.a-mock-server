package com.example.demo.util.excel_read_fail;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * parsing 은 되는데...
 * cellType 이 s, inlineStr 으로 2개인데 다 처리가 안되는 단점이 있다.
 * 
 * 복잡하고, 약간 순수 xml 을 처리하는 느낌이라, 다른 것으로 사용.
 */
@Slf4j
public class ExampleEventUserModel {
	public void processOneSheet(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader( pkg );
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		// To look up the Sheet Name / Sheet Order / rID,
		//  you need to process the core Workbook stream.
		// Normally it's of the form rId# or rSheet#
		InputStream sheet2 = r.getSheet("rId2");
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
	}
	public void processAllSheets(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader( pkg );
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		Iterator<InputStream> sheets = r.getSheetsData();
		while(sheets.hasNext()) {
			System.out.println("Processing new sheet:\n");
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			System.out.println("");
		}
	}
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
		XMLReader parser = XMLHelper.newXMLReader();
		ContentHandler handler = new SheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}
	/**
	 * See org.xml.sax.helpers.DefaultHandler javadocs
	 */
	private static class SheetHandler extends DefaultHandler {
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;
		private boolean nextIsInlineString;
		private SheetHandler(SharedStringsTable sst) {
			this.sst = sst;
		}
		@Override
		public void startElement(String uri, String localName, String name,
								 Attributes attributes) throws SAXException {

//			log.debug("localName={}, name={}, attributes={}", localName, name, attributes);
			
			// c => cell
			if(name.equals("c")) {
				// Print the cell reference
				String cellType = attributes.getValue("t");
				System.out.print(attributes.getValue("r") + " - cellType = " + cellType);
				// Figure out if the value is an index in the SST
				if(cellType != null
						&& ( cellType.equals("s")
								|| cellType.equals("inlineStr")
								)
								) {
					nextIsString = true;
					if ( cellType.equals("inlineStr")) {
						nextIsInlineString = true;
					}
				} else {
					nextIsString = false;
					nextIsInlineString = false;
				}
			}
//			// c => cell
//			if(name.equals("c")) { // cell 읽기.
//				// Print the cell reference
////				String cellType = attributes.getValue("t");
////				System.out.print(attributes.getValue("r") + " - cellType = " + cellType);
//				// Figure out if the value is an index in the SST
//				nextIsString = true;
//			} else {
//				nextIsString = false;
//			}
			// Clear contents cache
			lastContents = "";
			System.out.println();
		}
		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
//			System.out.print("endElement :: localName = " + localName + ", - name = " + name);
			// c => cell
			if(name.equals("c")) {
				if(nextIsString) {
					int idx = Integer.parseInt(lastContents);
					lastContents = sst.getItemAt(idx).getString();
					System.out.println(lastContents);
					nextIsString = false;
				}
				// v => contents of a cell
				// Output after we've seen the string contents
				if(name.equals("v") && nextIsInlineString == false ) {
					System.out.println(lastContents);
				}
				System.out.println();
			}
		}
		@Override
		public void characters(char[] ch, int start, int length) {
			lastContents += new String(ch, start, length);
		}
	}
//	public static void main(String[] args) throws Exception {
//		ExampleEventUserModel example = new ExampleEventUserModel();
//		example.processOneSheet(args[0]);
//		example.processAllSheets(args[0]);
//	}
	public static void main(String[] args) throws Exception {
		log.debug("start");
//		String filePath = "/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns.xlsx";
		String filePath = "/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/temp_data_6_columns_20.xlsx";
//		String filePath = "/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20.xlsx";
		
//		System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		
		ExampleEventUserModel example = new ExampleEventUserModel();
//		example.processOneSheet(filePath);
		example.processAllSheets(filePath);
		log.debug("end");
	}

}