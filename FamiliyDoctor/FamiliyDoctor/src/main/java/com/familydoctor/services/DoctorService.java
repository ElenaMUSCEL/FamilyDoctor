package com.familydoctor.services;

import com.familydoctor.models.DTO.DoctorDTO;
import com.familydoctor.models.DTO.PatientDTO;

import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();

    DoctorDTO getDoctorById(Long id);

    DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO);

    void deleteDoctor(Long id);

    PatientDTO addPatient(Long doctorId, PatientDTO patientDTO);

    DoctorDTO createDoctor(DoctorDTO doctorDTO);

    List<PatientDTO> getPatientsByBirthYear(Long doctorId, int birthYear);
}
