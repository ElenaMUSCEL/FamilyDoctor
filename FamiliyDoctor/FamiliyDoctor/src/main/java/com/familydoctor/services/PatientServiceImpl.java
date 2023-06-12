package com.familydoctor.services;

import com.familydoctor.exceptions.PatientNotFoundException;
import com.familydoctor.models.DTO.PatientDTO;
import com.familydoctor.models.entities.Patient;
import com.familydoctor.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, ObjectMapper objectMapper) {
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patientToSave = objectMapper.convertValue(patientDTO, Patient.class);
        Patient patientSaved = patientRepository.save(patientToSave);
        return objectMapper.convertValue(patientSaved, PatientDTO.class);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> objectMapper.convertValue(patient, PatientDTO.class))
                .toList();
    }

    public Patient updatePatientById(long patientId, PatientDTO patientDTO) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isPresent()) {
            Patient patientFound = optionalPatient.get();
            patientFound.setFirstName(patientDTO.getFirstName());
            patientFound.setLastName(patientDTO.getLastName());
            patientFound.setAddress(patientDTO.getAddress());
            patientFound.setEmail(patientDTO.getEmail());
            patientFound.setPhoneNumber(patientDTO.getPhoneNumber());
            patientFound.setDateOfBirth(patientDTO.getDateOfBirth());
            patientFound.setGender(patientDTO.getGender());
            Patient updatedPatient = patientRepository.save(patientFound);
            log.info("Patient updated: " + patientDTO);
            return updatedPatient;
        } else {
            throw new PatientNotFoundException("Patient not found: Patient with id " + patientId, patientId);
        }
    }
    @Override
    public void deletePatientById(long patientId) {
        patientRepository.deleteById(patientId);
    }
}
