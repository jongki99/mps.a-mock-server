package com.example.demo.util.data_reader.reader.xlsx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>saveAction 만 추가해주면 동작은 한다.
 * 
 * Map 으로 데이터를 받아서 처리할 수 있도록 기능을 제공하므로, 이것을 상속 받아서 saveAction 부분을 overrride 해서 업무 처리를 하도록 한다.
 * 또 다른 클래스 구현체로 정의해서 업무 처리를 할수 있음.
 * 
 * 이 구현체는 Map<String, String> 으로 데이터를 key(A) value 쌍으로 조회해서 사용할수 있다.
 * 
 * 참조 구현제 : {@link AbsObjectXlsxReader} 를 사용하면, DTO 형태로 매핑해주므로 매핑처리를 별도 구현하지 않아도 사용하기 편하게 할수는 있다.
 * </pre>
 */
@Slf4j
public class MapXlsxReader extends AbsXlsxStreamReader<Map<String, String>> {
	
	
	public MapXlsxReader() {
		this(1000);
	}
	public MapXlsxReader(int rowSize) {
		super(rowSize);
		log.debug("set rowSize={}", rowSize);
	};
	
	
	/**
	 * <pre>여기서는 추상으로 가도되지만... 실제 구현체로도 가능해서 여기까지 한다.
	 * 
	 * 사용할때는 saveAction 만 override 해서 사용한다.
	 * </pre>
	 */
	@Override
	public void saveAction(List<Map<String, String>> rows) {
		if ( CollectionUtils.isNotEmpty(rows) ) {
			// apache poi 를 사용하므로 common 꺼를 사용... isNotEmpty 도 있고.
			log.debug("데이터 확인용 샘플. 저장 등의 업무 처리 용으로 사용.");
			log.debug("first={}", rows.get(0));
			log.debug("last={}", rows.get(rows.size()-1));
			log.debug("saveAction rows.size={}", rows.size());
		}
	}
	
	
	@Override
	public void endRow(int rowNum) {
		addRow(new HashMap<>(getRowDataMap()));
	}
	
	
	/**
	 * <pre>첫번째 컬럼은 기본값으로 처리.
	 * 
	 * 업무에 따라서 확장해서 사용한다.
	 * </pre>
	 */
	@Override
	protected boolean isValidationObject(Map<String, String> row) {
		if ( row == null ) {
			return false;
		}
		
		if ( StringUtils.isBlank(row.get("A")) ){ // 첫번째 컬럼 필수.
			return false;
		}
		
		return true;
	}
	
}
