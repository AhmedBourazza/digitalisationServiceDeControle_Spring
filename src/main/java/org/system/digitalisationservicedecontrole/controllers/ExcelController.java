package org.system.digitalisationservicedecontrole.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.system.digitalisationservicedecontrole.services.ExcelService;

import java.io.IOException;
@RestController
public class ExcelController {

    @Autowired
    ExcelService excelService;

    @GetMapping("/excel")
    public void generateExcel(@RequestParam("dateControle") String dateControle,
                              @RequestParam("matricule") String matricule,
                              HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=formulaire.xls");
        excelService.generateExcel(dateControle, matricule, response);
    }
}



