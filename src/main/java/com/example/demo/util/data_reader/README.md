# Data File Stream Reader

	Xlsx, Xls, Csv 파일 형식을 한개의 인터페이스로 streaming 으로 읽어서 처리할 수 있도록 하는 프로그램.

	// 엑셀파일poi
	implementation ('org.apache.poi:poi:4.1.2')
	implementation ('org.apache.poi:poi-ooxml:4.1.2')
	implementation 'org.apache.commons:commons-csv:1.8'

	rowSize 를 제공하여 특정 개수단위로 처리할 수 있는 메소드만을 제공한다.
	v0.1 으로 특정 기능을 제공하기 위해서 만들었으므로, 단순하게 특정 목적을 위한 기능만 있다.
	