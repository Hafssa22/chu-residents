package com.example.HOSIX.AGiRH.Repository.ChuRepo;

import com.example.HOSIX.AGiRH.Entity.CHURes.CHUResident;
import com.example.HOSIX.AGiRH.Enum.Statut;
import com.example.HOSIX.AGiRH.Enum.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CHURepository extends JpaRepository<CHUResident, Long> {
    @Query("SELECT r FROM CHUResident r WHERE " +
            "LOWER(r.fullName) LIKE %:query% OR " +
            "LOWER(r.matricule) LIKE %:query% OR " +
            "LOWER(r.email) LIKE %:query% OR " +
            "LOWER(r.service) LIKE %:query% OR " +
            "LOWER(r.serviceAffectation) LIKE %:query% OR " +
            "LOWER(r.statutGlobal) LIKE %:query%")
    List<CHUResident> searchMultiFields(@Param("query") String query);


    boolean existsByMatricule(String matricule);
    Optional<CHUResident> findByMatricule(String matricule);

    // Méthodes pour les KPI
    Long countByStatutGlobal(Statut statut);
    Long countByTypeService(TypeService typeService);
    List<CHUResident> findByStatutGlobalAndDateModificationBefore(Statut statut, LocalDate date);

    // Requêtes natives pour éviter les problèmes d'enum
    @Query(value = "SELECT COUNT(*) FROM chu_resident WHERE statut_global = 'ACTIF'", nativeQuery = true)
    Long countActifs();

    @Query(value = "SELECT COUNT(*) FROM chu_resident WHERE statut_global = 'INACTIF'", nativeQuery = true)
    Long countInactifs();

    @Query(value = "SELECT COUNT(*) FROM chu_resident WHERE type_service = 'MEDICAL'", nativeQuery = true)
    Long countMedical();

    @Query(value = "SELECT COUNT(*) FROM chu_resident WHERE type_service = 'CHIRURGICAL'", nativeQuery = true)
    Long countChirurgical();

    @Query(value = "SELECT COUNT(*) FROM chu_resident WHERE AGiRH_Id IS NOT NULL AND HOSIX_Id IS NOT NULL", nativeQuery = true)
    Long countFusioned();

    @Query(value = "SELECT COUNT(*) FROM chu_resident WHERE AGiRH_Id IS NOT NULL AND HOSIX_Id IS NULL", nativeQuery = true)
    Long countAgirhOnly();

    @Query(value = "SELECT COUNT(*) FROM chu_resident WHERE AGiRH_Id IS NULL AND HOSIX_Id IS NOT NULL", nativeQuery = true)
    Long countHosixOnly();

    List<CHUResident> findByStatutGlobal(Statut statut);

}
