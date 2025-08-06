package com.example.HOSIX.AGiRH.Controller;

import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import com.example.HOSIX.AGiRH.Service.FusionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fusion")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000") // autorise les appels depuis React
public class FusionController {

    @Autowired
    private FusionService fusionService;

    @GetMapping("/chu")
    public ResponseEntity<List<CHUResident>> lancerFusion() {
        try {
            log.info("Démarrage de la fusion des résidents via GET");
            List<CHUResident> residents = fusionService.fusionnerResidents();
            log.info("Fusion terminée avec succès");
            return ResponseEntity.ok(residents);
        } catch (Exception e) {
            log.error("Erreur lors de la fusion: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Service de fusion opérationnel";
    }
}
