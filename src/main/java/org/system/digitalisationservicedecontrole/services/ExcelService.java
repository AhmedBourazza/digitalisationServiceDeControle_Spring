package org.system.digitalisationservicedecontrole.services;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.system.digitalisationservicedecontrole.repositories.ExcelRepo;

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

            // General Information Styles (Couleur #9AC8EB)
            CellStyle generalInfoStyle = workbook.createCellStyle();
            HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
            short generalInfoColorIndex = palette.findSimilarColor(154, 200, 235).getIndex(); // RGB for #9AC8EB
            generalInfoStyle.setFillForegroundColor(generalInfoColorIndex);
            generalInfoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            generalInfoStyle.setAlignment(HorizontalAlignment.CENTER);
            Font generalInfoFont = workbook.createFont();
            generalInfoFont.setColor(IndexedColors.WHITE.getIndex());
            generalInfoFont.setBold(true);
            generalInfoStyle.setFont(generalInfoFont);

            // Section Title Styles (Couleur #BED3C3)
            CellStyle sectionTitleStyle = workbook.createCellStyle();
            short sectionTitleColorIndex = palette.findSimilarColor(190, 211, 195).getIndex(); // RGB for #BED3C3
            sectionTitleStyle.setFillForegroundColor(sectionTitleColorIndex);
            sectionTitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            sectionTitleStyle.setAlignment(HorizontalAlignment.CENTER);
            Font sectionTitleFont = workbook.createFont();
            sectionTitleFont.setBold(true);
            sectionTitleStyle.setFont(sectionTitleFont);

            // Table Header Styles (Couleur #226D68)
            CellStyle headerStyle = workbook.createCellStyle();
            short headerColorIndex = palette.findSimilarColor(34, 109, 104).getIndex(); // RGB for #226D68
            headerStyle.setFillForegroundColor(headerColorIndex);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);

            // Data Row Styles (Couleur #ECF8F6)
            CellStyle dataRowStyle = workbook.createCellStyle();
            short dataRowColorIndex = palette.findSimilarColor(236, 248, 246).getIndex(); // RGB for #ECF8F6
            dataRowStyle.setFillForegroundColor(dataRowColorIndex);
            dataRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Border Style
            CellStyle borderStyle = createBorderStyle(workbook);

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
            generalInfoRow.createCell(1).setCellValue(getUniqueValueFromResults(results, 7));
            // Replace with actual column index for 'Controleur'
            generalInfoRow.getCell(1).setCellStyle(generalInfoStyle);

            generalInfoRow = sheet.createRow(rowIndex++);
            generalInfoRow.createCell(0).setCellValue("Accompagnant");
            generalInfoRow.getCell(0).setCellStyle(generalInfoStyle);
            generalInfoRow.createCell(1).setCellValue(getUniqueValueFromResults(results, 8)); // Replace with actual column index for 'Propriétaire'
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
                    headerRow.createCell(0).setCellValue("Sous section");
                    headerRow.createCell(1).setCellValue("Question");
                    headerRow.createCell(2).setCellValue("Reponse");


                    for (Cell cell : headerRow) {
                        cell.setCellStyle(headerStyle);
                    }
                }

                // Add data rows
                Row dataRow = sheet.createRow(dataRowIndex++);
                dataRow.createCell(0).setCellValue((String) row[6]);
                dataRow.createCell(1).setCellValue((String) row[4]);
                dataRow.createCell(2).setCellValue((String) row[5]);

                for (Cell cell : dataRow) {
                    cell.setCellStyle(dataRowStyle);
                }
            }

            // Apply borders to all cells
            applyBorders(sheet, borderStyle);

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
        ClassPathResource resource = new ClassPathResource("static/img/excelImage.PNG");
        try (InputStream is = resource.getInputStream()) {
            byte[] imageBytes = is.readAllBytes();
            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(0);
            anchor.setCol2(31);
            anchor.setRow2(3);

            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize();
        }
    }

    private String getUniqueValueFromResults(List<Object[]> results, int index) {
        if (!results.isEmpty()) {
            return (String) results.get(0)[index];
        }
        return "";
    }
    private CellStyle createBorderStyle(Workbook workbook) {
        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        borderStyle.setBorderRight(BorderStyle.THIN);
        borderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return borderStyle;
    }


    private void applyBorders(Sheet sheet, CellStyle borderStyle) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellStyle(borderStyle);
            }
        }
    }
}
