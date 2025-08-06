package com.example.HOSIX.AGiRH.Service;

import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import com.example.HOSIX.AGiRH.Enum.Statut;
import com.example.HOSIX.AGiRH.Enum.TypeService;
import com.example.HOSIX.AGiRH.Event.AccountDesactivatedEvent;
import com.example.HOSIX.AGiRH.Repository.ChuRepo.CHURepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class DesactivationService {

    @Autowired
    private CHURepository residentRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Scheduled(cron = "0 * * * * *")
    public void desactiverResidents() {
        LocalDate today = LocalDate.now();
        List<CHUResident> actifs = residentRepository.findByStatutGlobal(Statut.ACTIF);

        for (CHUResident resident : actifs) {
            LocalDate dateDebut = resident.getDateDebut();
            if (dateDebut == null) continue;

            Period period = Period.between(dateDebut, today);
            int years = period.getYears();

            TypeService typeService = resident.getTypeService();
            if (typeService == null) continue;

            int dureeMax = switch (typeService) {
                case CHIRURGICAL -> 5;
                case MEDICAL -> 4;
            };

            if (years >= dureeMax) {
                resident.setStatutGlobal(Statut.INACTIF);
                residentRepository.save(resident);

                // 🔔 Envoie l'événement à Kafka
                AccountDesactivatedEvent event = new AccountDesactivatedEvent();
                event.setMatricule(resident.getMatricule());
                event.setReason("Fin de la période de résidence (" + dureeMax + " ans)");
                event.setDate(LocalDate.parse(today.toString()));
                event.setPhoneNumber(resident.getTelephone());
                kafkaProducerService.sendAccountDeactivationEvent(event);
            }
        }
    }
}
