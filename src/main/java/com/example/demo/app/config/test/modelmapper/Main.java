package com.example.demo.app.config.test.modelmapper;
import java.time.LocalDateTime;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        // ModelMapper 인스턴스 생성
        ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
		.setDeepCopyEnabled(true)
		.setPropertyCondition(Conditions.isNotNull()) //null일경우 복사하지 않도록 설정
		.setFieldMatchingEnabled(true)
//		.setFieldAccessLevel(AccessLevel.PUBLIC)			//setter 없이 매핑되도록 설정
		.setFieldAccessLevel(AccessLevel.PRIVATE)			//setter 없이 매핑되도록 설정
		.setMatchingStrategy(MatchingStrategies.STRICT);  //필드명이 같을때만 매핑하도록 설정

        // LocalDateTime과 Person 객체 생성
        Person person = new Person("John", LocalDateTime.now());

        // Person -> PersonDTO로 매핑
        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);

        // 결과 출력
        System.out.println("Name: " + personDTO.getName());
        System.out.println("Birth Date: " + personDTO.getBirthDate());
        
//        // Person -> PersonDTO로 매핑
//        Person person2 = modelMapper.map(personDTO, Person.class);
//
//        // 결과 출력
//        System.out.println("Name: " + person2.getName());
//        System.out.println("Birth Date: " + person2.getBirthDate());
        
        // 경고가 발생하는 지 확인
//        var list = List.of("a", "b", "c");
//        list.size();
//        
//        log.debug("list.size={}", list.size());
    }
}