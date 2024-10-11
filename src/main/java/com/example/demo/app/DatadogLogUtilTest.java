package com.example.demo.app;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.joda.time.LocalDateTime;

import java.util.Base64;

@Slf4j
public class DatadogLogUtilTest {

//    @Value("${aes.key}")
    private String aesKey = "1234567890123456";
    
	public static void main(String[] args) {
		new DatadogLogUtilTest().run(LocalDateTime.now().toString());
	}

    public void run(String... args) {
    	log.debug("run start = {}", args.length > 0 ? args[0] : "");
    	
        if (aesKey == null || aesKey.length() != 16) {
            log.error("Invalid AES key. It must be 16 characters long.");
            return;
        }

        DatadogLogUtil util = new DatadogLogUtil(aesKey);
        String originalText = "Hello, AES Encryption!";
        log.info("Original Text: {}", originalText);

        // 암호화
        String encodedText = util.encode(originalText);
        log.info("Encoded Text: {}", encodedText);

        // 복호화
        String decodedText = util.decode(encodedText);
        log.info("Decoded Text: {}", decodedText);

        // 검증
        if (originalText.equals(decodedText)) {
            log.info("Success: Decoded text matches the original.");
        } else {
            log.error("Failure: Decoded text does not match the original.");
        }

        // 추가 테스트 시나리오
        performAdditionalTests(util);
    }

    /**
     * 추가적인 테스트 시나리오를 실행하는 메서드
     *
     *
     * @param util DatadogLogUtil 인스턴스
     */
    private void performAdditionalTests(DatadogLogUtil util) {
        log.info("Starting additional tests...");

        // 테스트 1: 빈 문자열
        String empty = "";
        String encodedEmpty = util.encode(empty);
        String decodedEmpty = util.decode(encodedEmpty);
        log.info("Test 1 - Empty String: {}", decodedEmpty.equals(empty) ? "Passed" : "Failed");

        // 테스트 2: 특수 문자
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",.<>/?`~";
        String encodedSpecial = util.encode(specialChars);
        String decodedSpecial = util.decode(encodedSpecial);
        log.info("Test 2 - Special Characters: {}", decodedSpecial.equals(specialChars) ? "Passed" : "Failed");

        // 테스트 3: 긴 문자열
        StringBuilder longTextBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longTextBuilder.append("A");
        }
        String longText = longTextBuilder.toString();
        String encodedLong = util.encode(longText);
        String decodedLong = util.decode(encodedLong);
        log.info("Test 3 - Long String: {}", decodedLong.equals(longText) ? "Passed" : "Failed");

        // 테스트 4: null 입력 처리
        String nullEncoded = util.encode(null);
        String nullDecoded = util.decode(nullEncoded);
        log.info("Test 4 - Null Input: {}", nullDecoded == null ? "Passed" : "Failed");

        log.info("Additional tests completed.");
    }
}