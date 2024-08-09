package com.example.demo.util.test_string;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestStringEquals {
	public static void main(String[] args) {
		if ( "".equals(null) ) {
			log.error("");
		} else {
			log.info("string.equals(null) false :: OK");
		}
	}
}
