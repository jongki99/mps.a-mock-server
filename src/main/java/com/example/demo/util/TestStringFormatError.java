package com.example.demo.util;

public class TestStringFormatError {
	public static void main(String[] args) {
		System.out.println(String.format("{%s}", "정상"));
		System.out.println(String.format("{%s}", "[정상] 에러?"));
	}
}
