package org.system.digitalisationservicedecontrole.services;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.system.digitalisationservicedecontrole.repositories.ExcelRepo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private ExcelRepo excelRepo;

    public void generateExcel(String dateControle, String matricule, HttpServletResponse response) throws IOException {
        List<Object[]> results = excelRepo.findReponses(matricule, dateControle);
        if (results.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Aucun formulaire trouvé pour la date et le matricule spécifiés.\"}");
        } else {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("form");

            // Add image to the sheet
            addImageToSheet(sheet, workbook);

            // General Information Styles
            CellStyle generalInfoStyle = workbook.createCellStyle();
            HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
            short generalInfoColorIndex = palette.findSimilarColor(157, 184, 231).getIndex();
            generalInfoStyle.setFillForegroundColor(generalInfoColorIndex);
            generalInfoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            generalInfoStyle.setAlignment(HorizontalAlignment.CENTER);
            Font generalInfoFont = workbook.createFont();
            generalInfoFont.setColor(IndexedColors.WHITE.getIndex());
            generalInfoFont.setBold(true);
            generalInfoStyle.setFont(generalInfoFont);

            // Section Title Styles
            CellStyle sectionTitleStyle = workbook.createCellStyle();
            sectionTitleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            sectionTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            sectionTitleStyle.setAlignment(HorizontalAlignment.CENTER);
            Font sectionTitleFont = workbook.createFont();
            sectionTitleFont.setBold(true);
            sectionTitleStyle.setFont(sectionTitleFont);

            // Table Header Styles
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Data Row Styles (Background color RGB 233, 255, 229)
            CellStyle dataRowStyle = workbook.createCellStyle();
            short dataRowColorIndex = palette.findSimilarColor(233, 255, 229).getIndex();
            dataRowStyle.setFillForegroundColor(dataRowColorIndex);
            dataRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // General Information (after image)
            int rowIndex = 6; // Start after the image space
            Row generalInfoRow = sheet.createRow(rowIndex++);
            generalInfoRow.createCell(0).setCellValue("Date de controle");
            generalInfoRow.getCell(0).setCellStyle(generalInfoStyle);
            generalInfoRow.createCell(1).setCellValue(dateControle);
            generalInfoRow.getCell(1).setCellStyle(generalInfoStyle);

            generalInfoRow = sheet.createRow(rowIndex++);
            generalInfoRow.createCell(0).setCellValue("Matricule");
            generalInfoRow.getCell(0).setCellStyle(generalInfoStyle);
            generalInfoRow.createCell(1).setCellValue(matricule);
            generalInfoRow.getCell(1).setCellStyle(generalInfoStyle);

            generalInfoRow = sheet.createRow(rowIndex++);
            generalInfoRow.createCell(0).setCellValue("Unité");
            generalInfoRow.getCell(0).setCellStyle(generalInfoStyle);
            generalInfoRow.createCell(1).setCellValue(getUniqueValueFromResults(results, 1)); // Replace with actual column index for 'Unité'
            generalInfoRow.getCell(1).setCellStyle(generalInfoStyle);

            generalInfoRow = sheet.createRow(rowIndex++);
            generalInfoRow.createCell(0).setCellValue("Entité");
            generalInfoRow.getCell(0).setCellStyle(generalInfoStyle);
            generalInfoRow.createCell(1).setCellValue(getUniqueValueFromResults(results, 2)); // Replace with actual column index for 'Entité'
            generalInfoRow.getCell(1).setCellStyle(generalInfoStyle);

            generalInfoRow = sheet.createRow(rowIndex++);
            generalInfoRow.createCell(0).setCellValue("Controleur");
            generalInfoRow.getCell(0).setCellStyle(generalInfoStyle);
            generalInfoRow.createCell(1).setCellValue(getUniqueValueFromResults(results, 7)); // Replace with actual column index for 'Controleur'
            generalInfoRow.getCell(1).setCellStyle(generalInfoStyle);

            rowIndex++;
            int dataRowIndex = rowIndex;
            String currentSection = "";
            for (Object[] row : results) {
                String section = (String) row[3];

                if (!section.equals(currentSection)) {
                    currentSection = section;
                    dataRowIndex++;
                    // Add section title
                    Row sectionTitleRow = sheet.createRow(dataRowIndex++);
                    sectionTitleRow.createCell(0).setCellValue(section);
                    sectionTitleRow.getCell(0).setCellStyle(sectionTitleStyle);

                    // Add table headers
                    Row headerRow = sheet.createRow(dataRowIndex++);
                    headerRow.createCell(0).setCellValue("Question");
                    headerRow.createCell(1).setCellValue("Reponse");
                    headerRow.createCell(2).setCellValue("Justification");

                    for (Cell cell : headerRow) {
                        cell.setCellStyle(headerStyle);
                    }
                }

                // Add data rows
                Row dataRow = sheet.createRow(dataRowIndex++);
                dataRow.createCell(0).setCellValue((String) row[4]);
                dataRow.createCell(1).setCellValue((String) row[5]);
                dataRow.createCell(2).setCellValue((String) row[6]);
                for (Cell cell : dataRow) {
                    cell.setCellStyle(dataRowStyle);
                }
            }

            // Adjust column widths
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to the HTTP response
            try (ServletOutputStream ops = response.getOutputStream()) {
                workbook.write(ops);
            } finally {
                workbook.close();
            }
        }
    }

    private void addImageToSheet(Sheet sheet, Workbook workbook) throws IOException {
        // Load the image file
        try (InputStream is = new FileInputStream("C:\\Users\\pc\\Desktop\\excelImage.PNG")) { // Update path to your image
            byte[] imageBytes = is.readAllBytes();
            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();

            // Define the anchor point for the image
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0); // Column number where image starts
            anchor.setRow1(0); // Row number where image starts
            anchor.setCol2(31); // Column number where image ends
            anchor.setRow2(3); // Row number where image ends

            // Create the picture in the sheet
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(); // Resize the image to fit the cell area
        }
    }

    private String getUniqueValueFromResults(List<Object[]> results, int index) {
        // This method should return a unique value for 'Unité' or 'Entité'
        // Replace with the logic to get the correct value
        if (!results.isEmpty()) {
            return (String) results.get(0)[index];
        }
        return "";
    }
}