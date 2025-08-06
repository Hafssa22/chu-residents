package com.example.HOSIX.AGiRH.Service;

import com.example.HOSIX.AGiRH.Entity.AGiRHNew.AGiRHNewResident;
import com.example.HOSIX.AGiRH.Entity.AGiRHOld.AGiRHOldResident;
import com.example.HOSIX.AGiRH.Enum.Statut;
import com.example.HOSIX.AGiRH.Repository.AgirhNew.AGiRHNewRepository;
import com.example.HOSIX.AGiRH.Repository.AgirhOld.AGiRHOldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MigrationService {

    @Autowired
    private AGiRHNewRepository agiRHNewRepository;

    @Autowired
    private AGiRHOldRepository agiRHOldRepository;

    @Transactional("newTransactionManager")
    public void migrateResidents() {
        // Lire depuis l'ancienne base avec le transaction manager approprié
        List<AGiRHOldResident> oldResidents = readOldResidents();

        List<AGiRHNewResident> newResidents = oldResidents.stream().map(oldRes -> {
            AGiRHNewResident newRes = new AGiRHNewResident();
            // Attention: ne pas définir l'ID manuellement car c'est auto-généré
            // newRes.setAGiRHid(oldRes.getId()); // Supprimé cette ligne
            newRes.setFullName(oldRes.getFullName());
            newRes.setMatricule(oldRes.getMatricule());
            newRes.setDateDebut(oldRes.getDateDebut());
            newRes.setEmail(oldRes.getEmail());
            newRes.setNiveau(oldRes.getNiveau());
            newRes.setServiceAffectation(oldRes.getServiceAffectation());
            newRes.setTelephone(oldRes.getTelephone());
            newRes.setTypeService(oldRes.getTypeService());
            // Les timestamps seront définis automatiquement par @PrePersist

            // Logique statut
            if (oldRes.getDateFin() == null) {
                newRes.setStatutAgirh(Statut.ACTIF);
            } else {
                newRes.setStatutAgirh(Statut.INACTIF);
            }

            return newRes;
        }).collect(Collectors.toList());


        agiRHNewRepository.saveAll(newResidents);
    }

    @Transactional("oldTransactionManager")
    protected List<AGiRHOldResident> readOldResidents() {
        return agiRHOldRepository.findAll();
    }
}