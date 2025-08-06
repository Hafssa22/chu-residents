package com.example.HOSIX.AGiRH.Repository.HosixRepo;

import com.example.HOSIX.AGiRH.Entity.HOSIXRes.HOSIXResident;
import com.example.HOSIX.AGiRH.Enum.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HOSIXRepository extends JpaRepository<HOSIXResident, Long> {

    // Recherche par matricule (clé de fusion)
    Optional<HOSIXResident> findByMatricule(String matricule);

    // Vérification d'existence
    boolean existsByMatricule(String matricule);

    // Recherche par email
    Optional<HOSIXResident> findByEmail(String email);

    // Filtres par statut
    List<HOSIXResident> findByStatutHosix(Statut statut);

    // Résidents par service
    List<HOSIXResident> findByService(String service);
    List<HOSIXResident> findByServiceContainingIgnoreCase(String service);

    // Résidents par poste
    List<HOSIXResident> findByPosteOccupe(String poste);

    // Recherche par date d'affectation
    List<HOSIXResident> findByDateAffectationAfter(LocalDate date);
    List<HOSIXResident> findByDateAffectationBefore(LocalDate date);
    List<HOSIXResident> findByDateAffectationBetween(LocalDate startDate, LocalDate endDate);

    // Statistiques pour KPI
    @Query(value = "SELECT COUNT(*) FROM hosix_resident WHERE statut_hosix = 'ACTIF'", nativeQuery = true)
    Long countActifs();

    @Query(value = "SELECT COUNT(*) FROM hosix_resident WHERE statut_hosix = 'INACTIF'", nativeQuery = true)
    Long countInactifs();

    @Query(value = "SELECT COUNT(*) FROM hosix_resident WHERE service IS NOT NULL", nativeQuery = true)
    Long countWithService();

    // Recherche full-text
    @Query(value = "SELECT * FROM hosix_resident WHERE " +
            "MATCH(full_name, service, poste_occupe) AGAINST(?1 IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<HOSIXResident> fullTextSearch(String searchTerm);

    // Top services les plus représentés
    @Query(value = "SELECT service, COUNT(*) as count FROM hosix_resident " +
            "WHERE service IS NOT NULL GROUP BY service ORDER BY count DESC LIMIT ?1",
            nativeQuery = true)
    List<Object[]> findTopServices(int limit);
}