package com.familydoctor.services;

import com.familydoctor.exceptions.DoctorNotFoundException;
import com.familydoctor.models.DTO.DoctorDTO;
import com.familydoctor.models.DTO.PatientDTO;
import com.familydoctor.models.entities.Doctor;
import com.familydoctor.models.entities.Patient;
import com.familydoctor.repositories.DoctorRepository;
import com.familydoctor.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository, ObjectMapper objectMapper) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return mapDoctorsToDTOs(doctors);
    }

    @Override
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: Doctor with id " + id, id));
        return mapDoctorToDTO(doctor);
    }

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctorToSave = mapDTOToDoctor(doctorDTO);
        Doctor savedDoctor = doctorRepository.save(doctorToSave);
        return mapDoctorToDTO(savedDoctor);
    }

    @Override
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: Doctor with id " + id, id));
        Doctor updatedDoctor = mapDTOToDoctor(doctorDTO);
        updatedDoctor.setId(existingDoctor.getId());
        Doctor savedDoctor = doctorRepository.save(updatedDoctor);
        return mapDoctorToDTO(savedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new DoctorNotFoundException("Doctor not found: Doctor with id " + id, id);
        }
        doctorRepository.deleteById(id);
    }
    @Override
    public PatientDTO addPatient(Long doctorId, PatientDTO patientDTO) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: Doctor with id " + doctorId, doctorId));

        Patient patient = new Patient();
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setEmail(patientDTO.getEmail());
        patient.setAddress(patientDTO.getAddress());

        patient = patientRepository.save(patient);

        doctor.getPatients().add(patient);
        doctorRepository.save(doctor);

        return mapPatientToDTO(patient);
    }

    @Override
    public List<PatientDTO> getPatientsByBirthYear(Long doctorId, int birthYear) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found: Doctor with id " + doctorId, doctorId));

        List<Patient> patients = doctor.getPatients();

        List<Patient> filteredPatients = patients.stream()
                .filter(patient -> getBirthYear(patient.getDateOfBirth()) == birthYear)
                .collect(Collectors.toList());

        return mapPatientsToDTOs(filteredPatients);
    }

    private int getBirthYear(LocalDate dateOfBirth) {
        return dateOfBirth.getYear();
    }

    private List<DoctorDTO> mapDoctorsToDTOs(List<Doctor> doctors) {
        return doctors.stream()
                .map(this::mapDoctorToDTO)
                .collect(Collectors.toList());
    }

    private DoctorDTO mapDoctorToDTO(Doctor doctor) {
        return objectMapper.convertValue(doctor, DoctorDTO.class);
    }

    private Doctor mapDTOToDoctor(DoctorDTO doctorDTO) {
        return objectMapper.convertValue(doctorDTO, Doctor.class);
    }

    private List<PatientDTO> mapPatientsToDTOs(@org.jetbrains.annotations.NotNull List<Patient> patients) {
        return patients.stream()
                .map(this::mapPatientToDTO)
                .collect(Collectors.toList());
    }

    private PatientDTO mapPatientToDTO(Patient patient) {
        return objectMapper.convertValue(patient, PatientDTO.class);
    }

    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
