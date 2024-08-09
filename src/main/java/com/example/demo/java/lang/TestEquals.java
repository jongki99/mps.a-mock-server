package com.example.demo.java.lang;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEquals {
	public static void main(String[] args) {
		testNull();
	}
	
	
	private static void testNull() {

		String test = "1";
		String null1 = null;
		
		if ( test.equals(null1) ) {
			throw new RuntimeException("not eq");
		} else {
			log.debug("ok, not error"); // null하고 같은지를 체크하면 당연히 false
		}
		
	}
}
