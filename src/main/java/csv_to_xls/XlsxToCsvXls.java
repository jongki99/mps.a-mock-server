package csv_to_xls;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XlsxToCsvXls {

    static {
        // SAXParserFactory를 Xerces로 명시적으로 설정
        System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
    }
    
    public static void convert(String folderPath) throws IOException {
        Files.walk(Paths.get(folderPath))
            .filter(Files::isRegularFile)
            .filter(path -> path.toString().endsWith(".xlsx"))
            .forEach(path -> {
                try {
                	log.debug("file path={}", path.toFile().getAbsolutePath());
                    convertToCsvAndXls(path.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    private static void convertToCsvAndXls(File xlsxFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(xlsxFile);
             Workbook workbook = new XSSFWorkbook(fis)) {
            File csvFile = new File(xlsxFile.getParent(), xlsxFile.getName().replace(".xlsx", ".csv"));
            writeToCsv(workbook, csvFile);

            File xlsFile = new File(xlsxFile.getParent(), xlsxFile.getName().replace(".xlsx", ".xls"));
            writeToXls(workbook, xlsFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다: " + xlsxFile.getName(), e);
        }
    }

    private static void writeToCsv(Workbook workbook, File csvFile) throws IOException {
        DataFormatter formatter = new DataFormatter();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                StringBuilder rowString = new StringBuilder();
                for (Cell cell : row) {
                    rowString.append(formatter.formatCellValue(cell)).append(",");
                }
                writer.write(rowString.toString().replaceAll(",$", ""));
                writer.newLine();
            }
        }
    }

    private static void writeToXls(Workbook workbook, File xlsFile) throws IOException {
        try (HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(xlsFile)) {

            Sheet xlsxSheet = workbook.getSheetAt(0);
            org.apache.poi.hssf.usermodel.HSSFSheet hssfSheet = hssfWorkbook.createSheet();

            int rowNum = 0;
            Iterator<Row> rowIterator = xlsxSheet.iterator();

            while (rowIterator.hasNext() && rowNum < 65000) {
                Row xlsxRow = rowIterator.next();
                org.apache.poi.hssf.usermodel.HSSFRow hssfRow = hssfSheet.createRow(rowNum++);

                int cellNum = 0;
                for (Cell cell : xlsxRow) {
                    org.apache.poi.hssf.usermodel.HSSFCell hssfCell = hssfRow.createCell(cellNum++);
                    switch (cell.getCellType()) {
                        case STRING:
                            hssfCell.setCellValue(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            hssfCell.setCellValue(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            hssfCell.setCellValue(cell.getBooleanCellValue());
                            break;
                        default:
                            hssfCell.setCellValue(cell.toString());
                            break;
                    }
                }
            }

            hssfWorkbook.write(fos);
        }
    }

    public static void main(String[] args) {
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
//        if (args.length != 1) {
//            System.out.println("사용법: java XlsxToCsvXls <폴더 경로>");
//            return;
//        }
//
//        String folderPath = args[0];
        String folderPath = "/Users/P170355/Downloads/0000-ticket-temp/cpnPin";

        try {
            convert(folderPath);
            System.out.println("변환 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}