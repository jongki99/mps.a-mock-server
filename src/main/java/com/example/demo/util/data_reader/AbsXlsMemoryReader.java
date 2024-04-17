package com.example.demo.util.data_reader;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.global.util.StringUtil;

/**
 * 일단 이건 xls 파일이 잘 안 읽어지거나 하면 대안으로써 이전 소스 기준으로 만들어 둔다.
 * xls 을 메모리로 올려서 row 단위로 ASIS 처럼 처리하면서 공통 인터페이스를 구현해서 해당 구현을 지원해서 서비스 업무를 단순화 하기 위해서 구현한 구현체다.
 * 감안해서 사용하면 되겠다.
 * 
 * 5000건 짜리 10M 정도 필요함.row, column 수 등에 따라서 달라지겠지만...
 * 거기다가 엑셀파일의 기타 등등 잡다한 정보를 포함하면 건수와는 다른게 더 많이 잡힐수 있음.
 * 
 * @param <E>
 */
@Slf4j
public class AbsXlsMemoryReader<E> extends AbsDataFileReader<Map<String, String>> {

	public AbsXlsMemoryReader(int saveRowSize) {
		super(saveRowSize); // 데이터 저장 처리 단위.
	}
	
	
	/** saveRowSize 만큼 메모리에 담아둔다. 이를 saveAction 에서 처리한다. */
	protected List<Map<String, String>> rows = new ArrayList<>();	//실제 엑셀을 파싱해서 담아지는 데이터
	
	/** inputStream 닫아 주기 위해서 일단 담아두는데... */
	private InputStream inputStream;

	/** 메모리로 올린 엑셀 */
	private HSSFWorkbook workbook;

	private boolean isAll;
	
	
	@Override
	public void endRow(int rowNum) {
		// Map 으로 변환 처리.
		if ( isValidationObject(getRowDataMap()) ) {
			log.debug("{}", getRowDataMap());
			rows.add(new HashMap<>(getRowDataMap()));
			addTotalCount(1);
			if ( rows.size() % getActionRowSize() == 0 ) {
				if (rows != null && rows.size() > 0) {
					saveAction(rows);
				}
				rows.clear();
			}
		}
	}
	

	@Override
	public void endSheet() {
		// endRow 후 잔여 list 처리.
		if (rows != null && rows.size() > 0) {
			saveAction(rows);
		}
		rows.clear();
		log.debug("totalCount={}", getTotalCount());
	}
	

	@Override
	public void saveAction(List<Map<String, String>> rows) {
		log.debug("saveAction rows.size={}", rows.size());
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
		if ( workbook != null ) {
			try {
				workbook.close();
			} catch (Exception e) {
				// error skip
				log.error("xls workbook close error:", e);
			}
		}
		if ( inputStream != null ) {
			try {
				inputStream.close();
			} catch (Exception e) {
				// error skip
				log.error("xls inputStream close error:", e);
			}
		}
	}
	

	@Override
	public void readData(InputStream inputStream) throws IOException {
		this.inputStream = inputStream;
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		this.workbook = workbook;
		this.isAll = false;
	}
	

	@Override
	public void readData(InputStream inputStream, boolean isAll) throws IOException {
		this.inputStream = inputStream;
		this.isAll = isAll;
	}
	

	@Override
	public void parse() throws IOException {
		if ( this.isAll ) {
			Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			while (sheetIterator.hasNext()) {
				parse(sheetIterator.next());
			}
		} else {
			Sheet worksheet = workbook.getSheetAt(0); // 첫번째 시트
			parse(worksheet);
		}
	}
	
	
	public void parse(Sheet worksheet) {
		int rows = worksheet.getPhysicalNumberOfRows(); // 해당시트의 row수
		for(int i = 0; i < rows; i++) { // row=0은 데이터 : 해더없음 
			Row row = worksheet.getRow(i);
			if(row != null) {
				startRow(i);
				int cells = row.getPhysicalNumberOfCells(); // 한 row당 cell수
				
				for (int j=0; j<cells; j++) {
					Cell cell = row.getCell(j); // 셀에 담겨있는 값을 읽는다.
					if(cell != null) {
						String value = "";
						if(cell.getCellType() == CellType.NUMERIC) {
							// value = cell.getNumericCellValue() + "";
							value = NumberToTextConverter.toText(cell.getNumericCellValue());
						} else {
							value = cell.getStringCellValue();
						}
						cell(cell.getAddress().formatAsString(), value, null);
					}
				}
				endRow(i);
			}
		}
		endSheet();
	}
	

	public static void main(String[] args) throws Exception {
		main21(args);
	}
	public static void main21(String[] args) throws Exception {
		final int rowsSize = 1000; // 약 7초 걸림. 메모리를 안쓰면 더 빠르네?
		
		List<String> filePaths = new ArrayList<>();
		// filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample/test_20-2.xlsx");
//		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample_xls_down/file_example_XLS_10.xls");
		filePaths.add("/Users/P170355/Downloads/work-down/work-dev/8933-cpnPinUp/sample_xls_down/file_example_XLS_5000.xls"); // 0.3초만에 다 읽어버리네... 260 M 까지... 순간적으로는 거의 동일하게 올라가는듯.. 메모리 제한을 걸고 테스트. 데이터가 더 많아야... ㅜ.
		// 5000건 -Xmx9m 까지 동작을 하네... 8m 부터는 OOM

		filePaths.forEach(filePath -> {
			log.debug(filePath);
			try (
					AbsXlsMemoryReader<Map<String, String>> xls2csv = new AbsXlsMemoryReader<>(rowsSize);
			) {
				xls2csv.readData(new FileInputStream(filePath));
				xls2csv.parse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Thread.sleep(5000);
	}

}