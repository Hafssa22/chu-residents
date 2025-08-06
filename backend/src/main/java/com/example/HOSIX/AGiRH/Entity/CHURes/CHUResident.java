package com.example.HOSIX.AGiRH.Entity.CHURes;

import com.example.HOSIX.AGiRH.Enum.Statut;
import com.example.HOSIX.AGiRH.Enum.TypeService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "chu_resident")  // Nom de table plus standard
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CHUResident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @Column(name = "date_entree")
    private LocalDate dateDebut;

    @Column(unique = true, nullable = false)
    private String matricule;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String service;
    private String niveau;

    @Column(name = "service_affectation")
    private String serviceAffectation;


    @Column(name = "date_affectation")
    private LocalDate dateAffectation;

    @Column(name = "poste_occupe")
    private String posteOccupe;

    @Column(name = "email")
    private String email;

    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_agirh")
    private Statut statutAgirh;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_hosix")
    private Statut statutHosix;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_global")
    private Statut statutGlobal;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_service")
    private TypeService typeService;

    // Audit fields
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}