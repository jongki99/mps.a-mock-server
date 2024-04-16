package com.example.demo.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

public class ExcelColumnMapper {

	public static void mapColumnsToFields(Map<String, String> cellToFields, Map<String, String> dataMap,
			Object targetObject) {
		for (Map.Entry<String, String> cellToField : cellToFields.entrySet()) {
			String cellName = cellToField.getKey();
			String fieldName = cellToField.getValue();
			setField(targetObject, fieldName, dataMap.get(cellName));
		}
	}

	private static void setField(Object targetObject, String fieldName, String columnName) {
		Field field = ReflectionUtils.findField(targetObject.getClass(), fieldName);
		if (field != null) {
			field.setAccessible(true);
			Class<?> fieldType = field.getType();
			Object value = convertValue(columnName, fieldType);
			ReflectionUtils.setField(field, targetObject, value);
		} else {
			// 필드를 찾을 수 없는 경우 예외 처리
			throw new IllegalArgumentException(
					"Field '" + fieldName + "' not found in class " + targetObject.getClass().getName());
		}
	}

	/**
	 * dto type 에 따라서 해당 메서드를 확장해주어야 한다.
	 * 보통 필드이니까. Object 는 없는 것으로... java 기본형으로 매핑할수 있도록 ai 한테 요청했다.
	 * 
	 * @param columnValue
	 * @param fieldType
	 * @return
	 */
	private static Object convertValue(String columnValue, Class<?> fieldType) {
		if (fieldType == int.class || fieldType == Integer.class) {
			return Integer.parseInt(columnValue);
		} else if (fieldType == long.class || fieldType == Long.class) {
			return Long.parseLong(columnValue);
		} else if (fieldType == double.class || fieldType == Double.class) {
			return Double.parseDouble(columnValue);
		} else if (fieldType == float.class || fieldType == Float.class) {
			return Float.parseFloat(columnValue);
		} else if (fieldType == short.class || fieldType == Short.class) {
			return Short.parseShort(columnValue);
		} else if (fieldType == byte.class || fieldType == Byte.class) {
			return Byte.parseByte(columnValue);
		} else if (fieldType == char.class || fieldType == Character.class) {
			// 문자열의 첫 번째 문자를 가져와서 char로 변환
			if (columnValue.length() > 0) {
				return columnValue.charAt(0);
			} else {
				throw new IllegalArgumentException("Cannot convert empty string to char");
			}
		} else {
			// 기타 타입에 대한 처리는 필요에 따라 추가
			return columnValue;
		}
	}

}
