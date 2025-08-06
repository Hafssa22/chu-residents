package com.example.HOSIX.AGiRH.Service;

import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import com.example.HOSIX.AGiRH.Repository.ChuRepo.CHURepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidentService {

    @Autowired
    private CHURepository chuResidentRepository;

    public List<CHUResident> getAllResidents() {
        return chuResidentRepository.findAll();
    }

    public CHUResident createResident(CHUResident resident) {
        return chuResidentRepository.save(resident);
    }

    public boolean deleteResident(Long id) {
        chuResidentRepository.deleteById(id);
        return false;
    }

    public CHUResident getResidentById(Long id) {
        return chuResidentRepository.findById(id).orElse(null);
    }

    public CHUResident updateResident(Long id, CHUResident updated) {
        updated.setId(id);
        return chuResidentRepository.save(updated);
    }
}
