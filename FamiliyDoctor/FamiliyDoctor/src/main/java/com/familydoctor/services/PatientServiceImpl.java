package com.familydoctor.services;

import com.familydoctor.models.DTO.PatientDTO;
import com.familydoctor.models.entities.Patient;
import com.familydoctor.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;

    public PatientServiceImpl(PatientRepository patientRepository, ObjectMapper objectMapper) {
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
    }
    @Override
    public PatientDTO createPatient(PatientDTO patientDTO){
        Patient patientToSave = objectMapper.convertValue(patientDTO, Patient.class);
        //patientToSave.setDateOfBirth(Date.valueOf(patientDTO.getDateOfBirth()).toLocalDate());
        Patient patientSaved = patientRepository.save(patientToSave);
        return objectMapper.convertValue(patientSaved, PatientDTO.class);
    }
}
