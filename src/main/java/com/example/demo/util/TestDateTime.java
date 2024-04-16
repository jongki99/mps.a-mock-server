package com.example.demo.util;

import java.util.Date;

import org.joda.time.LocalDateTime;

public class TestDateTime {
	public static void main(String[] args) {
		LocalDateTime date = LocalDateTime.parse("1985-04-12T23:20:50.123");
		System.out.println(date);
		
		Date dd = new Date(Date.parse("1985-04-12"));
		System.out.println(dd);
	}
}
