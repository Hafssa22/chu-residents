package com.example.HOSIX.AGiRH.Service;

import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import com.example.HOSIX.AGiRH.Enum.Statut;
import com.example.HOSIX.AGiRH.Enum.TypeService;
import com.example.HOSIX.AGiRH.Repository.ChuRepo.CHURepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KpiService {

    @Autowired
    private CHURepository chuRepository;

    public List<CHUResident> getResidents() {
        return chuRepository.findAll();
    }

    public Map<String, Object> getAllKPIs() {
        log.info("Génération des KPI pour le tableau de bord");

        Map<String, Object> kpis = new HashMap<>();

        try {
            List<CHUResident> allResidents = chuRepository.findAll();

            kpis.put("totalResidents", getTotalResidents(allResidents));
            kpis.put("statutDistribution", getStatutDistribution(allResidents));
            kpis.put("serviceDistribution", getServiceDistribution(allResidents));
            kpis.put("inactiveRate", getInactiveRate(allResidents));
            kpis.put("alerts", getKpiAlerts(allResidents));

        } catch (Exception e) {
            log.error("Erreur lors de la génération des KPI", e);
            kpis.put("status", "error");
            kpis.put("error", e.getMessage());
        }

        return kpis;
    }

    private Map<String, Object> getTotalResidents(List<CHUResident> residents) {
        return Map.of(
                "count", residents.size(),
                "label", "Total Résidents",
                "icon", "users"
        );
    }

    private Map<String, Object> getStatutDistribution(List<CHUResident> residents) {
        Map<Statut, Long> counts = residents.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getStatutGlobal() != null ? r.getStatutGlobal() : Statut.INACTIF,
                        Collectors.counting()
                ));

        long actifs = counts.getOrDefault(Statut.ACTIF, 0L);
        long inactifs = counts.getOrDefault(Statut.INACTIF, 0L);
        long total = residents.size();

        List<Map<String, Object>> data = List.of(
                Map.of(
                        "status", "ACTIF",
                        "count", actifs,
                        "percentage", total > 0 ? Math.round((actifs * 100.0) / total) : 0,
                        "color", "#10B981"
                ),
                Map.of(
                        "status", "INACTIF",
                        "count", inactifs,
                        "percentage", total > 0 ? Math.round((inactifs * 100.0) / total) : 0,
                        "color", "#EF4444"
                )
        );

        return Map.of(
                "data", data,
                "title", "Répartition par Statut",
                "type", "pieChart"
        );
    }

    private Map<String, Object> getServiceDistribution(List<CHUResident> residents) {
        Map<TypeService, Long> counts = residents.stream()
                .filter(r -> r.getTypeService() != null)
                .collect(Collectors.groupingBy(CHUResident::getTypeService, Collectors.counting()));

        long medical = counts.getOrDefault(TypeService.MEDICAL, 0L);
        long chirurgical = counts.getOrDefault(TypeService.CHIRURGICAL, 0L);
        long total = medical + chirurgical;

        List<Map<String, Object>> data = List.of(
                Map.of(
                        "service", "MÉDICAL",
                        "count", medical,
                        "percentage", total > 0 ? Math.round((medical * 100.0) / total) : 0,
                        "color", "#3B82F6"
                ),
                Map.of(
                        "service", "CHIRURGICAL",
                        "count", chirurgical,
                        "percentage", total > 0 ? Math.round((chirurgical * 100.0) / total) : 0,
                        "color", "#8B5CF6"
                )
        );

        return Map.of(
                "data", data,
                "title", "Répartition par Type de Service",
                "type", "barChart",
                "totalWithService", total,
                "totalWithoutService", residents.size() - total
        );
    }

    private Map<String, Object> getInactiveRate(List<CHUResident> residents) {
        long total = residents.size();
        long inactifs = residents.stream()
                .filter(r -> r.getStatutGlobal() == Statut.INACTIF)
                .count();

        double rate = total > 0 ? (inactifs * 100.0) / total : 0;

        return Map.of(
                "rate", Math.round(rate * 100.0) / 100.0,
                "inactiveCount", inactifs,
                "totalCount", total,
                "label", "Taux d'Inactivité Global",
                "unit", "%",
                "color", rate > 70 ? "#DC2626" : rate > 40 ? "#F59E0B" : "#10B981"
        );
    }

    public List<Map<String, Object>> getKpiAlerts(List<CHUResident> residents) {
        List<Map<String, Object>> alerts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(30);

        List<CHUResident> expirants = residents.stream()
                .filter(r -> r.getStatutGlobal() == Statut.ACTIF)
                .filter(r -> r.getDateDebut() != null && r.getTypeService() != null)
                .filter(r -> {
                    LocalDate debut = r.getDateDebut();
                    int duree = r.getTypeService() == TypeService.CHIRURGICAL ? 5 : 4;
                    LocalDate finTheorique = debut.plusYears(duree);
                    return !finTheorique.isBefore(today) && !finTheorique.isAfter(threshold);
                })
                .toList();

        if (!expirants.isEmpty()) {
            alerts.add(Map.of(
                    "type", "warning",
                    "message", expirants.size() + " comptes expirent dans les 30 prochains jours",
                    "priority", "haute",
                    "date", "Aujourd'hui"
            ));
        }

        double taux = (double) getInactiveRate(residents).get("rate");

        if (taux > 70) {
            alerts.add(Map.of(
                    "type", "danger",
                    "message", "Taux d'inactivité critique : " + taux + "%",
                    "priority", "critique",
                    "date", "Aujourd'hui"
            ));
        } else if (taux > 50) {
            alerts.add(Map.of(
                    "type", "warning",
                    "message", "Taux d'inactivité élevé : " + taux + "%",
                    "priority", "haute",
                    "date", "Aujourd'hui"
            ));
        }

        alerts.add(Map.of(
                "type", "info",
                "message", "Sauvegarde recommandée (dernière : il y a 7 jours)",
                "priority", "moyenne",
                "date", "Il y a 2h"
        ));

        return alerts;
    }

    public long getComptesExpirantDans30Jours() {
        List<CHUResident> residents = chuRepository.findAll();
        LocalDate today = LocalDate.now();

        return residents.stream()
                .filter(r -> r.getDateDebut() != null && r.getTypeService() != null)
                .filter(r -> {
                    LocalDate debut = r.getDateDebut();
                    int duree = r.getTypeService() == TypeService.CHIRURGICAL ? 5 : 4;
                    LocalDate fin = debut.plusYears(duree);
                    return fin.isAfter(today) && fin.isBefore(today.plusDays(30));
                })
                .count();
    }

    public double getTauxInactivite() {
        List<CHUResident> residents = chuRepository.findAll();
        long total = residents.size();
        if (total == 0) return 0.0;

        long inactifs = residents.stream()
                .filter(r -> r.getStatutGlobal() == Statut.INACTIF)
                .count();

        return Math.round((inactifs * 10000.0 / total)) / 100.0;
    }
}
