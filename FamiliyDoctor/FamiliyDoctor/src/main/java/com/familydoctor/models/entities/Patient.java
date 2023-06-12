package com.familydoctor.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "gender")
    private String gender;
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;
    @ManyToMany
    @JoinTable(
            name = "patient_medical_record",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_record_id")
    )
    private Set<MedicalRecord> medicalRecords = new HashSet<>();

    public void setDoctor(Doctor doctor) {
        if (this.doctor != null) {
            this.doctor.getPatients().remove(this);
        }
        this.doctor = doctor;
        if (doctor != null) {
            doctor.getPatients().add(this);
        }
    }
}
