package com.example.demo.app.util.crypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SecureAES {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String HMAC_SHA256 = "HmacSHA256";

    // MainGenKeyEnum: 암복호화 구분값과 암호화 키
    public enum MainGenKeyEnum {
        KEY1("key1", "secretValue1"),
        KEY2("key2", "secretValue2"),
        KEY3("key3", "secretValue3");

        private final String keyName;
        private final String keyValue;

        MainGenKeyEnum(String keyName, String keyValue) {
            this.keyName = keyName;
            this.keyValue = keyValue;
        }

        public String getKeyName() {
            return keyName;
        }

        public String getKeyValue() {
            return keyValue;
        }
    }

    // MainOldKeyEnum: 복호화 전용 구분값
    public enum MainOldKeyEnum {
        OLD_KEY1("oldKey1", "oldSecretValue1"),
        OLD_KEY2("oldKey2", "oldSecretValue2");

        private final String keyName;
        private final String keyValue;

        MainOldKeyEnum(String keyName, String keyValue) {
            this.keyName = keyName;
            this.keyValue = keyValue;
        }

        public String getKeyName() {
            return keyName;
        }

        public String getKeyValue() {
            return keyValue;
        }
    }

    // 키 저장소
    private static final Map<String, SecretKey> keys = new HashMap<>();

    static {
        initializeKeys();
    }

    private static void initializeKeys() {
        try {
            for (MainGenKeyEnum key : MainGenKeyEnum.values()) {
                keys.put(key.getKeyName(), generateKey(key.getKeyValue()));
            }
            for (MainOldKeyEnum key : MainOldKeyEnum.values()) {
                keys.put(key.getKeyName(), generateKey(key.getKeyValue()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize keys", e);
        }
    }

    // AES 암호화
    public static String encrypt(String input, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        return Base64.encodeBase64String(cipher.doFinal(input.getBytes()));
    }

    // AES 복호화
    public static String decrypt(String encryptedInput, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedInput)));
    }

    // HMAC 생성
    public static String generateHMAC(String data, SecretKey key) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(key);
        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return Base64.encodeBase64String(hmacBytes);
    }

    // 키 생성
    public static SecretKey generateKey(String keyValue) throws Exception {
        byte[] keyBytes = keyValue.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        keyBytes = sha.digest(keyBytes);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Salt 생성
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    // JSON 생성
    public static String createJson(String encryptedData, String hmac, String salt, String iv, String keyName) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.put("data", encryptedData);
        jsonObject.put("hmac", hmac);
        jsonObject.put("salt", salt);
        jsonObject.put("iv", iv);
        jsonObject.put("keyName", keyName);
        return objectMapper.writeValueAsString(jsonObject);
    }

    // 복호화 처리 메서드
    public static String decryptData(String jsonOutput) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.readValue(jsonOutput, ObjectNode.class);
        String receivedEncryptedData = jsonObject.get("data").asText();
        String receivedHMAC = jsonObject.get("hmac").asText();
        String receivedIV = jsonObject.get("iv").asText();
        String receivedSalt = jsonObject.get("salt").asText();
        String receivedKeyName = jsonObject.get("keyName").asText();

        SecretKey key = findKey(receivedKeyName);
        if (key == null) {
            throw new SecurityException("Invalid key name");
        }

        byte[] decryptedIV = Base64.decodeBase64(receivedIV);

        // HMAC 검증
        String computedHMAC = generateHMAC(receivedEncryptedData + receivedSalt, key);
        if (!computedHMAC.equals(receivedHMAC)) {
            throw new SecurityException("HMAC verification failed!");
        }

        // 복호화
        return decrypt(receivedEncryptedData, key, decryptedIV);
    }

    private static SecretKey findKey(String keyName) {
        SecretKey key = keys.get(keyName);
        if (key == null) {
            // MainGenKeyEnum에서 키를 찾지 못한 경우, MainOldKeyEnum에서 검색
            for (MainOldKeyEnum oldKey : MainOldKeyEnum.values()) {
                if (oldKey.getKeyName().equals(keyName)) {
                    return keys.get(oldKey.getKeyName());
                }
            }
        }
        return key;
    }

    public static void main(String[] args) {
        try {
            String originalText = "Hello, Secure World!";
            MainGenKeyEnum selectedKey = MainGenKeyEnum.KEY1; // 사용할 키 선택
            SecretKey mainKey = keys.get(selectedKey.getKeyName());
            byte[] iv = generateIV();
            byte[] salt = generateSalt();

            // 암호화
            String encryptedData = encrypt(originalText, mainKey, iv);
            String hmac = generateHMAC(encryptedData + Base64.encodeBase64String(salt), mainKey);

            // JSON 생성
            String jsonOutput = createJson(encryptedData, hmac, Base64.encodeBase64String(salt), Base64.encodeBase64String(iv), selectedKey.getKeyName());
            System.out.println("JSON Output: " + jsonOutput);

            // 복호화 처리
            String decryptedData = decryptData(jsonOutput);
            System.out.println("Decrypted Data: " + decryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}