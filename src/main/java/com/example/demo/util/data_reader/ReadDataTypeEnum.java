package com.example.demo.util.data_reader;

public enum ReadDataTypeEnum {
	XLSX,
	XLS,
	CSV,
	;

	/**
	 * 타입명에 맞는 확장자명으로 데이터가 들어오면, 해당 타입을 반환한다.
	 * 
	 * @param extName
	 * @return
	 */
	public ReadDataTypeEnum fromExt(String extName) {
		if ( extName != null ) {
			for (ReadDataTypeEnum data : ReadDataTypeEnum.values()) {
				if ( data.name().equals(extName) ) {
					return data;
				}
			}
		}
		return null;
	}
}
