package com.example.demo.util.data_reader.sample.reader;

import java.util.List;
import java.util.Map;

import com.example.demo.util.data_reader.reader.xlsx.MapXlsxReader;
import com.example.demo.util.data_reader.sample.test.TestLocalReader;

import lombok.extern.slf4j.Slf4j;

/**
 * 샘플 MapXlsxReader 을 이용해서 Map 으로 object 를 받아서 업무 처리를 하기 위해서 사용한다.
 */
@Slf4j
public abstract class SimpleAbsXlsxMapReader extends MapXlsxReader {

	/**
	 * 생성자, 처리 단위 개수를 지정해서 사용할 수 있도록 제공.
	 * 개수만큼 saveAction 에 list 로 들어온다.
	 * 
	 * @param rowSize
	 */
	public SimpleAbsXlsxMapReader(int rowSize) {
		super(rowSize);
	}
	

	/**
	 * 처리단위 개수별로 호출. 모자르면 마지막인데.. sheet 단위라 마지막이 아닐수도 있음.
	 * 끝나면 다한거다.
	 */
	@Override
	public abstract void saveAction(List<Map<String, String>> rows);
	

	/**
	 * 이건 로컬 테스트용.
	 * @param args
	 */
	public static void main(String[] args) {
		log.debug("업무별 확장 클래스...");
		TestLocalReader.mainMapXlsxReader(args);
	}
	
}
