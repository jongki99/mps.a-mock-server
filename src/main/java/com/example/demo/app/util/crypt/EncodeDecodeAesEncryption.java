package com.example.demo.app.util.crypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class EncodeDecodeAesEncryption {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String FIXED_KEY = "MySecretKey12345"; // 16바이트 키

    // AES-128 암호화
    public static String encrypt(String input, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        return Base64.encodeBase64String(cipher.doFinal(input.getBytes()));
    }

    // AES-128 복호화
    public static String decrypt(String encryptedInput, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedInput)));
    }

    // AES 키 및 IV 생성
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // AES-128
        return keyGen.generateKey();
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // 고정 키로 해싱
    public static String hashKey(String key) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(key.getBytes("UTF-8"));
        return Base64.encodeBase64String(hash);
    }

    // JSON 생성
    public static String createJson(String encryptedData, String hashedKey, String hashedIV) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.put("data", encryptedData);
        jsonObject.put("key", hashedKey);
        jsonObject.put("iv", hashedIV);
        return objectMapper.writeValueAsString(jsonObject);
    }

    // 키 복호화
    public static SecretKey decryptKey(String encryptedKey) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(encryptedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    public static void main(String[] args) {
        try {
        	String jsonOutput = null;
    		SecretKey key = generateKey();
    		byte[] iv = generateIV();
        	{
        		String originalText = "Hello, World!";
        		
        		// 암호화
        		String encryptedData = encrypt(originalText, key, iv);
        		String hashedKey = hashKey(Base64.encodeBase64String(key.getEncoded()));
        		String hashedIV = hashKey(Base64.encodeBase64String(iv));
        		
        		// JSON 생성
        		jsonOutput = createJson(encryptedData, hashedKey, hashedIV);
        		System.out.println("JSON Output: " + jsonOutput);
        	}
        	
        	if ( jsonOutput != null ) {
        		
        		// JSON 파싱
        		ObjectMapper objectMapper = new ObjectMapper();
        		ObjectNode jsonObject = objectMapper.readValue(jsonOutput, ObjectNode.class);
        		String receivedEncryptedData = jsonObject.get("data").asText();
        		String receivedHashedKey = jsonObject.get("key").asText();
        		String receivedHashedIV = jsonObject.get("iv").asText();
        		
        		// 해시된 키와 IV를 원본 키 및 IV로 변환
        		SecretKey decryptedKey = decryptKey(Base64.encodeBase64String(key.getEncoded()));
        		byte[] decryptedIV = Base64.decodeBase64(receivedHashedIV); // 복호화할 IV
        		
        		// 복호화 과정
        		String decryptedData = decrypt(receivedEncryptedData, decryptedKey, iv);
        		System.out.println("Decrypted Data: " + decryptedData);
        	} else {
        		System.out.println("no encoded data.");
        	}


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}