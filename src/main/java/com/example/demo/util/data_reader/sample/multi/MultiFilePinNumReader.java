package com.example.demo.util.data_reader.sample.multi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo.util.data_reader.ReadFileTypeEnum;
import com.example.demo.util.data_reader.multi.AbsMultiFileObjectTypeReader;
import com.example.demo.util.data_reader.sample.base.SamplePinNumDto;
import com.example.demo.util.data_reader.sample.base.SaveActionUtil;
import com.example.demo.util.data_reader.sample.test.TestLocalMultiFileReader;

import lombok.extern.slf4j.Slf4j;


/**
 * <pre>데이터 파일을 읽어서 동일한 인터페이스로 업무영역을 streaming 처리를 할수 있도록 기본 설정을 하는 Reader 클래스
 * 
 * SamplePinNumDto 를 매핑정보 등을 정의하고, 실제 업무(main 부분 참조)는 업무 처리 로직에서 saveAction 을 override 해서 로직내에서 처리하도록 한다.
 * 
 * 참조 : {@link MultiFileHashMapReader} : List<HashMap> 으로 saveAction 을 받아서 처리하는 샘플. 처리 샘플로 제공하는 것으로 가급적 Dto 를 만들고, 해당 Dto 를 처리하도록 Reader 를 생성해서 사용하자.
 * 
 * 사용방법 : 생성자로 처리할 list size 와 파일 타입을 전달해서 처리 방법을 결정.
 * parse(inputStream) 을 호출하여 실행 처리한다.
 * saveAction 을 추가로 구현하거나, 업무 로직에서 생성자와 saveAction 메소드를 override 하여 업무 실행처리하는 방법으로 처리.
 * </pre>
 */
@Slf4j
public class MultiFilePinNumReader extends AbsMultiFileObjectTypeReader<SamplePinNumDto> {
	
	
	public MultiFilePinNumReader(int rowsSize, ReadFileTypeEnum readDataTypeEnum) {
		super(rowsSize, readDataTypeEnum);
	}
	

	@Override
	public List<SamplePinNumDto> convertObject(List<Map<String, String>> rows) {
		List<SamplePinNumDto> objects = new ArrayList<>();
		
		rows.forEach(row -> {
			SamplePinNumDto newObject = new SamplePinNumDto();
			newObject
				.pinNum(row.get("A"))
				.staDtm(row.get("B"))
				.endDtm(row.get("C"))
				.pinUrl(row.get("D"))
				;
			objects.add(newObject);
		});
		
		return objects;
	}


	@Override
	public void saveAction(List<SamplePinNumDto> rows) {
		SaveActionUtil.saveActionDto(rows);
	}
	
	
	public static void main(String[] args) throws Exception {
		log.debug("구현 테스트. "+MultiFilePinNumReader.class.getSimpleName());
		TestLocalMultiFileReader.mainPinNumMultiFileObjectReader(args);
	}
}
