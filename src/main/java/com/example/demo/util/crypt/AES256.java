package com.example.demo.util.crypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AES256 {

	private static volatile AES256 INSTANCE;
	
	// AES256 CDC 에서 사용 하는 IV ( padding 처리용 : 16바이트 )
	private static byte[] ivByte                  = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  };
	// AES256 알고리즘 및 Padding 방법
	private static final String ALGORITHM_PADDING = "AES/CBC/PKCS5Padding";
	// 문자 인코딩 방식
	private static final String CHARSET_NAME      = "UTF-8";
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static AES256 getInstance() {
		if ( INSTANCE == null ) {
			synchronized ( AES256.class) {
				if (INSTANCE == null) {
					INSTANCE = new AES256();
				}
			}
		}
		return INSTANCE;
	}

    /**
    * <ul>
    * <li> 업무명    : AES256 암호화 기능 </li>
    * <li> METHOD : encode </li>
    * <li> PARAM  : String str, String aesKey </li>
    * <li> RETURN : String 암호화된 스트링 </li>
    * <li>설  명       : AES256 CDC 방식으로 암호화 처리 </li>
    * <li>          암호화된 문자열을 BASE64 로 다시 한번 암호화 처리 하여 리턴</li>
    * <li>작성일      : 2020. 07. 02.</li>
    * <li>작성자      : 이우정 </li>
    * </ul>
    */
	public static String encode( String str, String aesKey ) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] bytekey = aesKey.getBytes(CHARSET_NAME);
		SecretKeySpec secretKeySpec = new SecretKeySpec(bytekey, "AES");

		Cipher c = Cipher.getInstance(ALGORITHM_PADDING);
		c.init(Cipher.ENCRYPT_MODE, secretKeySpec,  new IvParameterSpec(ivByte));

		byte[] encrypted = c.doFinal(str.getBytes(CHARSET_NAME));
		return new String(Base64.encodeBase64(encrypted));
	}

    /**
    * <ul>
    * <li> 업무명    : AES256 복호화 기능 </li>
    * <li> METHOD : decode </li>
    * <li> PARAM  : String str( AES256 암호화된 문자열 ), String aesKey </li>
    * <li> RETURN : String 복호화된 스트링 </li>
    * <li>설  명       : AES256 CDC 방식으로 복호화 처리 </li>
    * <li>          복호화 하기 전에 암호화된 문자열을 BASE64 로 먼저 복호화 처리 한 후에 AES256 복호화 하여 리턴</li>
    * <li>작성일      : 2020. 07. 02.</li>
    * <li>작성자      : 이우정 </li>
    * </ul>
    */
	public static String decode( String str, String aesKey ) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] bytekey = aesKey.getBytes(CHARSET_NAME);
		SecretKeySpec secretKeySpec = new SecretKeySpec(bytekey, "AES");

		Cipher c = Cipher.getInstance(ALGORITHM_PADDING);
		c.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(ivByte));

		byte[] byteStr = Base64.decodeBase64(str.getBytes(CHARSET_NAME));
		return new String(c.doFinal(byteStr), CHARSET_NAME);
	}
	
	
	public static void main(String[] args) {
		
	}
}
