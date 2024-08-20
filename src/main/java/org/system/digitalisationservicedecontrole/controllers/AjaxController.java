package org.system.digitalisationservicedecontrole.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.system.digitalisationservicedecontrole.repositories.UniteRepo;
import org.system.digitalisationservicedecontrole.entities.Unite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ajax")
public class AjaxController {
    private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);

    private final UniteRepo uniteRepo;

    @Autowired
    public AjaxController(UniteRepo uniteRepo) {
        this.uniteRepo = uniteRepo;
    }

    @GetMapping("/unites")
    public ResponseEntity<List<Map<String, Object>>> getUnitesByEntite(@RequestParam("idEntite") Long idEntite) {
        logger.info("Requête AJAX reçue pour les unités avec idEntite: {}", idEntite);
        try {
            List<Object[]> results = uniteRepo.findUnitesByEntiteId(idEntite);
            List<Map<String, Object>> unites = results.stream()
                    .map(row -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("idUnite", row[0]);
                        map.put("nom", row[1]);
                        return map;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(unites);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des unités pour idEntite: {}", idEntite, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
