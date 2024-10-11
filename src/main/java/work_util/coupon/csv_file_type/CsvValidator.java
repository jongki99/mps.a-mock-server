package work_util.coupon.csv_file_type;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * fail test program.
 */
public class CsvValidator {

	public static void main(String[] args) {
		String parentPath = "/Users/P170355/Downloads/0000-sb/";
		String filePath = parentPath + "hiddencpn_user_1_unhashed.csv"; // CSV 파일 경로를 지정하세요.
		int expectedLength = 10; // 각 라인의 예상 길이

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			boolean isFirstLine = true;
			boolean hasHeader = false;

			while ((line = br.readLine()) != null) {
				if (isFirstLine) {
					hasHeader = isHeader(line);
					isFirstLine = false;
					System.out.println("헤더가 있습니다: " + hasHeader);
				}

				if (line.length() != expectedLength) {
					System.out.println("잘못된 길이의 라인 발견: " + line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 헤더 여부를 간단히 판단하기 위한 메소드 (예: 숫자가 아닌 문자로 구성된 경우)
	private static boolean isHeader(String line) {
		// 첫 줄이 숫자로만 이루어지지 않았으면 헤더라고 가정
		String[] columns = line.split(",");
		try {
			for (String col : columns) {
				Integer.parseInt(col);
			}
			return false; // 모든 값이 숫자면 헤더가 아님
		} catch (NumberFormatException e) {
			return true; // 숫자가 아니면 헤더라고 가정
		}
	}
}