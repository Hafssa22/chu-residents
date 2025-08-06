package com.example.HOSIX.AGiRH.Entity.HOSIXRes;

import com.example.HOSIX.AGiRH.Enum.Statut;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hosix_resident")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HOSIXResident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String matricule;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String service;

    @Column(name = "date_affectation")
    private LocalDate dateAffectation;

    @Column(name = "poste_occupe")
    private String posteOccupe;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_hosix")
    private Statut statutHosix;

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