package com.example.HOSIX.AGiRH.Entity.AGiRHNew;

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
@Table(name = "agirh_new_resident")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AGiRHNewResident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AGiRHid;

    @Column(unique = true, nullable = false)
    private String matricule;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String niveau;

    @Column(name = "service_affectation")
    private String serviceAffectation;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(unique = true)
    private String email;

    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_agirh")
    private Statut statutAgirh;

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