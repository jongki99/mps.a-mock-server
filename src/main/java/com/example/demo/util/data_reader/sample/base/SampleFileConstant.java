package com.example.demo.util.data_reader.sample.base;

public class SampleFileConstant {
	public static final String projectRootDir = System.getProperty("user.dir");
	public static final String fileDataDir = projectRootDir + "/src/main/resources/data_reader/";

	public static class CSV {
		public static final String g_100 = fileDataDir + "g_100.csv";
		public static final String temp_data_6_columns = fileDataDir + "temp_data_6_columns.csv";;
	}
	public static class XLS {
		public static final String file_example_XLS_10 = fileDataDir + "file_example_XLS_10.xls";
		public static final String file_example_XLS_5000 = fileDataDir + "file_example_XLS_5000.xls";
		public static final String file_example_XLS_20000_emptyCell = fileDataDir + "202408_Baemin_VIPPICK.xls";
		public static final String file_example_XLS_20000_emptyRow = fileDataDir + "Dalkomm_2408_americano.xls";
	}
	public static class XLSX {
		public static final String temp_data_6_columns_20 = fileDataDir + "temp_data_6_columns_20.xlsx";
		public static final String temp_data_6_columns = fileDataDir + "temp_data_6_columns.xlsx";
		public static final String temp_data_20 = fileDataDir + "temp_data_20.xlsx";
		public static final String test_20 = fileDataDir + "test_20.xlsx";
		public static final String test_20_2 = fileDataDir + "test_20-2.xlsx";
	}
}
