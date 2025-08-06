package com.example.HOSIX.AGiRH.Service;

import com.example.HOSIX.AGiRH.Entity.AGiRHNew.AGiRHNewResident;
import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import com.example.HOSIX.AGiRH.Entity.HOSIXRes.HOSIXResident;
import com.example.HOSIX.AGiRH.Enum.Statut;
import com.example.HOSIX.AGiRH.Repository.AgirhNew.AGiRHNewRepository;
import com.example.HOSIX.AGiRH.Repository.ChuRepo.CHURepository;
import com.example.HOSIX.AGiRH.Repository.HosixRepo.HOSIXRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FusionService {

    @Autowired
    private AGiRHNewRepository agirhRepo;

    @Autowired
    private HOSIXRepository hosixRepo;

    @Autowired
    private CHURepository chuRepo;

    public List<CHUResident> fusionnerResidents() {
        log.info("=== DÉBUT DE LA FUSION ===");

        try {
            List<AGiRHNewResident> agirResidents = agirhRepo.findAll();
            List<HOSIXResident> hosixResidents = hosixRepo.findAll();
            List<CHUResident> chuExistants = chuRepo.findAll();

            log.info("Données sources:");
            log.info("- AGiRH résidents: {}", agirResidents.size());
            log.info("- HOSIX résidents: {}", hosixResidents.size());
            log.info("- CHU résidents existants: {}", chuExistants.size());

            if (agirResidents.isEmpty()) {
                log.warn("Aucun résident AGiRH trouvé !");
                return chuRepo.findAll(); // retourne les existants
            }

            int fusionnes = 0;
            int ignores = 0;
            int erreurs = 0;

            for (AGiRHNewResident agir : agirResidents) {
                try {
                    String matricule = agir.getMatricule();
                    log.debug("Traitement du matricule: {}", matricule);

                    if (chuRepo.existsByMatricule(matricule)) {
                        log.debug("Matricule {} déjà présent, ignoré", matricule);
                        ignores++;
                        continue;
                    }

                    if (agir.getAGiRHid() == null) {
                        log.warn("AGiRH_Id est null pour le matricule: {} → résident ignoré", matricule);
                        ignores++;
                        continue;
                    }

                    Optional<HOSIXResident> hosixOpt = hosixRepo.findByMatricule(matricule);

                    CHUResident chu = new CHUResident();
                    chu.setMatricule(matricule);
                    chu.setFullName(agir.getFullName());
                    chu.setNiveau(agir.getNiveau());
                    chu.setServiceAffectation(agir.getServiceAffectation());
                    chu.setDateDebut(agir.getDateDebut());
                    chu.setEmail(agir.getEmail());
                    chu.setTelephone(agir.getTelephone());
                    chu.setStatutAgirh(agir.getStatutAgirh());
                    chu.setTypeService(agir.getTypeService());
                    chu.setDateCreation(LocalDate.now());
                    chu.setDateModification(LocalDate.now());

                    if (hosixOpt.isPresent()) {
                        HOSIXResident hosix = hosixOpt.get();
                        chu.setService(hosix.getService());
                        chu.setDateAffectation(hosix.getDateAffectation());
                        chu.setPosteOccupe(hosix.getPosteOccupe());
                        chu.setStatutHosix(hosix.getStatutHosix());

                        if (chu.getEmail() == null || chu.getEmail().trim().isEmpty()) {
                            chu.setEmail(hosix.getEmail());
                        }

                        chu.setStatutGlobal(determinerStatutGlobal(agir.getStatutAgirh(), hosix.getStatutHosix()));
                        log.debug("Fusion complète AGiRH+HOSIX pour matricule: {}", matricule);
                    } else {
                        chu.setStatutHosix(null);
                        chu.setStatutGlobal(agir.getStatutAgirh());
                        log.debug("Création AGiRH seul pour matricule: {}", matricule);
                    }

                    CHUResident saved = chuRepo.save(chu);
                    log.debug("Résident sauvegardé avec ID: {}", saved.getId());
                    fusionnes++;

                } catch (Exception e) {
                    log.error("Erreur pour matricule {}: {}", agir.getMatricule(), e.getMessage(), e);
                    erreurs++;
                }
            }

            log.info("=== RÉSULTATS FUSION ===");
            log.info("- Fusionnés: {}", fusionnes);
            log.info("- Ignorés: {}", ignores);
            log.info("- Erreurs: {}", erreurs);
            log.info("- Total CHU après fusion: {}", chuRepo.count());

        } catch (Exception e) {
            log.error("Erreur critique dans la fusion", e);
            throw e;
        }

        // ✅ Retourne tous les résidents CHU après fusion
        return chuRepo.findAll();
    }

    private Statut determinerStatutGlobal(Statut statutAgirh, Statut statutHosix) {
        // Ajoute ta logique ici
        if (statutAgirh == Statut.INACTIF || statutHosix == Statut.INACTIF) {
            return Statut.INACTIF;
        }
        return Statut.ACTIF;
    }
}
