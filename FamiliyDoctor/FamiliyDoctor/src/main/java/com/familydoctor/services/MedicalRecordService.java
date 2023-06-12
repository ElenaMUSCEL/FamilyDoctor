package com.familydoctor.services;

import com.familydoctor.models.DTO.MedicalRecordDTO;

import java.util.List;

public interface MedicalRecordService {

        MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecordDTO);

        MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO medicalRecordDTO);

        MedicalRecordDTO getMedicalRecordById(Long id);

        List<MedicalRecordDTO> getMedicalRecordsByPatientId(Long patientId);

        List<MedicalRecordDTO> getMedicalRecordsByDoctorAndPatient(Long doctorId, Long patientId);
}