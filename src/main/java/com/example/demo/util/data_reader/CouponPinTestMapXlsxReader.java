package com.example.demo.util.data_reader;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 샘플 MapXlsxReader 을 이용해서 Map 으로 object 를 받아서 업무 처리를 하기 위해서 사용한다.
 */
@Slf4j
public class CouponPinTestMapXlsxReader extends MapXlsxReader {

	/**
	 * 생성자, 처리 단위 개수를 지정해서 사용할 수 있도록 제공.
	 * 개수만큼 saveAction 에 list 로 들어온다.
	 * 
	 * @param rowSize
	 */
	public CouponPinTestMapXlsxReader(int rowSize) {
		super(rowSize);
	}

	/**
	 * 처리단위 개수별로 호출. 모자르면 마지막인데.. sheet 단위라 마지막이 아닐수도 있음.
	 * 끝나면 다한거다.
	 */
	@Override
	public void saveAction(List<Map<String, String>> rows) {
		// 샘플.
		if ( CollectionUtils.isNotEmpty(rows) ) {
			super.saveAction(rows);
		}
		// 실제 작업할때는 위 코드 지우고, 여기다 작업할거 하면 된다.
	}
	

	/**
	 * 이건 로컬 테스트용.
	 * @param args
	 */
	public static void main(String[] args) {
		log.debug("업무별 확장 클래스...");
		TestLocalXlsxReaderMain.mainMap(args);
	}
	
}
