package com.familydoctor.services;

import com.familydoctor.models.DTO.PatientDTO;
import com.familydoctor.models.entities.Patient;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface PatientService {

    PatientDTO createPatient(PatientDTO  patientDTO);
    List<PatientDTO> getAllPatients();
    Patient updatePatientById(long patientId, PatientDTO patientDTO);
    void deletePatientById(long patientId) throws ChangeSetPersister.NotFoundException;
}
