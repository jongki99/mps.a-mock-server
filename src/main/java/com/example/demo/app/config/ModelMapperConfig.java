package com.example.demo.app.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
				.setPropertyCondition(Conditions.isNotNull()) //null일경우 복사하지 않도록 설정
				.setFieldMatchingEnabled(true)
				.setFieldAccessLevel(AccessLevel.PRIVATE)			//setter 없이 매핑되도록 설정
				.setMatchingStrategy(MatchingStrategies.STRICT);  //필드명이 같을때만 매핑하도록 설정
		return modelMapper;
	}
}
