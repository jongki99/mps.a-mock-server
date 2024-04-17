package com.example.demo.util.data_reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * xlsx, xls, csv 파일을 읽어서 특정 단위로 saveAction 처리할 수 있도록 인터페이스를 제공한다.
 * 
 * 저장처리를 할 부분에서 saveAction 을 구현하여, 해당 메소드가 처리를 하도록 해서, 일관된 처리방식을 구현할 수 있도록 한다.
 * 
 * 저장한 데이터의 total 카운트 처리.
 * 엑셀, csv 에서 데이터를 읽을때 skip 처리할 row isValidationObject 에서 처리한다.
 * 오류를 내야될 경우는 안에서 exception 을 내주면 되겠지? 종료처리는 정의하지 않았네... -_-;; 이건 쉽지 않겠구만..
 * 
 * AutoCloseable 을 구현해서 자원해제처리를 포함하도록 하자.
 * xls 를 csv 로 변환하는 샘플을 이용해서 이 인터페이스를 구현했는데...
 * 잘 안되면 그냥. 전체를 메모리에 올리고, 순차로 읽으면서 하위 인터페이스를 구현해도 된다.
 * 
 * S3 에 직접 stream 으로 받아서 처리하도록 InputStream 만 만들었다. 추가해도 됨. 구현을 FileInputStream 으로 바꿔주면 되니까...
 * 
 * @param <E>
 */
public interface DataFileReader<E> extends AutoCloseable {

	
	/**
	 * Exception 에서 IOException 으로 수정.
	 */
	@Override void close() throws IOException;


	/**
	 * 데이터를 읽어들이도록 초기화 설정.
	 * xls, xlsx 등으로 처리할 경우, 사용.
	 * 
	 * @param filePath : 파일을 직접 참조할 수 있는 경로, inputStream 일 때가 있는데...
	 * @param isAll : xlsx, xls 에서 multi sheet 기준으로 처리해야 할때, true 면 모든 sheet 를 각각 읽어 들일수 있도록 하는 옵션. csv 는 단일 구조라 대상이 아님.
	 */
	public void readData(InputStream inputStream, boolean isAll) throws IOException;
	
	
	/**
	 * s3 파일을 직접 읽어서 처리하는 경우, inputStream 을 제공하여 처리하는 경우...
	 * 첫번째 시트만을 보통 사용하므로, 이것으로 호출.
	 * 
	 * xls : sample 이 FileInputStream 이니까 이걸로 대신 전달해도 됨. 
	 * 
	 * @param inputStream
	 */
	public void readData(InputStream inputStream) throws IOException;
	
	
	/**
	 * 데이터 처리를 시작하도록 한다.
	 * 각자의 상황에 맞게 시작을 정의함.
	 * 이 호출후 데이터를 읽어서 saveAction 을 호출하게 됨.
	 * 
	 * @throws IOException
	 */
	public void parse() throws IOException;
	
	
	/**
	 * 전체 저장 개수를 조회하는 메소드.
	 * saveAction 으로 처리된 개수이다.
	 * isValidationObject
	 * 		true 이면, 저장객체
	 * 		false 이면, skip 처리하도록 구현한다.
	 * 
	 * @return
	 */
	public int getTotalCount(); // 전체 유효한 record 개수.
	
	
	/**
	 * 저장처리를 하도록 List 단위로 데이터를 내려준다.
	 * 처리개수를 지정할 수 있도록 하위 구현체에서 구현해서 해당 메소드를 호출해주므로 이 메소드를 업무 단위로 구현하여 저장처리하도록 한다.
	 * 
	 * @param rows
	 */
	public void saveAction(List<E> rows); // 단위 rows 당 묶음 처리용 메소드. list 개수만큼 list 로딩하여 처리한다.
}
