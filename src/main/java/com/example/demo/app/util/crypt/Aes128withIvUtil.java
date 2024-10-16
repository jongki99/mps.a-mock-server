package com.example.demo.app.util.crypt;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * java 1.8 에서 기본으로 설정된 값으로 사용하는 정도의 암호화만 사용해서 처리 함.
 * AES/GCM 방식으로 iv 를 추가하여 사용.
 * 
 * dev/stg/prd 를 구분해서 설정.
 */
@Slf4j
@Component
public class Aes128withIvUtil {

    private static final String AES = "AES";
    private static final String AES_CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 128;
    private static final int IV_SIZE = 12; // GCM 모드에서 권장되는 IV 크기
    private static final int GCM_TAG_LENGTH = 128;
    
    @Value("${coupon.log.aes128.keyBase64}")
    private String aesKeyBase64;

    @Value("${coupon.log.aes128.ivBase64}")
    private String aesIvBase64;
    
    private SecretKey secretKey;
    private byte[] iv;

    @PostConstruct
    public void init() {
        // Base64로 인코딩된 키와 IV를 디코딩하여 사용
    	initMain(aesKeyBase64, aesIvBase64);
    }
    public void initMain(String aesKey, String aesIv) {
        byte[] decodedKey = Base64.decodeBase64(aesKey);
        this.iv = Base64.decodeBase64(aesIv);
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
    }


    public String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.encodeBase64URLSafeString(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        byte[] decodedBytes = Base64.decodeBase64(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    // AES 키와 IV를 생성하여 Base64로 인코딩된 문자열을 반환하는 메서드
    public static String generateAESKeyBase64() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        keyGen.init(AES_KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.encodeBase64URLSafeString(secretKey.getEncoded());
    }
    public static String generateIVBase64() {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return Base64.encodeBase64URLSafeString(iv);
    }
    /**
     * aesKeyBase64, ivBase64 generate 예제.
     * 
     * 간단하게 key,iv 로만 암호화/복호화 하기로 함.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // AES 키와 IV를 생성하여 출력
        String aesKeyBase64 = generateAESKeyBase64();
        String ivBase64 = generateIVBase64();
        log.debug("Generated AES Key (Base64): {}", aesKeyBase64);
        log.debug("Generated IV (Base64): {}\n", ivBase64);

    	Aes128withIvUtil util = new Aes128withIvUtil();
        util.initMain(aesKeyBase64, ivBase64); // 초기화. spring 에서 여기까지 처리 됨.
        
        
        // test.
        List<String> sources = new ArrayList<>();
        sources.add("1234567890");
        sources.add("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        
        for (Iterator<String> iterator = sources.iterator(); iterator.hasNext();) {
			String source = iterator.next();
			log.debug("source={}", source);
			
			String encrpyted = util.encrypt(source);
			log.debug("encrpyted={}", encrpyted);
			
			String decrpyted = util.decrypt(encrpyted);
			log.debug("decrpyted={}\n", decrpyted);
		}
    }
}