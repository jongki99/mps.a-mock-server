package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdotDirectAccessUrlConverter {
	public static void main(String[] args) {
		String apolloUrl = "https://stg-apollo.sktmembership.co.kr/mps/apollo-bff/mypage/couponbox/coupon-detail.do?cpnId=5668&cpnPinId=5668&ocb=N";
		
		String preUrl = "skt.adot://common_web?url=" + convertUrlEncode(apolloUrl);
		
		// encodedUrl sampleurl https%3A%2F%2Fdev-apollo.sktmembership.co.kr%2Fmps%2Fapollo-bff%2Fprogram%2Ftday.do%3FdeeplinkYn%3DY&enable_cookies=Y&toolbar_type=close&full_screen=true

		
	}

	private static String convertUrlEncode(String apolloUrl) {
		String encodedUrl = null;
		try {
			encodedUrl = URLEncoder.encode(apolloUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		log.debug("encodedUrl=\n\n{}\n", encodedUrl);

		return encodedUrl;
	}
}
