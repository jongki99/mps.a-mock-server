package com.example.demo.util.data_reader.reader.xls;
/* ====================================================================
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==================================================================== */

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingRowDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.example.demo.util.data_reader.reader.AbsDataFileReader;
import com.example.demo.util.data_reader.sample.reader.TestLocalReader;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.global.util.StringUtil;

/**
* 일단 잘 동작하는거 같음. // 2024-04-17 (수)
*/
@Slf4j
public class MapXlsStreamReader extends AbsDataFileReader<Map<String, String>> implements HSSFListener {
	
	private int minColumns;
	private POIFSFileSystem fs;
//	private final PrintStream output;
	
	private int lastRowNumber;
	private int lastColumnNumber;
	
	/** Should we output the formula, or the value it has? */
	private final boolean outputFormulaValues = true;
	
	/** For parsing Formulas */
	private SheetRecordCollectingListener workbookBuildingListener;
	private HSSFWorkbook stubWorkbook;
	
	// Records we pick up as we process
	private SSTRecord sstRecord;
	private FormatTrackingHSSFListener formatListener;
	
	/** So we known which sheet we're on */
	// 일단 사용안하는데... 되려나? ㅜ.ㅜ // TODOKJK : xls 파일 다중 시트의 경우, 테스트 필요.모든 시트 잘 읽는지.. 이벤트는 잘 되는지 등등..
	@SuppressWarnings("unused")
	private int sheetIndex = -1;
	private BoundSheetRecord[] orderedBSRs;
	private final List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();
	
	// For handling formulas with string results
	private int nextRow;
	private int nextColumn;
	private boolean outputNextStringRecord;
	
	
	/**
	 * Creates a new XLS -&gt; CSV converter
	 * 
	 * @param fs         The POIFSFileSystem to process
	 * @param output     The PrintStream to output the CSV to
	 * @param minColumns The minimum number of columns to output, or -1 for no
	 *                   minimum
	 * @param saveRowSize 
	 */
	public MapXlsStreamReader(POIFSFileSystem fs, PrintStream output, int minColumns, int saveRowSize) {
		super(saveRowSize); // 데이터 저장 처리 단위.
		this.fs = fs;
//		this.output = output;
		this.minColumns = minColumns;
	}
	
	
	/**
	 * Creates a new XLS -&gt; CSV converter
	 * 
	 * @param filename   The file to process
	 * @param minColumns The minimum number of columns to output, or -1 for no
	 *                   minimum
	 *
	 * @throws IOException if the file cannot be read or parsing the file fails
	 */
	public MapXlsStreamReader(InputStream inputStream, int minColumns, int saveRowSize) throws IOException {
		this(new POIFSFileSystem(inputStream), System.out, minColumns, saveRowSize);
	}
	
	
	public MapXlsStreamReader(int saveRowSize) {
		super(saveRowSize); // 데이터 저장 처리 단위.
	}
	
	
	/**
	 * Initiates the processing of the XLS file to CSV
	 *
	 * @throws IOException if the workbook contained errors
	 */
	public void process() throws IOException {
		MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
		formatListener = new FormatTrackingHSSFListener(listener);

		HSSFEventFactory factory = new HSSFEventFactory();
		HSSFRequest request = new HSSFRequest();

		if (outputFormulaValues) {
			request.addListenerForAllRecords(formatListener);
		} else {
			workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
			request.addListenerForAllRecords(workbookBuildingListener);
		}

		factory.processWorkbookEvents(request, fs);
	}
	

	/**
	 * Main HSSFListener method, processes events, and outputs the CSV as the file is processed.
	 */
	@Override
	public void processRecord(org.apache.poi.hssf.record.Record record) {
		int thisRow = -1;
		int thisColumn = -1;
		String thisStr = null;

		switch (record.getSid()) {
		case EOFRecord.sid: // EOF 파일의 끝이라...
//			log.debug("EOFRecord.sid={}", EOFRecord.sid);
			// 파일의 끝이니까 시트가 종료된걸로... 인지...
			// 테스트 해봐야...
			endSheet();
			break;
		case BoundSheetRecord.sid: // 시트의 경, 시작부분이라고 봐도 되겠지. 첫번째 시트를 넣고, 두번째를 넣고...
			boundSheetRecords.add((BoundSheetRecord) record);
			if ( boundSheetRecords.size() > 1 ) {
				// 두번째 시트 시작이니까 첫번째 시트 종료된걸로... 인지...
				// 테스트 해봐야...
				endSheet();
			}
			break;
		case BOFRecord.sid: // 시트의 시작을 구분... BOFRecord.TYPE_WORKSHEET workSheet 인 경우만? 다른것도 있나?
			BOFRecord br = (BOFRecord) record;
			if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
				// Create sub workbook if required
				if (workbookBuildingListener != null && stubWorkbook == null) {
					stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
				}

				// Output the worksheet name
				// Works by ordering the BSRs by the location of
				// their BOFRecords, and then knowing that we
				// process BOFRecords in byte offset order
				sheetIndex++;
				if (orderedBSRs == null) {
					orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
				}
//				output.println(); // 개행을 한다고??? 왜 그러지? 결과물을 봐야 되겠구만... xls 파일좀...
//				output.println(orderedBSRs[sheetIndex].getSheetname() + " [" + (sheetIndex + 1) + "]:");
			}
			break;

		case SSTRecord.sid:
			sstRecord = (SSTRecord) record;
			break;

		case BlankRecord.sid:
			BlankRecord brec = (BlankRecord) record;

			thisRow = brec.getRow();
			thisColumn = brec.getColumn();
			thisStr = "";
			break;
		case BoolErrRecord.sid:
			BoolErrRecord berec = (BoolErrRecord) record;

			thisRow = berec.getRow();
			thisColumn = berec.getColumn();
			thisStr = "";
			break;

		case FormulaRecord.sid:
			FormulaRecord frec = (FormulaRecord) record;

			thisRow = frec.getRow();
			thisColumn = frec.getColumn();

			if (outputFormulaValues) {
				if (Double.isNaN(frec.getValue())) {
					// Formula result is a string
					// This is stored in the next record
					outputNextStringRecord = true;
					nextRow = frec.getRow();
					nextColumn = frec.getColumn();
				} else {
					thisStr = formatListener.formatNumberDateCell(frec);
				}
			} else {
				thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
//				thisStr = HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression());
			}
			break;
		case StringRecord.sid:
			if (outputNextStringRecord) {
				// String for formula
				StringRecord srec = (StringRecord) record;
				thisStr = srec.getString();
				thisRow = nextRow;
				thisColumn = nextColumn;
				outputNextStringRecord = false;
			}
			break;

		case LabelRecord.sid:
			LabelRecord lrec = (LabelRecord) record;

			thisRow = lrec.getRow();
			thisColumn = lrec.getColumn();
			thisStr = '"' + lrec.getValue() + '"';
//			thisStr = lrec.getValue();
			break;
		case LabelSSTRecord.sid:
			LabelSSTRecord lsrec = (LabelSSTRecord) record;

			thisRow = lsrec.getRow();
			thisColumn = lsrec.getColumn();
			if (sstRecord == null) {
				thisStr = '"' + "(No SST Record, can't identify string)" + '"';
			} else {
//				thisStr = '"' + sstRecord.getString(lsrec.getSSTIndex()).toString() + '"';
				thisStr = sstRecord.getString(lsrec.getSSTIndex()).toString();
			}
			break;
		case NoteRecord.sid:
			NoteRecord nrec = (NoteRecord) record;

			thisRow = nrec.getRow();
			thisColumn = nrec.getColumn();
			// TODO: Find object to match nrec.getShapeId()
			thisStr = '"' + "(TODO)" + '"';
			break;
		case NumberRecord.sid:
			NumberRecord numrec = (NumberRecord) record;

			thisRow = numrec.getRow();
			thisColumn = numrec.getColumn();

			// Format
			thisStr = formatListener.formatNumberDateCell(numrec);
			break;
		case RKRecord.sid:
			RKRecord rkrec = (RKRecord) record;

			thisRow = rkrec.getRow();
			thisColumn = rkrec.getColumn();
			thisStr = '"' + "(TODO)" + '"';
			break;
		default:
			break;
		}

		// Handle new row
		if (thisRow != -1 && thisRow != lastRowNumber) {
			lastColumnNumber = -1;
			startRow(lastRowNumber); // 수식과 연관이 있을 듯한데.. thisRow 또는 lastRowNumber 로 처리해야 될듯... 샘플이...
		}
		
		// Handle endSheet
		// log.debug("{}", record);
		if ( record instanceof MissingRowDummyRecord) {
			log.debug("{}", record);
			endSheet();
		}

		// Handle missing column
		if (record instanceof MissingCellDummyRecord) {
			MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
			thisRow = mc.getRow();
			thisColumn = mc.getColumn();
			thisStr = "";
		}

		// If we got something to print out, do so
		if (thisStr != null) {
//			if (thisColumn > 0) {
//				output.print(',');
//			}
//			output.print(thisStr);
			cell(getColumnRefName(thisRow, thisColumn), thisStr, null);
		}

		// Update column and row count
		if (thisRow > -1)
			lastRowNumber = thisRow;
		if (thisColumn > -1)
			lastColumnNumber = thisColumn;

		// Handle end of row
		if (record instanceof LastCellOfRowDummyRecord) {
			// Print out any missing commas if needed
			if (minColumns > 0) {
				// Columns are 0 based
				if (lastColumnNumber == -1) {
					lastColumnNumber = 0;
				}
//				for (int i = lastColumnNumber; i < (minColumns); i++) {
//					output.print(',');
//				}
			}

			// We're onto a new row
			lastColumnNumber = -1;

			// End the row
			// output.println();
			endRow(lastRowNumber);
		}
	}
	

	protected List<Map<String, String>> rows = new ArrayList<>();	//실제 엑셀을 파싱해서 담아지는 데이터
	

//	public abstract void endRow(int rowNum);
	
		
	@Override
	public void endRow(int rowNum) {
		// Map 으로 변환 처리.
		if ( isValidationObject(getRowDataMap()) ) {
			if(rowNum== 5000) {
				log.debug("{}", getRowDataMap());
			}
			rows.add(new HashMap<>(getRowDataMap()));
			addTotalCount(1);
			if ( rows.size() % getActionRowSize() == 0 ) {
				if ( CollectionUtils.isNotEmpty(rows) ) {
					saveAction(rows);
					rows.clear();
				}
			}
		}
	}
	

	@Override
	public void endSheet() {
		// endRow 후 잔여 list 처리.
		if ( CollectionUtils.isNotEmpty(rows) ) {
			saveAction(rows);
			rows.clear();
		}
		log.debug("totalCount={}", getTotalCount());
	}
	
	
	@Override
	public void saveAction(List<Map<String, String>> rows) {
		if ( CollectionUtils.isNotEmpty(rows) ) {
			log.debug("saveAction rows.size={}", rows.size());
		}
	}
	
	
	@Override
	public boolean isValidationObject(Map<String, String> row) {
		if ( StringUtil.isBlank(row.get("A")) ) {
			return false;
		}
		
		// 정상이다.
		return true;
	}
	
	
	/**
	 * AutoCloseable 처리.
	 */
	@Override
	public void close() throws IOException {
		this.close("xls stubWorkbook", stubWorkbook);
		this.close("xls fs", fs);
	}
	
	
	@Override
	public void readData(InputStream inputStream) throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem(inputStream);
		this.fs = fs;
		this.minColumns = -1;
	}
	
	
	@Override
	public void readData(InputStream inputStream, boolean isAll) throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem(inputStream);
		this.fs = fs;
		this.minColumns = -1;
	}
	
	
	@Override
	public void parse() throws IOException {
		process();
	}
	
	
	public static void main(String[] args) throws Exception {
		TestLocalReader.mainMapXlsStreamReader(args);
	}

}