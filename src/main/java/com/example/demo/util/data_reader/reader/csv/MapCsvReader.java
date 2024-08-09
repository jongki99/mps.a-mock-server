package com.example.demo.util.data_reader.reader.csv;
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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.example.demo.util.data_reader.reader.AbsDataFileReader;
import com.example.demo.util.data_reader.sample.base.SaveActionUtil;
import com.example.demo.util.data_reader.sample.test.TestLocalReader;

import lombok.extern.slf4j.Slf4j;
import skt.mno.mpai.mps.global.util.StringUtil;

/**
* CSV 파일을 읽어서 cell 단위로 읽어주는 Reader 구현체. 
*/
@Slf4j
public class MapCsvReader extends AbsDataFileReader<Map<String, String>> {
	
	
	public MapCsvReader(int saveRowSize) {
		super(saveRowSize); // 데이터 저장 처리 단위.
	}
	
	
	protected List<Map<String, String>> rows = new ArrayList<>();	//실제 엑셀을 파싱해서 담아지는 데이터
	
	
	private InputStreamReader inputStreamReader;


	private FileInputStream fileInputStream;
	
	
//	public abstract void endRow(int rowNum);
	
	
	@Override
	public void endRow(int rowNum) {
		// Map 으로 변환 처리.
		if ( isValidationObject(getRowDataMap()) ) {
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
		this.close("csv inputStreamReader", inputStreamReader);
		this.close("fileInputStream", fileInputStream);
	}
	
	
	@Override
	public void readData(InputStream inputStream) throws IOException {
		this.inputStreamReader = new InputStreamReader(inputStream);
	}
	
	
	@Override
	public void readData(InputStream inputStream, boolean isAll) throws IOException {
		this.inputStreamReader = new InputStreamReader(inputStream);
	}
	
	
	@Override
	public void readData(String filePath, boolean isAll) throws IOException {
		this.fileInputStream = new FileInputStream(filePath);
		this.inputStreamReader = new InputStreamReader(fileInputStream);
	}
	

	@Override
	public void readData(String filePath) throws IOException {
		this.fileInputStream = new FileInputStream(filePath);
		this.inputStreamReader = new InputStreamReader(fileInputStream);
	}
	
	
	private void parseObject(CSVRecord csvRow, int rowIndex) {
		if ( rowIndex % this.getActionRowSize() == 0 ) {
			log.info("consumer.sync.csv = {}", csvRow);
		}
		
		startRow(rowIndex);
		
		final AtomicInteger colCount = new AtomicInteger(0);
		csvRow.forEach(col -> {
			int colIndex = colCount.getAndIncrement();
			cell(getColumnRefName(rowIndex, colIndex), col, null);
		});
		endRow(rowIndex);
	}
	
	
	@Override
	public void parse() throws IOException {
		final AtomicInteger rowCount = new AtomicInteger(0);
		CSVFormat.DEFAULT.parse(inputStreamReader).forEach(csvRow -> parseObject( csvRow, rowCount.getAndIncrement()) );
		endSheet();
	}
	
	
	/**
	 * TSV TDF 등 같은거 겠지? 사용은 안하겠지만.. 이미 있었다.
	 * @throws IOException
	 */
	public void parseTsv() throws IOException {
		final AtomicInteger rowCount = new AtomicInteger(0);
		CSVFormat.MONGODB_TSV.parse(inputStreamReader).forEach(csvRow -> parseObject( csvRow, rowCount.getAndIncrement()) );
		endSheet();
	}
	

	public static void main(String[] args) throws Exception {
		TestLocalReader.mainMapCsvReader(args);
	}
}