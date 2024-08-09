package work_util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EclipseLogClearUtil {

    public static void main(String[] args) {
        // 폴더 경로를 입력하세요
        String folderPath = "/Users/P170355/Downloads/0000-ticket-temp/eclipse_log_clear"; // 여기에 실제 폴더 경로를 입력하세요

        // 폴더에서 .log 및 .txt 파일 찾기
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".log") || name.endsWith(".txt"));

        if (files != null) {
        	String patterns = getCombinedPattern();
            for (File file : files) {
                processFile(file, patterns);
            }
        } else {
            System.out.println("지정한 폴더에서 파일을 찾을 수 없습니다.");
        }
    }

    private static String getCombinedPattern() {
        List<String> patternString = new ArrayList<>();
        
        patternString.add("\\[2m");
        patternString.add("\\[32m");
        patternString.add("\\[35m");
        patternString.add("\\[36m");
        patternString.add("\\[0;39m");

        // 패턴 문자열을 '|'로 결합
        return patternString.stream().collect(Collectors.joining("|"));
    }

    private static void processFile(File file, String patterns) {
        String outputFileName = file.getName()+".clear";
        File outputFile = new File(file.getParent(), outputFileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            Pattern specialCharPattern = Pattern.compile(patterns);

            while ((line = reader.readLine()) != null) {
                // 특수문자 제거
                String cleanLine = specialCharPattern.matcher(line).replaceAll("");
                writer.write(cleanLine);
                writer.newLine();
            }

            System.out.println("처리 완료: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}