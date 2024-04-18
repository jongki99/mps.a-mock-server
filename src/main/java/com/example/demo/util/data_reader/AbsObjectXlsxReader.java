package com.example.demo.util.data_reader;

import java.util.Map;

import com.example.demo.util.excel.ExcelColumnMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>Dto 를 이용해서 업무 처리를 구성하는게 가독성이 좋으므로, 이를 사용하기 위한 중간단계이다.
 * 
 * 샘플 구현체 : {@link PinNumXlsxReader} 구현체를 이용해서 데이터를 정의해두고, 구현체를 사용할때 saveAction 만 override 해서 업무만 종속시켜서 사용하는 방식으로 코드를 분리해서 사용할수 있도록 한다.
 * 
 * 참조 구현체 : {@link MapXlsxReader} 구현체는 Map 형식으로 작업을 할수 있다.
 * 
 * Dto 로 하게되면 Dto 구현체를 너무 많이 만들어줘야 하는데...
 * 
 * TODOKJK : Util 을 만들고, Map 에서 매핑처리하는 wrapper 를 만들어서 업무에서 사용할수 있는 방식을 해야 하나? ㅜ.
 * </pre>
 * 
 * @param <E>
 */
@Slf4j
public abstract class AbsObjectXlsxReader<E> extends AbsXlsxStreamReader<E> {

	public AbsObjectXlsxReader(int rowSize) {
		super(rowSize);
	};

	
	/**
	 * Dto 로 업무를 확실하게 사용하고 싶은 경우, 이 메소드를 구현해서 Dto 를 반환하도록 한다.
	 * 이 메소드가 없어도 하위에 넣으면 되지만... endRow 까지 구현해야 되서...
	 * 아니라면 endRow 업무를 각각의 클래스에서 만들어야 해서 여기서 해두면 업무단이 쉬워진다.
	 * 
	 * @return
	 */
	protected abstract Class<E> getRowClass();
	
	
	/**
	 * 엑셀 컬럼과 Dto vo 의 필드를 매핑해주는 설정 정보를 리턴.
	 * Dto 별로 해야 되서...
	 * 
	 * @return
	 */
	protected abstract Map<String, String> getCellToField();
	
	
	@Override
	public void endRow(int rowNum) {
		// end row
		E newObject;
		try {
			newObject = getRowClass().newInstance();
			if ( newObject == null ) {
				throw new RuntimeException("row class not initialize!");
			}
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
		
		ExcelColumnMapper.mapColumnsToFields(getCellToField(), getRowDataMap(), newObject);
		
		if ( isValidationObject(newObject) ) {
			addRow(newObject);
		}
	}
	
}
