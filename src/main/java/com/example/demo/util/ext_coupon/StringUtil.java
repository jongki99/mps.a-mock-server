package com.example.demo.util.ext_coupon;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * 업무 명 : StringUtil
 * 작성자 : 
 * 작성일 : 2021.01.10
 * 설 명 : 공통 StringUtil
 */
@Slf4j
@Component
public class StringUtil {

	/**
	 * 생성자
	 */
	private StringUtil() {
	}

	/**
	 * 주어진 문자열이 널(null)인경우 빈("") 문자열을 리턴하고
	 * 널 이외의 문자열은 그냥 리턴한다.
	 *
	 * <pre>
	 * StringUtil.defaultString(null)  = ""
	 * StringUtil.defaultString("")    = ""
	 * StringUtil.defaultString("bat") = "bat"
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String defaultString(final String str) {
		return StringUtils.defaultString(str);
	}

	/**
	 * 주어진 문자열로부터 컨트롤 문자들 (char <= 32)을 제거한다.
	 *
	 * <pre>
	 * StringUtil.trim(null)          = null
	 * StringUtil.trim("")            = ""
	 * StringUtil.trim("     ")       = ""
	 * StringUtil.trim("abc")         = "abc"
	 * StringUtil.trim("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String trim(final String str) {
		return StringUtils.trim(str);
	}

	/**
	 * 휴대전화 국번
	 */
	@SuppressWarnings("rawtypes")
	private static final List EXCHANGE_NO_LIST = Arrays.asList(new String[] {
			"010", "011", "016", "017", "018", "019" });

	/*
	 * HTML escape character 대상.
	 */
	private static final String[][] HTML_ARRAY = {
			{"&" , "&amp;" },  // & - ampersand  <-- 맨위에 있어야 함.
			{"\"", "&quot;"},  // " - double-quote
			{"<" , "&lt;"  },  // < - less-than
			{">" , "&gt;"  }   // > - greater-than
	};


	/**
	 * 주어진 문자열에 검색문자가 들어있는지 검사한다.
	 *
	 * <pre>
	 * StringUtil.contains(null, *)    = false
	 * StringUtil.contains("", *)      = false
	 * StringUtil.contains("abc", 'a') = true
	 * StringUtil.contains("abc", 'z') = false
	 * </pre>
	 *
	 * @param str
	 * @param searchChar
	 * @return
	 */
	public static boolean contains(final String str, final char searchChar) {
		return StringUtils.contains(str, searchChar);
	}

	/**
	 * 주어진 문자열에 검색문자열이 들어있는지 검사한다.
	 *
	 * <pre>
	 * StringUtil.contains(null, *)    = false
	 * StringUtil.contains(*, null)    = false
	 * StringUtil.contains("", "")     = false
	 * StringUtil.contains("abc", "")  = true
	 * StringUtil.contains("abc", "a") = true
	 * StringUtil.contains("abc", "z") = false
	 * </pre>
	 *
	 * @param str
	 * @param searchChar
	 * @return
	 */
	public static boolean contains(final String str, final String searchStr) {
		return StringUtils.contains(str, searchStr);
	}

	/**
	 * 주어진 문자열에 대소문자를 구분하지 않고 검색문자열이 들어있는지 검사한다.
	 *
	 * <pre>
	 * StringUtil.contains(null, *)    = false
	 * StringUtil.contains(*, null)    = false
	 * StringUtil.contains("", "")     = false
	 * StringUtil.contains("abc", "")  = true
	 * StringUtil.contains("abc", "a") = true
	 * StringUtil.contains("abc", "z") = false
	 * StringUtil.contains("abc", "A") = true
	 * StringUtil.contains("abc", "Z") = false
	 * </pre>
	 *
	 * @param str
	 * @param searchChar
	 * @return
	 */
	public static boolean containsIgnoreCase(final String str, final String searchStr) {
		return StringUtils.containsIgnoreCase(str, searchStr);
	}



	/**
	 * 주어진 문자가 null 또는 공백이거나 주어진 Object가 null이면 defaultStr값 리턴
	 * @param str
	 * @param defaultStr
	 * @return
	 */
	public static String defaultString(final Object obj, final String defaultStr) {
		if (obj == null || StringUtil.isBlank(String.valueOf(obj)) ) {
			return defaultStr;
		}else{
			return obj.toString();
		}
	}


	/**
	 * 주어진 문자가 null 또는 공백이라면 defaultStr값 리턴
	 * @param str
	 * @param defaultStr
	 * @return
	 */
	public static String defaultString(final String str, final String defaultStr) {
		if ( StringUtil.isBlank(str) ) {
			return defaultStr;
		}else{
			return str;
		}
	}

	//@@@DOWN_SIZING_STT
	/**
	 * 주어진 문자열이 널(null)이거나 "null"문자열인 경우 빈("") 문자열을 리턴하고
	 * 널, "null"문자열 이외의 문자열은 그냥 리턴한다.
	 *
	 * <pre>
	 * StringUtil.defaultString(null)  = ""
	 * StringUtil.defaultString("null")  = ""
	 * StringUtil.defaultString("")    = ""
	 * StringUtil.defaultString("bat") = "bat"
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String defaultStringDS(String str) {
		str = StringUtil.isNulls(str);
		return StringUtils.defaultString(str);
	}

	/**
	 * 주어진 문자가 null, "null"문자열 또는 공백이라면 defaultStr값 리턴
	 *
	 * @param str
	 * @param defaultStr
	 * @return
	 */
	public static String defaultStringDS(String str, final String defaultStr) {
		str = StringUtil.isNulls(str);

		if ( StringUtil.isBlank(str) ) {
			return defaultStr;
		}else{
			return str;
		}
	}
	//@@@DOWN_SIZING_END

	/**
	 * 두 문자열을 비교하여 동일하면 true를 리턴한다.
	 * 두 문자열에 trim을 적용한 후, 비교한다.
	 *
	 * <pre>
	 * StringUtil.equals(null, null)    = true
	 * StringUtil.equals(null, "abc")   = false
	 * StringUtil.equals("abc", null)   = false
	 * StringUtil.equals("abc", "abc")  = true
	 * StringUtil.equals("abc", "abc ") = true
	 * StringUtil.equals("abc", "ABC")  = false
	 * </pre>
	 *
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(final String str1, final String str2) {
		return StringUtils.equals(trim(str1), trim(str2));
	}

	/**
	 * 대소문자를 구분하지 않고 두 문자열을 비교하여 동일하면 true를 리턴한다.
	 *
	 * <pre>
	 * StringUtil.equalsIgnoreCase(null, null)   = true
	 * StringUtil.equalsIgnoreCase(null, "abc")  = false
	 * StringUtil.equalsIgnoreCase("abc", null)  = false
	 * StringUtil.equalsIgnoreCase("abc", "abc") = true
	 * StringUtil.equalsIgnoreCase("abc", "ABC") = true
	 * </pre>
	 *
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsIgnoreCase(final String str1, final String str2) {
		return StringUtils.equalsIgnoreCase(trim(str1), trim(str2));
	}

	/**
	 * 주어진 문자열에서 검색하고자 하는 문자열이 포함된 첫번째 인덱스를 리턴한다.
	 * 주어진 문자열이 널(null)인경우, -1을 리턴한다.
	 *
	 * <pre>
	 * StringUtil.indexOf(null, *)          = -1
	 * StringUtil.indexOf(*, null)          = -1
	 * StringUtil.indexOf("", "")           = 0
	 * StringUtil.indexOf("aabaabaa", "a")  = 0
	 * StringUtil.indexOf("aabaabaa", "b")  = 2
	 * StringUtil.indexOf("aabaabaa", "ab") = 1
	 * StringUtil.indexOf("aabaabaa", "")   = 0
	 * </pre>
	 *
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static int indexOf(final String str, final String searchStr) {
		return StringUtils.indexOf(str, searchStr);
	}

	/**
	 * 주어진 문자열에서 startPos이후부터 검색하고자 하는 문자열이 포함된 첫번째 인덱스를 리턴한다.
	 * 주어진 문자열이 널(null)인경우, -1을 리턴한다.
	 *
	 * <pre>
	 * StringUtil.indexOf(null, *, *)          = -1
	 * StringUtil.indexOf(*, null, *)          = -1
	 * StringUtil.indexOf("", "", 0)           = 0
	 * StringUtil.indexOf("aabaabaa", "a", 0)  = 0
	 * StringUtil.indexOf("aabaabaa", "b", 0)  = 2
	 * StringUtil.indexOf("aabaabaa", "ab", 0) = 1
	 * StringUtil.indexOf("aabaabaa", "b", 3)  = 5
	 * StringUtil.indexOf("aabaabaa", "b", 9)  = -1
	 * StringUtil.indexOf("aabaabaa", "b", -1) = 2
	 * StringUtil.indexOf("aabaabaa", "", 2)   = 2
	 * StringUtil.indexOf("abc", "", 9)        = 3
	 * </pre>
	 *
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static int indexOf(final String str, final String searchStr, final int startPos) {
		return StringUtils.indexOf(str, searchStr, startPos);
	}

	/**
	 * 공백(" "), 빈(""), 널 문자열을 검사한다.
	 *
	 * <pre>
	 * StringUtil.isBlank(null)      = true
	 * StringUtil.isBlank("")        = true
	 * StringUtil.isBlank(" ")       = true
	 * StringUtil.isBlank("bob")     = false
	 * StringUtil.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param src
	 * @return
	 */
	public static boolean isBlank(final String src) {
		return StringUtils.isBlank(src);
	}

	/**
	 * 빈(""), 널 문자열을 검사한다.
	 *
	 * <pre>
	 * StringUtil.isEmpty(null)      = true
	 * StringUtil.isEmpty("")        = true
	 * StringUtil.isEmpty(" ")       = false
	 * StringUtil.isEmpty("bob")     = false
	 * StringUtil.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(final String src) {
		return StringUtils.isEmpty(src);
	}

	/**
	 * 전달받은 문자열이 숫자이면 true를 리턴한다.
	 *
	 * <pre>
	 * StringUtil.isNumeric(null)   = false
	 * StringUtil.isNumeric("")     = false
	 * StringUtil.isNumeric("  ")   = false
	 * StringUtil.isNumeric("12 ")  = false
	 * StringUtil.isNumeric("123")  = true
	 * StringUtil.isNumeric("12 3") = false
	 * StringUtil.isNumeric("ab2c") = false
	 * StringUtil.isNumeric("12-3") = false
	 * StringUtil.isNumeric("12.3") = false
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(final String str) {
		if ( StringUtil.isBlank(str) ) {
			return false;
		}
		return StringUtils.isNumeric(str);
	}


	/**
	 * 문자열의 왼쪽부터 길이만큼의 문자열을 리턴한다.
	 * len은 0 이상의 정수이어야 한다. 음수일 경우 예외가 발생한다.
	 *
	 * <pre>
	 * StringUtil.left(null, *)    = null
	 * StringUtil.left("", *)      = ""
	 * StringUtil.left("abc", 0)   = ""
	 * StringUtil.left("abc", 2)   = "ab"
	 * StringUtil.left("abc", 5)   = "abc"
	 * </pre>
	 *
	 * @param str
	 * @param len
	 * @return
	 */
	public static String left(final String str, final int len) {
		return StringUtils.left(str, len);
	}

	/**
	 * 문자열의 왼쪽에 공백(' ')를 붙인다.
	 * 전체 문자열의 크기가 size만큼 붙인다.
	 *
	 * <pre>
	 * StringUtil.leftPad(null, *)   = null
	 * StringUtil.leftPad("", 3)     = "   "
	 * StringUtil.leftPad("bat", 3)  = "bat"
	 * StringUtil.leftPad("bat", 5)  = "  bat"
	 * StringUtil.leftPad("bat", 1)  = "bat"
	 * StringUtil.leftPad("bat", -1) = "bat"
	 * </pre>
	 *
	 * @param str
	 * @param size
	 * @return
	 */
	public static String leftPad(final String str, final int size) {
		return StringUtils.leftPad(str, size);
	}

	/**
	 * 문자열의 왼쪽에 주어진 문자를 붙인다.
	 * 전체 문자열의 크기가 size만큼 붙인다.
	 *
	 * <pre>
	 * StringUtil.leftPad(null, *, *)     = null
	 * StringUtil.leftPad("", 3, 'z')     = "zzz"
	 * StringUtil.leftPad("bat", 3, 'z')  = "bat"
	 * StringUtil.leftPad("bat", 5, 'z')  = "zzbat"
	 * StringUtil.leftPad("bat", 1, 'z')  = "bat"
	 * StringUtil.leftPad("bat", -1, 'z') = "bat"
	 * </pre>
	 *
	 * @param str
	 * @param size
	 * @param padChar
	 * @return
	 */
	public static String leftPad(final String str, final int size, final char padChar) {
		return StringUtils.leftPad(str, size, padChar);
	}

	/**
	 * 문자열의 왼쪽에 주어진 문자열을 붙인다.
	 * 전체 문자열의 크기가 size만큼 붙인다.
	 *
	 * <pre>
	 * StringUtil.leftPad(null, *, *)      = null
	 * StringUtil.leftPad("", 3, "z")      = "zzz"
	 * StringUtil.leftPad("bat", 3, "yz")  = "bat"
	 * StringUtil.leftPad("bat", 5, "yz")  = "yzbat"
	 * StringUtil.leftPad("bat", 8, "yz")  = "yzyzybat"
	 * StringUtil.leftPad("bat", 1, "yz")  = "bat"
	 * StringUtil.leftPad("bat", -1, "yz") = "bat"
	 * StringUtil.leftPad("bat", 5, null)  = "  bat"
	 * StringUtil.leftPad("bat", 5, "")    = "  bat"
	 * </pre>
	 *
	 * @param str
	 * @param size
	 * @param padStr
	 * @return
	 */
	public static String leftPad(final String str, final int size, final String padStr) {
		return StringUtils.leftPad(str, size, padStr);
	}

	/**
	 * 문자열을 소문자로 변환한다.
	 *
	 * <pre>
	 * StringUtil.lowerCase(null)  = null
	 * StringUtil.lowerCase("")    = ""
	 * StringUtil.lowerCase("aBc") = "abc"
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String lowerCase(final String str) {
		return StringUtils.lowerCase(str);
	}

	/**
	 * 주어진 문자열에서 특정 포지션부터 주어진 길이만큼의 부분 문자열을 얻는다.
	 * 만약 주어진 포지션이 음수인 경우, 오른쪽에서부터 인덱싱한다.
	 *
	 * <pre>
	 * StringUtil.mid(null, *, *)    = null
	 * StringUtil.mid("", 0, *)      = ""
	 * StringUtil.mid("abc", 0, 2)   = "ab"
	 * StringUtil.mid("abc", 0, 4)   = "abc"
	 * StringUtil.mid("abc", 2, 4)   = "c"
	 * StringUtil.mid("abc", 4, 2)   = ""
	 * StringUtil.mid("abc", -2, 2)  = "ab"
	 * </pre>
	 *
	 * @param str
	 * @param pos
	 * @param len
	 * @return
	 */
	public static String mid(final String str, final int pos, final int len) {
		return StringUtils.mid(str, pos, len);
	}

	/**
	 * 주어진 문자열에서 모든 제거문자를 제거한 후,
	 * 문자가 제거된 문자열을 리턴한다.
	 *
	 * <pre>
	 * StringUtil.remove(null, *)       = null
	 * StringUtil.remove("", *)         = ""
	 * StringUtil.remove("queued", 'u') = "qeed"
	 * StringUtil.remove("queued", 'z') = "queued"
	 * </pre>
	 *
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String remove(final String str, final char remove) {
		return StringUtils.remove(str, remove);
	}

	/**
	 * 주어진 문자열에서 모든 제거문자를 제거한 후,
	 * 문자가 제거된 문자열을 리턴한다.
	 *
	 * <pre>
	 * StringUtil.remove(null, *)       = null
	 * StringUtil.remove("", *)         = ""
	 * StringUtil.remove("queuedz", new char[]{'u','z'}) = "qeed"
	 * </pre>
	 *
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String remove(final String str, final char[] remove) {
		String result = str;
		for ( int i = 0; i<remove.length; i++) {
			result = remove(result, remove[i]);
		}
		return result;
	}

	/**
	 * 주어진 문자열에서 모든 제거문자열을 제거한 후,
	 * 문자열이 제거된 문자열을 리턴한다.
	 *
	 * <pre>
	 * StringUtil.remove(null, *)        = null
	 * StringUtil.remove("", *)          = ""
	 * StringUtil.remove(*, null)        = *
	 * StringUtil.remove(*, "")          = *
	 * StringUtil.remove("queued", "ue") = "qd"
	 * StringUtil.remove("queued", "zz") = "queued"
	 * </pre>
	 *
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String remove(final String str, final String remove) {
		return StringUtils.remove(str, remove);
	}

	/**
	 * 주어진 문자열에서 바꾸하고자하는 문자열을 바꿀 문자열로 대체한다.
	 *
	 * <pre>
	 * StringUtil.replace(null, *, *)        = null
	 * StringUtil.replace("", *, *)          = ""
	 * StringUtil.replace("any", null, *)    = "any"
	 * StringUtil.replace("any", *, null)    = "any"
	 * StringUtil.replace("any", "", *)      = "any"
	 * StringUtil.replace("aba", "a", null)  = "aba"
	 * StringUtil.replace("aba", "a", "")    = "b"
	 * StringUtil.replace("aba", "a", "z")   = "zbz"
	 * </pre>
	 *
	 * @param text
	 * @param repl
	 * @param with
	 * @return
	 */
	public static String replace(final String text, final String repl, final String with) {
		return StringUtils.replace(text, repl, with);
	}

	/**
	 * '눋'을 아스키코드 '눝'으로 변환한다.
	 * @param text 검색할 문자
	 * @return
	 */
	public static String replaceToCodeFromNoot(final String text){
		return StringUtils.replace(text, "눋", "&#45597;");
	}

	/**
	 * 문자열의 오른쪽부터 길이만큼의 문자열을 리턴한다.
	 * len은 0 이상의 정수이어야 한다. 음수일 경우 예외가 발생한다.
	 *
	 * <pre>
	 * StringUtil.right(null, *)    = null
	 * StringUtil.right("", *)      = ""
	 * StringUtil.right("abc", 0)   = ""
	 * StringUtil.right("abc", 2)   = "bc"
	 * StringUtil.right("abc", 5)   = "abc"
	 * </pre>
	 *
	 * @param str
	 * @param len
	 * @return
	 */
	public static String right(final String str, final int len) {
		return StringUtils.right(str, len);
	}

	/**
	 * 문자열의 오른쪽에 공백(' ')를 붙인다.
	 * 전체 문자열의 크기가 size만큼 붙인다.
	 *
	 * <pre>
	 * StringUtil.rightPad(null, *)   = null
	 * StringUtil.rightPad("", 3)     = "   "
	 * StringUtil.rightPad("bat", 3)  = "bat"
	 * StringUtil.rightPad("bat", 5)  = "bat  "
	 * StringUtil.rightPad("bat", 1)  = "bat"
	 * StringUtil.rightPad("bat", -1) = "bat"
	 * </pre>
	 *
	 * @param str
	 * @param size
	 * @return
	 */
	public static String rightPad(final String str, final int size) {
		return StringUtils.rightPad( str, size );
	}

	/**
	 * 문자열의 오른쪽에 주어진 문자를 붙인다.
	 * 전체 문자열의 크기가 size만큼 붙인다.
	 *
	 * <pre>
	 * StringUtil.rightPad(null, *, *)     = null
	 * StringUtil.rightPad("", 3, 'z')     = "zzz"
	 * StringUtil.rightPad("bat", 3, 'z')  = "bat"
	 * StringUtil.rightPad("bat", 5, 'z')  = "batzz"
	 * StringUtil.rightPad("bat", 1, 'z')  = "bat"
	 * StringUtil.rightPad("bat", -1, 'z') = "bat"
	 * </pre>
	 *
	 * @param str
	 * @param size
	 * @param padChar
	 * @return
	 */
	public static String rightPad(final String str, final int size, final char padChar) {
		return StringUtils.rightPad(str, size, padChar);
	}

	/**
	 * 문자열의 왼쪽에 주어진 문자열을 붙인다.
	 * 전체 문자열의 크기가 size만큼 붙인다.
	 *
	 * <pre>
	 * StringUtil.rightPad(null, *, *)      = null
	 * StringUtil.rightPad("", 3, "z")      = "zzz"
	 * StringUtil.rightPad("bat", 3, "yz")  = "bat"
	 * StringUtil.rightPad("bat", 5, "yz")  = "batyz"
	 * StringUtil.rightPad("bat", 8, "yz")  = "batyzyzy"
	 * StringUtil.rightPad("bat", 1, "yz")  = "bat"
	 * StringUtil.rightPad("bat", -1, "yz") = "bat"
	 * StringUtil.rightPad("bat", 5, null)  = "bat  "
	 * StringUtil.rightPad("bat", 5, "")    = "bat  "
	 * </pre>
	 *
	 * @param str
	 * @param size
	 * @param padStr
	 * @return
	 */
	public static String rightPad(final String str, final int size, final String padStr) {
		return StringUtils.rightPad(str, size, padStr);
	}

	/**
	 * 주어진 문자열을 공백(' ')문자로 분리한다.
	 *
	 * <pre>
	 * StringUtil.split(null)       = null
	 * StringUtil.split("")         = []
	 * StringUtil.split("abc def")  = ["abc", "def"]
	 * StringUtil.split("abc  def") = ["abc", "def"]
	 * StringUtil.split(" abc ")    = ["abc"]
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String[] split(final String str) {
		return StringUtils.split(str);
	}

	/**
	 * 주어진 문자열을 구분문자로 분리한다.
	 *
	 * <pre>
	 * StringUtil.split(null, *)         = null
	 * StringUtil.split("", *)           = []
	 * StringUtil.split("a.b.c", '.')    = ["a", "b", "c"]
	 * StringUtil.split("a..b.c", '.')   = ["a", "b", "c"]
	 * StringUtil.split("a:b:c", '.')    = ["a:b:c"]
	 * StringUtil.split("a\tb\nc", null) = ["a", "b", "c"]
	 * StringUtil.split("a b c", ' ')    = ["a", "b", "c"]
	 * </pre>
	 *
	 * @param str
	 * @param separatorChar
	 * @return
	 */
	public static String[] split(final String str, final char separatorChar) {
		return StringUtils.split(str, separatorChar);
	}

	/**
	 * 주어진 문자열을 구분문자열에 포함된 문자들로 분리한다.
	 *
	 * <pre>
	 * StringUtil.split(null, *)         = null
	 * StringUtil.split("", *)           = []
	 * StringUtil.split("abc def", null) = ["abc", "def"]
	 * StringUtil.split("abc def", " ")  = ["abc", "def"]
	 * StringUtil.split("abc  def", " ") = ["abc", "def"]
	 * StringUtil.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
	 * StringUtil.split("ab:cd,ef", ":,") = ["ab", "cd", "ef"]
	 * </pre>
	 *
	 * @param str
	 * @param separatorChars
	 * @return
	 */
	public static String[] split(final String str, final String separatorChars) {
		return StringUtils.split(str, separatorChars);
	}

	/**
	 * 주어진 문자열로부터 시작 인덱스부터 서브 문자열을 리턴한다.
	 * 만약 시작 인덱스가 음수인 경우, 오른쪽에서부터 인덱싱한다.
	 *
	 * <pre>
	 * StringUtil.substring(null, *)   = null
	 * StringUtil.substring("", *)     = ""
	 * StringUtil.substring("abc", 0)  = "abc"
	 * StringUtil.substring("abc", 2)  = "c"
	 * StringUtil.substring("abc", 4)  = ""
	 * StringUtil.substring("abc", -2) = "bc"
	 * StringUtil.substring("abc", -4) = "abc"
	 * </pre>
	 *
	 * @param str
	 * @param start
	 * @return
	 */
	public static String substring(final String str, final int start) {
		return StringUtils.substring(str, start);
	}

	/**
	 * 주어진 문자열로부터 시작 인덱스부터 종료인덱스까지 서브 문자열을 리턴한다.
	 * 만약 인덱스가 음수인 경우, 오른쪽에서부터 인덱싱한다.
	 *
	 * <pre>
	 * StringUtil.substring(null, *, *)    = null
	 * StringUtil.substring("", * ,  *)    = ""
	 * StringUtil.substring("abc", 0, 2)   = "ab"
	 * StringUtil.substring("abc", 2, 0)   = ""
	 * StringUtil.substring("abc", 2, 4)   = "c"
	 * StringUtil.substring("abc", 4, 6)   = ""
	 * StringUtil.substring("abc", 2, 2)   = ""
	 * StringUtil.substring("abc", -2, -1) = "b"
	 * StringUtil.substring("abc", -4, 2)  = "ab"
	 * </pre>
	 *
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static String substring(final String str, final int start, final int end) {
		return StringUtils.substring(str, start, end);
	}


	/**
	 * 주어진 문자열로부터 컨트롤 문자들 (char <= 32)을 제거한다.
	 * 만약 컨트롤문자들이 제거된 문자열이 null인 경우 빈문자열("")을 리턴한다.
	 *
	 * <pre>
	 * StringUtil.trimToEmpty(null)          = ""
	 * StringUtil.trimToEmpty("")            = ""
	 * StringUtil.trimToEmpty("     ")       = ""
	 * StringUtil.trimToEmpty("abc")         = "abc"
	 * StringUtil.trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String trimToEmpty(final String str) {
		return StringUtils.trimToEmpty(str);
	}

	/**
	 * 주어진 문자열로부터 컨트롤 문자들 (char <= 32)을 제거한다.
	 * 만약 컨트롤문자들이 제거된 문자열이 빈문자열("")일 경우 null을 리턴한다.
	 *
	 * <pre>
	 * StringUtil.trimToNull(null)          = null
	 * StringUtil.trimToNull("")            = null
	 * StringUtil.trimToNull("     ")       = null
	 * StringUtil.trimToNull("abc")         = "abc"
	 * StringUtil.trimToNull("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String trimToNull(final String str) {
		return StringUtils.trimToNull(str);
	}


	/**
	 * HTML의 문자들을 escape처리한다.
	 *
	 * <xmp>
	 * 대상 문자
	 * & => &amp;  - ampersand
	 * \ => &quot; - double-quote
	 * < => &lt;   - less-than
	 * > => &gt;   - greater-than
	 * </xmp>
	 *
	 * @param str
	 * @return
	 */
	public static String escapeHtml(final String str) {
		if ( isBlank(str) ) {
			return str;
		}

		String content = str;
		for( int i = 0; i<HTML_ARRAY.length; i++ ) {
			content = replace(content, HTML_ARRAY[i][0], HTML_ARRAY[i][1]);
		}
		return content;
	}

	public static String unescapeHtml(final String str) {
		if ( isBlank(str) ) {
			return str;
		}

		String content = str;
		for( int i = HTML_ARRAY.length - 1; i >= 0; i-- ) {
			content = replace(content, HTML_ARRAY[i][1], HTML_ARRAY[i][0]);
		}
		return content;
	}



	/**
	 * 문자열의 길이를 리턴한다.
	 *
	 * <pre>
	 * StringUtil.getLength(null)    = 0
	 * StringUtil.getLength("")      = 0
	 * StringUtil.getLength("     ") = 5
	 * StringUtil.getLength("abc")   = 3
	 * StringUtil.getLength(" abc ") = 5
	 * </pre>
	 *
	 * @param src
	 * @return
	 */
	public static int getLength(final String src) {
		if ( isEmpty(src) ) {
			return 0;
		}
		return src.length();
	}



	/**
	 *
	 * 전달받은 문자열에서 앞부분의 연속된 '0'들을 제거하여 반환함
	 *
	 * @param value
	 * @return
	 */
	public static String eraseZero(String value) {
		if (value == null) {
			return "";
		}

		int index = 0;
		value = value.trim();

		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) != '0') {
				break;
			}
			index++;
		}
		return value.substring(index);
	}

	/**
	 * <pre>
	 * 전달받은 문자열에서 앞부분의 연속된 '0'들을 제거하여 반환함
	 *   ex) -0000000123 = -123
	 * </pre>
	 *
	 * @param value
	 * @return
	 */
	public static String eraseZeroCoveredMinus(String value) {
		if (value == null) {
			return "";
		}

		int index = 0;
		value = value.trim();
		boolean isMinus = value.startsWith("-");

		for (int i = 0; i < value.length(); i++) {
			if(i==0 && isMinus) {
				index++;
				continue;
			}
			if (value.charAt(i) != '0') {
				break;
			}
			index++;
		}
		if(isMinus) {
			return "-" + value.substring(index);
		} else {
			return value.substring(index);
		}
	}

	/**
	 *
	 * 특정 문자열의 왼편에 붙어있는 '0'들을 제거하여 반환함<br>
	 * 로직중복으로 인해서 내용 제거하고 eraseZero() 메소드 호출로 대체
	 *
	 * @param src
	 * @return
	 */
	public static String ltrimZero(final String src) {
		return eraseZero(src);
	}

	/**
	 *
	 * 전달받은 문자열에 지정된 길이에 맞게 0을 채운다
	 *
	 * @param value
	 * @param size
	 * @return
	 */
	public static String fillZero(String value, final int size) {
		if (StringUtils.isEmpty(value)) {
			return "";
		}

		int count = size - value.getBytes().length;

		for (int i = 0; i < count; i++) {
			value = "0" + value;
		}

		return value;
	}

	/**
	 *
	 * 전달받은 문자열에서 지정한 문자열을 제거한 문자열을 리턴한다.
	 *
	 * <pre>
	 *
	 * </pre>
	 *
	 * @param des
	 *            변환시킬 문자열
	 * @param del
	 *            삭제할 문자열
	 * @return
	 */
	public static String getRemoveChar( final String des, final String del )
	{
		StringTokenizer t = new StringTokenizer( des, del );
		StringBuffer str = new StringBuffer();

		while ( t.hasMoreTokens() ) {
			str.append( t.nextToken() );
		}

		return str.toString();
	}

	/**
	 *
	 * 전달받은 문자열에 space를 넣어서 지정한 길이를 만든다<br>
	 * static 메소드인 fillSpace()를 호출함.
	 *
	 * @param des
	 * @param size
	 * @return
	 */
	public static String getFillSpace(final String des, final int size) {
		return fillSpace(des, size);
	}

	/**
	 *
	 * 전달받은 문자열에 space를 넣어서 지정한 길이를 만든다
	 *
	 * @param des
	 * @param size
	 * @return
	 */
	public static String fillSpace(String des, final int size) {
		StringBuffer str = new StringBuffer();
		// des = replace(des, " ", " ");

		if (des == null) {
			for (int i = 0; i < size; i++) {
				str.append(" ");
			}
			return str.toString();
		}

		if (des.trim().length() > size) {
			return des.substring(0, size);
		} else {
			des = des.trim();
		}

		byte[] bDes = des.getBytes();
		int diffsize = size - bDes.length;
		str.append(des);

		for (int i = 0; i < diffsize; i++) {
			str = str.append(" ");
		}

		return str.toString();
	}

	/**
	 *
	 * 전달받은 문자열에 지정된 길이에 맞게 0을 채운다<br>
	 *
	 * @param des
	 * @param size
	 * @return
	 */
	public static String getFillZero(String des, final int size) {
		StringBuffer str = new StringBuffer();

		if (des == null) {
			for (int i = 0; i < size; i++) {
				str.append("0");
			}
			return str.toString();
		}

		if (des.trim().length() > size) {
			return des.substring(0, size);
		} else {
			des = des.trim();
		}

		int diffsize = size - des.length();

		for (int i = 0; i < diffsize; i++) {
			str = str.append("0");
		}

		str.append(des);

		return str.toString();
	}



	/**
	 *
	 * 전달받은 문자열이 null값이거나 빈문자열("")일 경우 참을 반환함
	 *
	 * <pre>
	 * StringUtil.isNull(null)      = true
	 * StringUtil.isNull("")        = true
	 * StringUtil.isNull(" ")       = false
	 * StringUtil.isNull("bob")     = false
	 * StringUtil.isNull("  bob  ") = false
	 * </pre>
	 *
	 * @param value
	 * @return
	 */
	public final static boolean isNull(final String value) {
		return StringUtils.isEmpty(value);
	}

	/**
	 *
	 * 전달받은 문자열이 null이거나 "null"문자열이거나 빈문자열("")일 경우 ""를 반환함<br>
	 * StringUtil.defaultString(String)를 권고.
	 *
	 * @deprecated isNulls() -> defaultString()
	 * @param str
	 * @return
	 */
	@Deprecated
	public static String isNulls(final String str) {
		String return_str = str;
		if (StringUtils.isEmpty(str) || "null".equals(str)) {
			return_str = "";
		}
		return return_str;
	}

	/**
	 *
	 * 전달받은 문자열을 숫자로 변환하여 반환하되, 숫자가 아니라면 빈문자열("")을 반환함.
	 *
	 * @param value
	 * @return
	 */
	public final static String getNumber(final String value) {
		if ( isBlank(value) ) {
			return "";
		}

		try {
			return Integer.parseInt(value.trim()) + "";
		} catch (NumberFormatException e) {
			throw e;
		}
	}

	/**
	 *
	 * 전달받은 문자열이 한칸의 빈칸일 경우 (" ") 참을 반환함
	 *
	 * @param value
	 * @return
	 */
	public final static boolean isSpace(final String value) {
		if (value != null && " ".equals(value)) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * 인자로 전달받은 데이터가 올바른 날짜 데이터인지 검사한다
	 *
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static boolean isValidDate(final String year, final String month, final String date) {
		if (year == null || year.length() != 4 || !isNumeric(year)) {
			return false;
		}

		if (month == null || month.length() > 2 || month.length() <= 0
				|| !isNumeric(month)) {
			return false;
		}

		if (date == null || date.length() > 2 || date.length() <= 0
				|| !isNumeric(date)) {
			return false;
		}

		return true;
	}

	/**
	 *
	 * null값이나 문자열 "null"일 경우 빈 문자열("") 로 변환하여 반환함
	 *
	 * @deprecated replaceNull() -> defaultString()
	 * @param str
	 * @return
	 */
	@Deprecated
	public static String replaceNull(final String str) {
		String result = str;

		if (str == null || "null".equals(str)) {
			result = "";
		}

		return result;
	}

	/**
	 *
	 * 스트링 치환 함수 주어진 문자열(buffer)에서 특정문자열('src')를 찾아 특정문자열('dst')로 치환
	 *
	 * @param buffer
	 * @param src
	 * @param dst
	 * @return
	 */
	public static String replaceAll(final String buffer, final String src, final String dst) {
		if (buffer == null) {
			return null;
		}
		if (buffer.indexOf(src) < 0) {
			return buffer;
		}

		int bufLen = buffer.length();
		int srcLen = src.length();
		StringBuffer result = new StringBuffer();

		int i = 0;
		int j = 0;
		for (; i < bufLen;) {
			j = buffer.indexOf(src, j);
			if (j >= 0) {
				result.append(buffer.substring(i, j));
				result.append(dst);

				j += srcLen;
				i = j;
			} else {
				break;
			}
		}
		result.append(buffer.substring(i));
		return result.toString();
	}



	/**
	 *
	 * 전달받은 문자열을 가격표시 형태대로 3자리마다 ',' 삽입하여 반환함
	 *
	 * @param dStr
	 * @return
	 */
	public static String getCurrDisplay(String dStr) {
		if (dStr == null) {
			return "";
		}

		String sep = ",";
		int sLoc;

		sLoc = dStr.length();

		while (sLoc > 3) {
			dStr = dStr.substring(0, sLoc - 3) + sep + dStr.substring(sLoc - 3);
			sLoc -= 3;
		}

		return dStr;
	}



	/**
	 *
	 * 휴대폰 유효성 검사
	 *
	 * @param sSvcNum
	 * @return 0(성공), 2009(실패)
	 */
	public static String getSvcNumCheck(final String sSvcNum){

		//boolean bResult = false;
		String sResult="2009";
		boolean bCk=false;
		String sTemp="";

		if(!sSvcNum.startsWith("0100")){
			//휴대전화 자리수 체크

			if((sSvcNum.length() >9) && (sSvcNum.length() <12) ){

				//국번 체크
				for(int i=0 ; i< EXCHANGE_NO_LIST.size();i++){
					sTemp= (String) EXCHANGE_NO_LIST.get( i );
					if(sTemp.equals(sSvcNum.substring(0,3))){
						bCk= true ;
						break;
					}
				}

				if(bCk){
					//숫자 체크
					for(int i=0; i<sSvcNum.length(); i++) {
						char c = sSvcNum.charAt(i);
						if(Character.isDigit(c) == true) {
							sResult="0";
						} else {
							break;
						}

					}
				}

			}

		}  // end of first if
		return sResult;

	}
	/**
	 * Method cropByte. 문자열 바이트수만큼 끊어주고, 생략표시하기
	 * @param str 문자열
	 * @param i 바이트수
	 * @param trail 생략 문자열. 예) "..."
	 * @return String
	 */
	public static String cropByte(final String str, final int i, final String trail) {
		if (str==null) {
			return "";
		}
		String tmp = str;
		int slen = 0, blen = 0;
		char c;
		try {
			//if(tmp.getBytes("MS949").length>i) {//2-byte character..
			if(tmp.getBytes("UTF-8").length>i) {//3-byte character..
				while (blen+1 < i) {
					c = tmp.charAt(slen);
					blen++;
					slen++;
					if ( c  > 127 ) {
						blen++;
					}
				}
				tmp=tmp.substring(0,slen)+trail;
			}
		} catch(Exception e) {
			log.debug(e.getMessage());
		}
		return tmp;
	}


	/**
	 * XSS 보안
	 * @param src
	 * @return
	 */
	public static String escapeXss(final String src) {

		//null일때는 그대로 리턴
		if(src == null) {
			return null;
		}

		int length = src.length();
		String comp = "";
		StringBuffer buffer = new StringBuffer("");
		for (int i=0; i<length; i++) {
			comp = src.substring(i, i+1);
			if ("\"".compareTo(comp) == 0) {
				comp = src.substring(i, i);
				buffer.append("&quot;");
			} else if ("\\".compareTo(comp) == 0) {
				comp = src.substring(i, i);
				buffer.append("￦");
			} else if ("<".compareTo(comp) == 0) {
				comp = src.substring(i, i);
				buffer.append("&lt;");
			} else if (">".compareTo(comp) == 0) {
				comp = src.substring(i, i);
				buffer.append("&gt;");
			}
			buffer.append(comp);
		}
		return buffer.toString();
	}


	/**
	 * Base64 Decode
	 * @param src
	 * @return
	 */
	public static String decodeBase64(final String src) {

		String result = "";

		if(src !=null){
			byte [] decoded = Base64.decodeBase64(src.getBytes());
			result = new String(decoded);
		}

		return result;
	}

	/**
	 *  받은 문자열의 숫자여부 체크와 받은 길이가 일치한지 확인하여 리턴해준다.
	 *    param src 문자열
	 *    param length 길이
	 * @return
	 */
	public static boolean getNumberChk(final String src, final int length)
	{
		boolean rv = false;

		// 받은 문자열이 null이거나 공백이면  false를 리턴한다. 단 길이(length)가 0이면 길이 체크를 하지 않는다.
		if( src == null || "".equals(src) ) {
			return false;
		} else
		{
			// 그리고 받은 길이와 일치하지 않으면 false를 리턴한다. 단 길이(length)가 0이면 길이 체크를 하지 않는다.
			if(length !=0 && src.length() != length) {
				return false;
			}
		}

		// 받은 문자열의 숫자 검증을 한다.
		for(int i=0; i<src.length(); i++) {
			if(Character.isDigit(src.charAt(i)) )
			{
				rv = true;
			}
			else
			{
				rv = false;
				break;
			}
		}
		return rv;
	}

	/**
	 *
	 * <pre>
	 * Post로 넘어오는 Parameter를 이름과 값을을 구분하여 저장
	 * </pre>
	 *
	 * @param String  Parameter 구분 PostParam인지 MainParam
	 * @param String  PostParam의 value
	 * @param String  PostParam의 value
	 * @return Hashtable
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable divideParameter( final String ParamNm, final String src, final String Divider)
	{
		Hashtable hsPostParam = new Hashtable();
		String TmpArray[] = null;
		String Tmp1Array[] = null;

		//hsPostParam.put( ParamNm, src );

		try
		{
			TmpArray = src.split( Divider );
			if ( TmpArray.length > 0 ) {

				for ( int i = 0; i < TmpArray.length; i++) {
					Tmp1Array   =   TmpArray[i].split("=");

					if ( Tmp1Array.length > 1 ) {
						hsPostParam.put(Tmp1Array[0], Tmp1Array[1]);
					} else if ( Tmp1Array.length == 1 ) {
						hsPostParam.put(Tmp1Array[0], "");
					}
				}
			}
		}
		catch( Exception e ) {
			log.debug(e.getMessage());
		}
		return hsPostParam;
	}

	/**
	 * 일반전화번호, 휴대폰전화번호의 가운데 자리를 4자리로 만들어 반환
	 *
	 * <pre>
	 * StringUtil.getFillSvcNum("0117778888")
	 * </pre>
	 *
	 * @param svcNum
	 * @return String
	 */
	public static String getFillSvcNum(final String svcNum){
		String[] resultArr = getSplitSvcNum(svcNum);

		if(resultArr[1].length() == 3){
			resultArr[1] = "0"+resultArr[1];
		}

		String result = resultArr[0] + resultArr[1] + resultArr[2];

		return result;
	}

	/**
	 * 일반전화번호, 휴대폰전화번호를 자리별로 분리한다.
	 *
	 * <pre>
	 * StringUtil.getSplitSvcNum("050612345678")
	 * </pre>
	 *
	 * @param svcNum
	 * @return String[]
	 */
	public static String[] getSplitSvcNum(final String svcNum){
		String[] resultArr = new String[]{"", "", ""};
		String regExp = "^(01\\d{1}|02|0502|0505|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})";

		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(StringUtil.defaultString(svcNum));

		if(matcher.matches()){
			resultArr[0] = matcher.group(1);
			resultArr[1] = matcher.group(2);
			resultArr[2] = matcher.group(3);
		}

		return resultArr;
	}

	/**
	 *
	 * <pre>
	 * Post로 넘어오는 Parameter를 이름과 값을을 구분하여 저장
	 * </pre>
	 *
	 * @param String  PostParam의 value
	 * @param String  PostParam의 value
	 * @return POST true, 이외 false
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable divideParameter( final String src, final String Divider)
	{
		Hashtable hsPostParam = new Hashtable();
		String TmpArray[] = null;
		String Tmp1Array[] = null;

		//hsPostParam.put( "PostParam", src );

		try
		{
			TmpArray = src.split( Divider );
			if ( TmpArray.length > 0 ) {

				for ( int i = 0; i < TmpArray.length; i++) {
					Tmp1Array   =   TmpArray[i].split("=");

					if ( Tmp1Array.length > 1 ) {
						hsPostParam.put(Tmp1Array[0], Tmp1Array[1]);
					} else if ( Tmp1Array.length == 1 ) {
						hsPostParam.put(Tmp1Array[0], "");
					}
				}
			}
		}
		catch( Exception e ) {
			log.debug(e.getMessage());
		}
		return hsPostParam;
	}

	/**
	 *
	 * <pre>
	 * Post로 넘어오는 Parameter를 이름과 값을을 구분하여 저장
	 * </pre>
	 *
	 * @param String  PostParam의 value
	 * @return POST true, 이외 false
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable divideParameter( final String src)
	{
		Hashtable hsPostParam = new Hashtable();
		String TmpArray[] = null;
		String Tmp1Array[] = null;

		//hsPostParam.put( "PostParam", src );

		if ( StringUtils.isEmpty(src) ) {
			return hsPostParam;
		}
		try
		{
			TmpArray = src.split( "&" );
			if ( TmpArray.length > 0 ) {

				for ( int i = 0; i < TmpArray.length; i++) {
					Tmp1Array   =   TmpArray[i].split("=");

					if ( Tmp1Array.length > 1 ) {
						hsPostParam.put(Tmp1Array[0], Tmp1Array[1]);
					} else if ( Tmp1Array.length == 1 ) {
						hsPostParam.put(Tmp1Array[0], "");
					}
				}
			}
		}
		catch( Exception e ) {
			log.debug(e.getMessage());
		}
		return hsPostParam;
	}


	/**
	 *
	 * <pre>
	 * 넘겨온 자릿수 만큼 숫자발생
	 * </pre>
	 *
	 * @param String  PostParam의 value
	 * @return POST true, 이외 false
	 */
	public static String makeRandownNum(final int makeNumLength)
	{
		int iArr[] = new int[makeNumLength];
		String sArr[] = new String[makeNumLength + 1];
		sArr[makeNumLength] = "";
		for(int i = 0; i < makeNumLength; i++)
		{
			iArr[i] = (int)(Math.random() * 10D);
			sArr[i] = String.valueOf(iArr[i]);
			sArr[makeNumLength] = sArr[makeNumLength] + sArr[i];
		}

		return sArr[makeNumLength];
	}

	/**
	 * <pre>
	 * javascript unescape 대체
	 * </pre>
	 */
	public static String unescape(final String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String getEncodedParam(final HttpServletRequest req, final String key) {
		String rtnStr = req.getParameter(key);
		if(rtnStr!=null && rtnStr.length() > 0) {
			try {
				rtnStr = URLEncoder.encode(req.getParameter(key), req.getCharacterEncoding());
			} catch (UnsupportedEncodingException e) {
				log.debug(e.getMessage());
			}
		}
		return rtnStr;
	}


	//전화번호 12자리 -> 필요없는 앞자리 제거
	public static String getTelSvcNum(final String sSvcNum )
	{
		//앞자리가 4자리인 번호
		String[] AREACD4 = {"0130", "0131", "0132", "0502", "0504", "0505", "0506"};

		String svcNum = StringUtil.defaultString(sSvcNum).trim() ;

		if ( svcNum.length() == 11 ) {
			svcNum = "0"+svcNum;
		}

		if ( svcNum.length() == 10 ) {
			svcNum = "00"+svcNum;
		}

		if ( svcNum == null || "".equals( svcNum ) || svcNum.length() != 12 ) {
			return "";
		}

		String sSvcNum1 = svcNum.substring(0, 4);
		//앞자리가 4자리번호와 같으로 앞자리는 그대로 리턴
		if(ArrayUtils.contains(AREACD4, sSvcNum1)) {
			return sSvcNum;
		}

		String sSvcNum2 = svcNum.substring(4, 8);
		String sSvcNum3 = svcNum.substring(8, 12);
		String retNum = "";
		String retNum1 = "";

		for(int i=1;i<5;i++) // 지역번호 추출
		{
			if(Integer.parseInt(sSvcNum1.substring(0,i))!= 0)
			{
				retNum1 = sSvcNum1.substring(i-2,4);
				i=5;
			}
		}

		retNum = retNum1 + sSvcNum2 + sSvcNum3;
		return retNum;
	}
	
	//카드번호 마스킹 처리
	public static String getMaskingCardNum(String cardNum) {
		
		if(cardNum != null) {
			String[] cardNums = cardNum.split("");
			
			for(int i=0; i< cardNums.length; i++) {
				if(i==10 || i==12 || i==15)
					cardNums[i] = "*";
			}
			
			cardNum = String.join("", cardNums);
		}
		
		 return cardNum;
	}

	/**
	 * 16 카드번호에서 앞 10자리만 가져온다.
	 * 16자리가 아닌경우 null
	 * 
	 * @param memberCardNum16
	 * @return
	 */
	public static String getCardNum1(String memberCardNum16) {
		if ( memberCardNum16 == null ) {
			return null;
		}
		memberCardNum16 = memberCardNum16.trim();
		if ( memberCardNum16.length() != 16 ) {
			return null;
		}
		
		return memberCardNum16.substring(0, 10);
	}

	/**
	 * 16 카드번호에서 뒤 6자리만 가져온다.
	 * 
	 * @param memberCardNum16
	 * @return
	 */
	public static String getCardNum2(String memberCardNum16) {
		if ( memberCardNum16 == null ) {
			return null;
		}
		memberCardNum16 = memberCardNum16.trim();
		if ( memberCardNum16.length() != 16 ) {
			return null;
		}
		
		return memberCardNum16.substring(10);
	}

	public static boolean isNotBlank(String val) {
		return !isBlank(val);
	}
	
	
	public static String getFileExtName(String filePath) {
		if ( filePath == null) {
			return null;
		}
		
		int lastIndex = filePath.lastIndexOf(".");
		if ( lastIndex < 0) {
			return null;
		}
		
		return filePath.substring(lastIndex + 1);
	}
	 
}