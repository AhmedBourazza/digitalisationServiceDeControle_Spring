package org.system.digitalisationservicedecontrole.services;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.system.digitalisationservicedecontrole.entities.Controleur;
import org.system.digitalisationservicedecontrole.repositories.ControleurRepo;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    ControleurRepo controleurRepo;

    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Controleur> controleurs = controleurRepo.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Controleurs");

        HSSFRow headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("id_controleur");

        headerRow.createCell(1).setCellValue("matricule");
        headerRow.createCell(2).setCellValue("nom");


        headerRow.createCell(3).setCellValue("prenom");


        int dataRowIndex = 1;
        for (Controleur c : controleurs) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);

            dataRow.createCell(0).setCellValue(c.getIdControleur());

            dataRow.createCell(1).setCellValue(c.getMatricule());
            dataRow.createCell(2).setCellValue(c.getNom());

            dataRow.createCell(3).setCellValue(c.getPrenom());

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
