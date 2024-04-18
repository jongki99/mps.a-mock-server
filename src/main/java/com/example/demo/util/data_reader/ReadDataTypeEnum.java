package com.example.demo.util.data_reader;

import com.example.demo.util.ext_coupon.StringUtil;

public enum ReadDataTypeEnum {
	XLSX,
	XLS,
	CSV,
//	TSV,
	;

	/**
	 * 타입명에 맞는 확장자명으로 데이터가 들어오면, 해당 타입을 반환한다.
	 * 
	 * @param extName
	 * @return
	 */
	public static ReadDataTypeEnum fromExt(String extName) {
		if ( extName != null ) {
			for (ReadDataTypeEnum data : ReadDataTypeEnum.values()) {
				if ( data.name().equals(extName) ) {
					return data;
				}
			}
		}
		return null;
	}

	public static ReadDataTypeEnum getReadDataTypeEnum(String filePath) {
		ReadDataTypeEnum re = null;
		if ( filePath != null ) {
			if ( filePath.length() < 5 ) { // 확장자만 넣은것으로 간주해서 일단 처리.
				re = fromExt(filePath.toUpperCase());
			}
			
			if ( re == null ) {
				String ext = StringUtil.getFileExtName(filePath);
				if ( ext != null ) {
					re = fromExt(ext.toUpperCase());
				}
			}
		}
		return re;
	}
}
