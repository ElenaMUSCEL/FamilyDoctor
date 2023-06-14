package com.familydoctor.services;

import com.familydoctor.exceptions.DoctorNotFoundException;
import com.familydoctor.exceptions.MedicalRecordNotFoundException;
import com.familydoctor.exceptions.PatientNotFoundException;
import com.familydoctor.models.DTO.MedicalRecordDTO;
import com.familydoctor.models.DTO.PatientDTO;
import com.familydoctor.models.entities.Doctor;
import com.familydoctor.models.entities.MedicalRecord;
import com.familydoctor.models.entities.Patient;
import com.familydoctor.repositories.DoctorRepository;
import com.familydoctor.repositories.MedicalRecordRepository;
import com.familydoctor.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository,
                                    PatientRepository patientRepository,
                                    DoctorRepository doctorRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord medicalRecord = convertToEntity(medicalRecordDTO);
        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);
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

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        List<MedicalRecord> filteredRecords = medicalRecords.stream()
                .filter(record -> record.getPatient().equals(patient))
                .collect(Collectors.toList());

        return convertToDTOList(filteredRecords);
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByDoctorAndPatient(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: ID " + doctorId, doctorId));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found: ID " + patientId, patientId));

        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        List<MedicalRecord> filteredRecords = medicalRecords.stream()
                .filter(record -> record.getDoctor().equals(doctor) && record.getPatient().equals(patient))
                .collect(Collectors.toList());

        return convertToDTOList(filteredRecords);
    }

    private MedicalRecord convertToEntity(MedicalRecordDTO medicalRecordDTO) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setSymptoms(medicalRecordDTO.getSymptoms());
        medicalRecord.setDiagnostic(medicalRecordDTO.getDiagnostic());
        medicalRecord.setTreatment(medicalRecordDTO.getTreatment());

        Patient patient = patientRepository.findById(medicalRecordDTO.getPatient().getId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found: ID " + medicalRecordDTO.getPatient().getId(), medicalRecordDTO.getPatient().getId()));
        medicalRecord.setPatient(patient);

        Doctor doctor = (Doctor) doctorRepository.findByFirstNameAndLastName(medicalRecordDTO.getDoctorFirstName(), medicalRecordDTO.getDoctorLastName())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: " + medicalRecordDTO.getDoctorFirstName() + " " + medicalRecordDTO.getDoctorLastName()));
        medicalRecord.setDoctor(doctor);

        return medicalRecord;
    }

    private MedicalRecordDTO convertToDTO(MedicalRecord medicalRecord) {
        MedicalRecordDTO medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setId(medicalRecord.getId());
        medicalRecordDTO.setSymptoms(medicalRecord.getSymptoms());
        medicalRecordDTO.setDiagnostic(medicalRecord.getDiagnostic());
        medicalRecordDTO.setTreatment(medicalRecord.getTreatment());

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(medicalRecord.getPatient().getId());
        medicalRecordDTO.setPatient(patientDTO);

        medicalRecordDTO.setDoctorFirstName(medicalRecord.getDoctor().getFirstName());
        medicalRecordDTO.setDoctorLastName(medicalRecord.getDoctor().getLastName());

        return medicalRecordDTO;
    }

    private List<MedicalRecordDTO> convertToDTOList(List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void updateMedicalRecordFields(MedicalRecord medicalRecordToUpdate, MedicalRecordDTO medicalRecordDTO) {
        medicalRecordToUpdate.setSymptoms(medicalRecordDTO.getSymptoms());
        medicalRecordToUpdate.setDiagnostic(medicalRecordDTO.getDiagnostic());
        medicalRecordToUpdate.setTreatment(medicalRecordDTO.getTreatment());

        Patient patient = patientRepository.findById(medicalRecordDTO.getPatient().getId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found: ID " + medicalRecordDTO.getPatient().getId(), medicalRecordDTO.getPatient().getId()));
        medicalRecordToUpdate.setPatient(patient);

        Doctor doctor = (Doctor) doctorRepository.findByFirstNameAndLastName(medicalRecordDTO.getDoctorFirstName(), medicalRecordDTO.getDoctorLastName())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: " + medicalRecordDTO.getDoctorFirstName() + " " + medicalRecordDTO.getDoctorLastName()));
        medicalRecordToUpdate.setDoctor(doctor);
    }
}

