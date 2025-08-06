package com.example.HOSIX.AGiRH.Controller;

import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import com.example.HOSIX.AGiRH.Repository.ChuRepo.CHURepository;
import com.example.HOSIX.AGiRH.Service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/residents")
@CrossOrigin(origins = "http://localhost:3000")
public class ResidentController {

    @Autowired
    private ResidentService service;

    @Autowired
    private CHURepository chuRepository;


    // Récupérer tous les résidents
    @GetMapping("/residents")
    public List<CHUResident> getAllResidents() {
        return service.getAllResidents();
    }

    // Récupérer un résident par id
    @GetMapping("/read/{id}")
    public ResponseEntity<CHUResident> getResidentById(@PathVariable Long id) {
        CHUResident resident = service.getResidentById(id);
        if (resident == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resident);
    }

    // Créer un résident
    @PostMapping("/create")
    public ResponseEntity<CHUResident> createResident(@RequestBody CHUResident resident) {
        CHUResident created = service.createResident(resident);
        return ResponseEntity.ok(created);
    }

    // Mettre à jour un résident
    @PutMapping("/update/{id}")
    public ResponseEntity<CHUResident> updateResident(@PathVariable Long id, @RequestBody CHUResident resident) {
        CHUResident updated = service.updateResident(id, resident);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // Supprimer un résident
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteResident(id);
    }

    @GetMapping("/search")
    public List<CHUResident> searchResidents(@RequestParam String query) {
        return chuRepository.searchMultiFields(query.toLowerCase());
    }



}
