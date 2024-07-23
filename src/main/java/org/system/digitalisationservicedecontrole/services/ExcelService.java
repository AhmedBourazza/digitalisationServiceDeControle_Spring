package org.system.digitalisationservicedecontrole.services;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.system.digitalisationservicedecontrole.dto.ExcelDTO;
import org.system.digitalisationservicedecontrole.entities.Excel;
import org.system.digitalisationservicedecontrole.repositories.ExcelRepo;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
@Service
public class ExcelService {

    @Autowired
    private ExcelRepo excelRepo;

    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Object[]> results = excelRepo.findReponses("Mat-1");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("form");

        HSSFRow headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("Date de controle");
        headerRow.createCell(1).setCellValue("Entité");
        headerRow.createCell(2).setCellValue("Unité");
        headerRow.createCell(3).setCellValue("Section");
        headerRow.createCell(4).setCellValue("Question");
        headerRow.createCell(5).setCellValue("Reponse");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int dataRowIndex = 1;
        for (Object[] row : results) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);

           //dataRow.createCell(0).setCellValue("vxcv");
            dataRow.createCell(0).setCellValue(dateFormat.format(row[0]));
            dataRow.createCell(1).setCellValue((String) row[1]);
            dataRow.createCell(2).setCellValue((String) row[2]);
            dataRow.createCell(3).setCellValue((String) row[3]);
            dataRow.createCell(4).setCellValue((String) row[4]);
            dataRow.createCell(5).setCellValue((String) row[5]);

            dataRowIndex++;
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=controleurs.xls");

        try (ServletOutputStream ops = response.getOutputStream()) {
            workbook.write(ops);
        } finally {
            workbook.close();
        }
    }
}
