package com.example.HOSIX.AGiRH.Repository.AgirhOld;

import com.example.HOSIX.AGiRH.Entity.AGiRHOld.AGiRHOldResident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AGiRHOldRepository extends JpaRepository<AGiRHOldResident, Long> {
    // JpaRepository fournit déjà la méthode findAll()
}

