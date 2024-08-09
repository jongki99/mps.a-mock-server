package com.example.demo.util.data_reader;

import java.math.BigInteger;

import org.apache.poi.ss.util.NumberToTextConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestNumberToTextConverter {
	public static void main(String[] args) {
		{
			double value = 0.0;
			log.debug("double value={}, String.valueOf={}, text={}", value, String.valueOf(value), NumberToTextConverter.toText(value));
			// double value=0.0, String.valueOf=0.0, text=0
		}{
			double value = 3.3333333333333333333;
			log.debug("double value={}, String.valueOf={}, text={}", value, String.valueOf(value), NumberToTextConverter.toText(value));
			// double value=3.3333333333333335, String.valueOf=3.3333333333333335, text=3.33333333333333
		}{
			double value = 3.333333333333333333;
			log.debug("double value={}, String.valueOf={}, text={}", value, String.valueOf(value), NumberToTextConverter.toText(value));
			// double value=3.3333333333333335, String.valueOf=3.3333333333333335, text=3.33333333333333
		}{
			double value = 20241212000000L;
			log.debug("double value={}, String.valueOf={}, text={}", value, String.valueOf(value), NumberToTextConverter.toText(value));
			// double value=2.0241212E13, String.valueOf=2.0241212E13, text=20241212000000
		}{
			double value = 20241212000000.1234512345;
			log.debug("double value={}, String.valueOf={}, text={}", value, String.valueOf(value), NumberToTextConverter.toText(value));
			// double value=2.0241212000000125E13, String.valueOf=2.0241212000000125E13, text=20241212000000.1
		}{
			double value = 20241212235959L;
			log.debug("double value={}, String.valueOf={}, text={}", value, String.valueOf(value), NumberToTextConverter.toText(value));
			// double value=2.0241212235959E13, String.valueOf=2.0241212235959E13, text=20241212235959
		}{
			double value = 2024121223595900000L; // 20자리 에러네... ㅋㅋ... 
			log.debug("double value={}, String.valueOf={}, text={}", value, String.valueOf(value), NumberToTextConverter.toText(value));
			// double value=2.0241212235958999E18, String.valueOf=2.0241212235958999E18, text=2024121223595900000
		}{
			BigInteger value = new BigInteger("20241234235959000000000"); // 이렇게하면 오히려 toString 이 잘 찍히네... 어렵네... 숫자의 세계는... 
			log.debug("double value={}, String.valueOf={}, text={}", value, String.valueOf(value), NumberToTextConverter.toText(value.floatValue()));
			// double value=20241212235959000000000, String.valueOf=20241212235959000000000, text=2.02412123524346E+22
		}
		
	}
}
