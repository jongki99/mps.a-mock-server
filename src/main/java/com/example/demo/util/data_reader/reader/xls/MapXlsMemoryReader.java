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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;

import com.example.demo.util.data_reader.reader.AbsDataFileReader;
import com.example.demo.util.data_reader.sample.base.SaveActionUtil;
import com.example.demo.util.data_reader.sample.test.TestLocalReader;

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
public class MapXlsMemoryReader<E> extends AbsDataFileReader<Map<String, String>> {

	public MapXlsMemoryReader(int saveRowSize) {
		super(saveRowSize); // 데이터 저장 처리 단위.
	}
	
	
	/** saveRowSize 만큼 메모리에 담아둔다. 이를 saveAction 에서 처리한다. */
	protected List<Map<String, String>> rows = new ArrayList<>();	//실제 엑셀을 파싱해서 담아지는 데이터
	
	/** 메모리로 올린 엑셀 */
	private HSSFWorkbook workbook;

	private boolean isAll;
	
	
	@Override
	public void endRow(int rowNum) {
		// Map 으로 변환 처리.
		if ( isValidationObject(getRowDataMap()) ) {
//			log.debug("{}", getRowDataMap());
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
	

	/**
	 * 이 메소드에서 데이터를 read 처리하는 소스를 override 해서 구현하면 됨.
	 * 여기서는 샘플로 개수만 출력하도록 구현함.
	 * 
	 * 구현부에 따라 다르겠지만... 보통 not null 이고, not empty 이다.
	 */
	@Override
	public void saveAction(List<Map<String, String>> rows) {
		SaveActionUtil.saveAction(rows);
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
		this.close("xls workbook", workbook);
	}
	

	@Override
	public void readData(InputStream inputStream) throws IOException {
		readData(inputStream, false);
	}
	@Override
	public void readData(InputStream inputStream, boolean isAll) throws IOException {
		this.isAll = isAll;
		this.workbook = new HSSFWorkbook(inputStream);
	}
	

	@Override
	public void parse() throws IOException {
		if ( this.isAll ) {
			Iterator<Sheet> sheetIterator = workbook.sheetIterator();
			while (sheetIterator.hasNext()) {
				parseSheet(sheetIterator.next());
			}
		} else {
			Sheet worksheet = workbook.getSheetAt(0); // 첫번째 시트만..
			parseSheet(worksheet);
		}
	}
	
	
	/**
	 * 엑셀 파일을 읽어서 처리하는 것까지 진행함.
	 * @param worksheet
	 */
	public void parseSheet(Sheet worksheet) {
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
		TestLocalReader.mainMapXlsMemoryReader(args);
	}
	
	
}