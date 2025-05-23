package com.medicaldata.medicSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)

    private Patient patient;

    private String medicine;

    private String dosage;

    private String frequency;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate prescriptionDate;

    private LocalDate updatedDate;

    private String notes;
}
