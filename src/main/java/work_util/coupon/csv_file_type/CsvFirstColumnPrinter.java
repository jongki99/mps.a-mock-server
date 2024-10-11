package work_util.coupon.csv_file_type;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvFirstColumnPrinter {

	public static void main(String[] args) {
		String parentPath = "/Users/P170355/Downloads/0000-sb/utf8-BOM/";
		String filePath = parentPath + "noBOM.csv"; // CSV 파일 경로를 지정하세요.
		filePath = parentPath + "withBOM.csv"; // CSV 파일 경로를 지정하세요.
		filePath = parentPath + "euc-kr.csv"; // CSV 파일 경로를 지정하세요.

		try {
			// Reader로 CSV 파일을 읽고 Apache Commons CSV 라이브러리로 파싱
			FileInputStream fileInputStream = new FileInputStream(filePath);
			BOMInputStream bomInputStream = new BOMInputStream(fileInputStream);
			
//			Reader reader = new FileReader(bomInputStream);
			Reader reader = new InputStreamReader(bomInputStream, "UTF-8");
//			Reader reader = new InputStreamReader(bomInputStream, "EUC-KR");
			
			CSVParser csvParser = CSVFormat.DEFAULT.parse(reader);
//			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
			int cnt = 0;
			int limitCount = 10;

			// 각 CSV 레코드를 순회하면서 첫 번째 컬럼 값을 출력
			for (CSVRecord csvRecord : csvParser) {
				cnt++;
				if(cnt > limitCount) {
					log.debug("cnt={}, limitCount={}, 한글={}", cnt, limitCount, csvRecord.get(5));
					break;
				}
				String firstColumnValue = csvRecord.get(0); // 첫 번째 컬럼 값
				log.debug("value={}, length={}, 한글={}", firstColumnValue, firstColumnValue.length(), csvRecord.get(5));
			}

			// 리소스 정리
			csvParser.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}