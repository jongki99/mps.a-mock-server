package com.example.demo.app.config.test.modelmapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class JacksonMain {
    public static void main(String[] args) {
        // ModelMapper 인스턴스 생성
    	ObjectMapper objectMapper = new ObjectMapper();
//		modelMapper.getConfiguration()
//		.setDeepCopyEnabled(true)
//		.setPropertyCondition(Conditions.isNotNull()) //null일경우 복사하지 않도록 설정
//		.setFieldMatchingEnabled(true)
////		.setFieldAccessLevel(AccessLevel.PUBLIC)			//setter 없이 매핑되도록 설정
//		.setFieldAccessLevel(AccessLevel.PRIVATE)			//setter 없이 매핑되도록 설정
//		.setMatchingStrategy(MatchingStrategies.STRICT);  //필드명이 같을때만 매핑하도록 설정

        // LocalDateTime과 Person 객체 생성
        Person person = new Person("John", LocalDateTime.now());

        // Person -> PersonDTO로 매핑
        PersonDTO personDTO;
        String objectJsonString = "{\"name\":\"test\", \"birthDate\":\"2024-08-08T12:12:12.1231\"}";
		try {
			JavaType t = TypeFactory.defaultInstance().constructType(Exception.class);
			Class<?> bt = ClassUtil.rawClass(t);
			System.out.println(bt);
			
			RuntimeException e1 = new RuntimeException("1");
			RuntimeException e2 = new RuntimeException(e1);
			RuntimeException e3 = new RuntimeException(e2);
			String str = objectMapper.writeValueAsString(e3);
			System.out.println(str);			

			personDTO = objectMapper.readValue(objectJsonString, PersonDTO.class);
			Map map = objectMapper.readValue(objectJsonString, Map.class);
			System.out.println(map.toString());

			Map map2 = objectMapper.readValue("{\"이상한 스트링.\":\"\"}", Map.class);
			System.out.println(map2.toString());

			String commCdValue = objectJsonString;
			PersonDTO dto = objectMapper.reader().forType(new TypeReference<PersonDTO>() {}).readValue(commCdValue);
			System.out.println("dto:" + dto);
//			objectMapper

	        // 결과 출력
	        System.out.println("Name: " + personDTO.getName());
	        System.out.println("Birth Date: " + personDTO.getBirthDate());
		} catch (IOException e) {
			e.printStackTrace();
		}

        Map<String, String> messageMap;
		try {
			messageMap = objectMapper.readValue(objectJsonString, Map.class);
	        System.out.println(messageMap.toString());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
//		Main.main(args);
    }

}
