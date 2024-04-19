package com.example.demo.util.data_reader.sample.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SaveActionUtil {

	public static void saveAction(List<Map<String, String>> rows) {
		if ( CollectionUtils.isNotEmpty(rows) ) {
			// apache poi 를 사용하므로 common 꺼를 사용... isNotEmpty 도 있고.
			log.debug("데이터 확인용 샘플. 저장 등의 업무 처리 용으로 사용.");
			log.debug("first={}", rows.get(0));
			log.debug("last={}", rows.get(rows.size()-1));
			log.debug("saveAction rows.size={}", rows.size());
		}
	}

	public static void saveActionDto(List<SamplePinNumDto> rows) {
		if ( CollectionUtils.isNotEmpty(rows) ) {
			// apache poi 를 사용하므로 common 꺼를 사용... isNotEmpty 도 있고.
			log.debug("데이터 확인용 샘플. 저장 등의 업무 처리 용으로 사용.");
			log.debug("first={}", rows.get(0));
			log.debug("last={}", rows.get(rows.size()-1));
			log.debug("saveAction rows.size={}", rows.size());
		}
	}

}
