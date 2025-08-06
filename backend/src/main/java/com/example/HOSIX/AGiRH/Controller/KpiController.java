package com.example.HOSIX.AGiRH.Controller;

import com.example.HOSIX.AGiRH.Repository.ChuRepo.CHURepository;
import com.example.HOSIX.AGiRH.Service.KpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kpi")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class KpiController {

    @Autowired
    private CHURepository chuRepository;

    @Autowired
    private KpiService kpiService;  // Injection du service KpiService

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getSimpleCount() {
        try {
            log.info("Tentative de récupération du nombre de résidents CHU");

            Long count = chuRepository.count();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("totalResidents", count);
            response.put("timestamp", LocalDate.now());

            log.info("Nombre de résidents CHU: {}", count);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Erreur lors du comptage des résidents CHU", e);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        try {
            log.info("Génération du dashboard KPI simplifié");

            Map<String, Object> kpis = new HashMap<>();

            // 1. Total résidents
            Long totalCount = chuRepository.count();
            log.info("Total résidents: {}", totalCount);

            Map<String, Object> total = new HashMap<>();
            total.put("count", totalCount);
            total.put("label", "Total Résidents");
            total.put("icon", "users");
            kpis.put("totalResidents", total);

            try {
                Long actifs = chuRepository.countActifs();
                Long inactifs = chuRepository.countInactifs();

                List<Map<String, Object>> statutData = new ArrayList<>();

                Map<String, Object> actifData = new HashMap<>();
                actifData.put("status", "ACTIF");
                actifData.put("count", actifs);
                actifData.put("percentage", totalCount > 0 ? Math.round((actifs * 100.0) / totalCount) : 0);
                actifData.put("color", "#10B981");
                statutData.add(actifData);

                Map<String, Object> inactifData = new HashMap<>();
                inactifData.put("status", "INACTIF");
                inactifData.put("count", inactifs);
                inactifData.put("percentage", totalCount > 0 ? Math.round((inactifs * 100.0) / totalCount) : 0);
                inactifData.put("color", "#EF4444");
                statutData.add(inactifData);

                Map<String, Object> statutDistribution = new HashMap<>();
                statutDistribution.put("data", statutData);
                statutDistribution.put("title", "Répartition par Statut");
                statutDistribution.put("type", "pieChart");
                kpis.put("statutDistribution", statutDistribution);

                double inactiveRate = totalCount > 0 ? (inactifs * 100.0) / totalCount : 0;
                Map<String, Object> rate = new HashMap<>();
                rate.put("rate", Math.round(inactiveRate * 100.0) / 100.0);
                rate.put("inactiveCount", inactifs);
                rate.put("totalCount", totalCount);
                rate.put("label", "Taux d'Inactivité Global");
                rate.put("unit", "%");
                rate.put("color", inactiveRate > 20 ? "#EF4444" : inactiveRate > 10 ? "#F59E0B" : "#10B981");
                kpis.put("inactiveRate", rate);

            } catch (Exception e) {
                log.warn("Erreur lors du calcul des statuts détaillés", e);
                Map<String, Object> defaultStats = new HashMap<>();
                defaultStats.put("error", "Impossible de calculer les statuts détaillés");
                kpis.put("statutDistribution", defaultStats);
            }

            kpis.put("status", "success");
            kpis.put("generatedAt", LocalDate.now());

            log.info("Dashboard KPI généré avec succès");
            return ResponseEntity.ok(kpis);

        } catch (Exception e) {
            log.error("Erreur lors de la génération du dashboard", e);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("error", e.getMessage());
            response.put("errorClass", e.getClass().getSimpleName());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/alertes")
    public ResponseEntity<List<Map<String, Object>>> getAlertes() {
        List<Map<String, Object>> alertes = new ArrayList<>();

        try {
            long comptesExpirants = kpiService.getComptesExpirantDans30Jours();
            double tauxInactifs = kpiService.getTauxInactivite();

            if (comptesExpirants > 0) {
                alertes.add(Map.of(
                        "type", "warning",
                        "message", comptesExpirants + " comptes expirent dans les 30 prochains jours",
                        "priority", "haute",
                        "date", LocalDate.now().toString()
                ));
            }

            if (tauxInactifs > 70.0) {
                alertes.add(Map.of(
                        "type", "danger",
                        "message", "Taux d'inactivité critique: " + tauxInactifs + "%",
                        "priority", "critique",
                        "date", LocalDate.now().toString()
                ));
            }

            return ResponseEntity.ok(alertes);

        } catch (Exception e) {
            log.error("Erreur lors de la génération des alertes KPI", e);
            return ResponseEntity.internalServerError().body(List.of(
                    Map.of("type", "error", "message", "Erreur lors du calcul des alertes", "details", e.getMessage())
            ));
        }
    }

}
