package com.example.demo.app.util.crypt;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;

@Slf4j
public class DatadogLogUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    @Value("datadog.aes-key")
    private final String key;

    /**
     * 생성자에서 AES 키를 주입받습니다.
     *
     * @param key AES-128 암호화를 위한 16바이트 키
     */
    public DatadogLogUtil(String key) {
        if (key == null || key.length() != 16) {
            throw new IllegalArgumentException("AES key must be 16 characters long");
        }
        this.key = key;
    }

    /**
     * AES-128 암호화 메서드
     *
     * @param source 암호화할 문자열
     * @return Base64로 인코딩된 암호문, 실패 시 null
     */
    public String encode(String source) {
        if (source == null) {
            log.error("encode() called with null source");
            return null;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(source.getBytes("UTF-8"));
            String encoded = Base64.getEncoder().encodeToString(encrypted);
            log.debug("Encoded '{}' to '{}'", source, encoded);
            return encoded;
        } catch (Exception e) {
            log.error("Error during encoding: ", e);
            return null;
        }
    }

    /**
     * AES-128 복호화 메서드
     *
     * @param source Base64로 인코딩된 암호문
     * @return 복호화된 문자열, 실패 시 null
     */
    public String decode(String source) {
        if (source == null) {
            log.error("decode() called with null source");
            return null;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(source);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            String decoded = new String(decrypted, "UTF-8");
            log.debug("Decoded '{}' to '{}'", source, decoded);
            return decoded;
        } catch (Exception e) {
            log.error("Error during decoding: ", e);
            return null;
        }
    }
}