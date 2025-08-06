package com.example.HOSIX.AGiRH.Service;

import com.example.HOSIX.AGiRH.Event.AccountDesactivatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC_NAME = "account_deactivated_topic";

    @Autowired
    private KafkaTemplate<String, AccountDesactivatedEvent> kafkaTemplate;

    // Envoie simple sans clé
    public void sendEvent(AccountDesactivatedEvent event) {
        kafkaTemplate.send(TOPIC_NAME, event);
    }

    // Envoie avec clé (le matricule est utilisé comme clé Kafka)
    public void sendAccountDeactivationEvent(AccountDesactivatedEvent event) {
        kafkaTemplate.send(TOPIC_NAME, event.getMatricule(), event);
    }
}
