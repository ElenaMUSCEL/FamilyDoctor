package com.familydoctor.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symptoms")
    private String symptoms;

    @Column(name = "diagnostic")
    private String diagnostic;
    @Column(name = "treatment")
    private String treatment;
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;
    @ManyToMany(mappedBy = "medicalRecords")
    private Set<Patient> patients = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;

    public Patient getPatient() {
        return patient;
    }
}
