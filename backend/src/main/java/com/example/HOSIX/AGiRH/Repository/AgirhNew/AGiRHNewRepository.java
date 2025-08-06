package com.example.HOSIX.AGiRH.Repository.AgirhNew;

import com.example.HOSIX.AGiRH.Entity.AGiRHNew.AGiRHNewResident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AGiRHNewRepository extends JpaRepository<AGiRHNewResident, Long> {
    Optional<AGiRHNewResident> findByMatricule(String matricule);
}
