package com.familydoctor.unitTest;

import com.familydoctor.exceptions.PatientNotFoundException;
import com.familydoctor.models.DTO.PatientDTO;
import com.familydoctor.models.entities.Patient;
import com.familydoctor.repositories.PatientRepository;
import com.familydoctor.services.PatientServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ObjectMapper objectMapper;

    private PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientService = new PatientServiceImpl(patientRepository, objectMapper);
    }

    @Test
    void createPatient_ValidPatientDTO_ReturnsConvertedPatientDTO() {
        // Arrange
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstName("Ion");
        patientDTO.setLastName("Popescu");
        patientDTO.setAddress("Timisoara");
        patientDTO.setEmail("ion@gmail.com");
        patientDTO.setPhoneNumber("0787676676");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender("male");

        Patient patientToSave = new Patient();
        Patient patientSaved = new Patient();
        when(objectMapper.convertValue(eq(patientDTO), eq(Patient.class))).thenReturn(patientToSave);
        when(patientRepository.save(eq(patientToSave))).thenReturn(patientSaved);
        when(objectMapper.convertValue(eq(patientSaved), eq(PatientDTO.class))).thenReturn(patientDTO);

        PatientDTO result = patientService.createPatient(patientDTO);

        assertNotNull(result);
        assertEquals(patientDTO, result);
        verify(objectMapper).convertValue(eq(patientDTO), eq(Patient.class));
        verify(patientRepository).save(eq(patientToSave));
        verify(objectMapper).convertValue(eq(patientSaved), eq(PatientDTO.class));
    }
    @Test
    void getAllPatients_ExistingPatients_ReturnsListOfPatientsDTO() {
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        List<Patient> patients = Arrays.asList(patient1, patient2);
        PatientDTO patientDTO1 = new PatientDTO();
        PatientDTO patientDTO2 = new PatientDTO();
        when(patientRepository.findAll()).thenReturn(patients);
        when(objectMapper.convertValue(eq(patient1), eq(PatientDTO.class))).thenReturn(patientDTO1);
        when(objectMapper.convertValue(eq(patient2), eq(PatientDTO.class))).thenReturn(patientDTO2);

        List<PatientDTO> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(patientDTO1));
        assertTrue(result.contains(patientDTO2));
    }

    @Test
    void updatePatientById_ExistingPatient_ReturnsUpdatedPatient() {
        long patientId = 1;
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstName("Ion");
        patientDTO.setLastName("Popescu");
        patientDTO.setAddress("Timisoara");
        patientDTO.setEmail("ion@gmail.com");
        patientDTO.setPhoneNumber("0787676676");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender("male");

        Patient patientFound = new Patient();
        Patient updatedPatient = new Patient();
        when(patientRepository.findById(eq(patientId))).thenReturn(Optional.of(patientFound));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.updatePatientById(patientId, patientDTO);

        assertNotNull(result);
        assertEquals(patientDTO.getFirstName(), result.getFirstName());
        assertEquals(patientDTO.getLastName(), result.getLastName());
        assertEquals(patientDTO.getAddress(), result.getAddress());
        assertEquals(patientDTO.getEmail(), result.getEmail());
        assertEquals(patientDTO.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(patientDTO.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(patientDTO.getGender(), result.getGender());
        verify(patientRepository).findById(eq(patientId));
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void updatePatientById_NonExistingPatient_ThrowsPatientNotFoundException() {
        long patientId = 1;
        PatientDTO patientDTO = new PatientDTO();
        when(patientRepository.findById(eq(patientId))).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> patientService.updatePatientById(patientId, patientDTO));

        verify(patientRepository).findById(eq(patientId));
    }

    @Test
    void deletePatientById_ValidId_PatientDeleted() {
        long patientId = 1;
        Patient patient = new Patient();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        patientService.deletePatientById(patientId);

        verify(patientRepository).findById(patientId);
        verify(patientRepository).delete(patient);
    }

    @Test
    void deletePatientById_InvalidId_ThrowsException() {
        long patientId = 1;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> patientService.deletePatientById(patientId));

        verify(patientRepository).findById(patientId);
        verify(patientRepository, never()).deleteById(patientId);
    }
}
