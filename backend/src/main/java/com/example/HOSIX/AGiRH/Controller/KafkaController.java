package com.example.HOSIX.AGiRH.Controller;

import com.example.HOSIX.AGiRH.Event.AccountDesactivatedEvent;
import com.example.HOSIX.AGiRH.Service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
@CrossOrigin(origins = "http://localhost:3000") // Si tu veux appeler depuis React
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEvent(@RequestBody AccountDesactivatedEvent event) {
        kafkaProducerService.sendAccountDeactivationEvent(event);
        return ResponseEntity.ok("✅ Event envoyé à Kafka (topic account_deactivated_topic) !");
    }
}
