package com.familydoctor.integrationTest;

import com.familydoctor.models.DTO.PatientDTO;
import com.familydoctor.models.entities.Patient;
import com.familydoctor.repositories.PatientRepository;
import com.familydoctor.services.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class PatientServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;
  //  @Autowired
    //private TestRestTemplate restTemplate;


    @Test
    void testCreatePatientShouldPass() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstName("Ion");
        patientDTO.setLastName("Popescu");
        patientDTO.setAddress("Bucuresti");
        patientDTO.setEmail("ion@gmail.com");
        patientDTO.setPhoneNumber("0765787909");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender("male");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(patientDTO.getFirstName()));
    }

    @Test
    void testCreatePatientShouldFailValidation() throws Exception {
        PatientDTO patientDTO = new PatientDTO();

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString(patientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllPatientsShouldPass() throws Exception {
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setFirstName("Ion");
        patient1.setLastName("Popescu");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFirstName("Maria");
        patient2.setLastName("Gheorghe");

        patientRepository.saveAll(List.of(patient1, patient2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value(patient1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(patient1.getLastName()))
                .andExpect(jsonPath("$[1].firstName").value(patient2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(patient2.getLastName()));
    }
    @Test
    void testUpdatePatientByIdShouldPass() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ion");
        patient.setLastName("Popescu");
        patientRepository.save(patient);

        PatientDTO updatedPatientDTO = new PatientDTO();
        updatedPatientDTO.setFirstName("Updated Ion");
        updatedPatientDTO.setLastName("Updated Popescu");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/{id}", patient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString(updatedPatientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patient.getId()))
                .andExpect(jsonPath("$.firstName").value(updatedPatientDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedPatientDTO.getLastName()));
    }

    @Test
    void testUpdatePatientByIdShouldFailPatientNotFound() throws Exception {
        long patientId = 1L;
        PatientDTO updatedPatientDTO = new PatientDTO();
        updatedPatientDTO.setFirstName("Updated Ion");
        updatedPatientDTO.setLastName("Updated Popescu");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString(updatedPatientDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePatientByIdShouldPass() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Ion");
        patient.setLastName("Popescu");
        patientRepository.save(patient);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients/{id}", patient.getId()))
                .andExpect(status().isOk());

        Optional<Patient> deletedPatient = patientRepository.findById(patient.getId());
    }

    @Test
    void testDeletePatientByIdShouldFailPatientNotFound() throws Exception {
        long patientId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients/{id}", patientId))
                .andExpect(status().isNotFound());
    }

    private String objectToString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}