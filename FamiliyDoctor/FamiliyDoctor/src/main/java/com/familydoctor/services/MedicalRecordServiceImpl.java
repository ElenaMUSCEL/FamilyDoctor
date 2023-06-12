package com.familydoctor.services;

import com.familydoctor.exceptions.DoctorNotFoundException;
import com.familydoctor.exceptions.MedicalRecordNotFoundException;
import com.familydoctor.exceptions.PatientNotFoundException;
import com.familydoctor.models.DTO.MedicalRecordDTO;
import com.familydoctor.models.entities.Doctor;
import com.familydoctor.models.entities.MedicalRecord;
import com.familydoctor.models.entities.Patient;
import com.familydoctor.repositories.DoctorRepository;
import com.familydoctor.repositories.MedicalRecordRepository;
import com.familydoctor.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository,
                                    PatientRepository patientRepository,
                                    DoctorRepository doctorRepository,
                                    ObjectMapper objectMapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord medicalRecordToSave = convertToEntity(medicalRecordDTO);
        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecordToSave);
        return convertToDTO(savedMedicalRecord);
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO medicalRecordDTO) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(id);
        if (optionalMedicalRecord.isPresent()) {
            MedicalRecord medicalRecordToUpdate = optionalMedicalRecord.get();
            updateMedicalRecordFields(medicalRecordToUpdate, medicalRecordDTO);
            MedicalRecord updatedMedicalRecord = medicalRecordRepository.save(medicalRecordToUpdate);
            return convertToDTO(updatedMedicalRecord);
        } else {
            throw new MedicalRecordNotFoundException("Medical record not found: ID " + id, id);
        }
    }

    @Override
    public MedicalRecordDTO getMedicalRecordById(Long id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found: ID " + id, id));
        return convertToDTO(medicalRecord);
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found: ID " + patientId, patientId));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatient(patient);
        return convertToDTOList(medicalRecords);
    }

    private MedicalRecord convertToEntity(MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord medicalRecord = objectMapper.convertValue(medicalRecordDTO, MedicalRecord.class);
        setPatientAndDoctor(medicalRecord, medicalRecordDTO.getPatientId(), medicalRecordDTO.getDoctorId());
        return medicalRecord;
    }

    private MedicalRecordDTO convertToDTO(MedicalRecord medicalRecord) {
        MedicalRecordDTO medicalRecordDTO = objectMapper.convertValue(medicalRecord, MedicalRecordDTO.class);
        setPatientAndDoctorIds(medicalRecordDTO,
                medicalRecord.getPatient(),
                medicalRecord.getDoctor());
        return medicalRecordDTO;
    }

    private List<MedicalRecordDTO> convertToDTOList(List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void updateMedicalRecordFields(MedicalRecord medicalRecord, MedicalRecordDTO medicalRecordDTO) {
        medicalRecord.setSymptoms(medicalRecordDTO.getSymptoms());
        medicalRecord.setDiagnostic(medicalRecordDTO.getDiagnostic());
        medicalRecord.setTreatment(medicalRecordDTO.getTreatment());
        setPatientAndDoctor(medicalRecord, medicalRecordDTO.getPatientId(), medicalRecordDTO.getDoctorId());
    }

    private void setPatientAndDoctor(MedicalRecord medicalRecord, Long patientId, Long doctorId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found: ID " + patientId, patientId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: ID " + doctorId, doctorId));
        medicalRecord.setPatient(patient);
        medicalRecord.setDoctor(doctor);
    }

    private void setPatientAndDoctorIds(MedicalRecordDTO medicalRecordDTO, Patient patient, Doctor doctor) {
        medicalRecordDTO.setPatientId(patient.getId());
        medicalRecordDTO.setDoctorId(doctor.getId());
    }
}
