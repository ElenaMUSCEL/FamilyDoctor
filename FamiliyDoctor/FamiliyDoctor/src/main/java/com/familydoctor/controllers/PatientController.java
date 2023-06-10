package com.familydoctor.controllers;

import com.familydoctor.models.DTO.PatientDTO;
import com.familydoctor.models.entities.Patient;
import com.familydoctor.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody @Valid PatientDTO patientDTO) {
        return ResponseEntity.ok(patientService.createPatient(patientDTO));
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<Patient> updatePatientById(@PathVariable long patientId, @RequestBody PatientDTO patientDTO) {
        Patient updatedPatient = patientService.updatePatientById(patientId, patientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatientById(@PathVariable long patientId) throws ChangeSetPersister.NotFoundException {
        patientService.deletePatientById(patientId);
        return ResponseEntity.noContent().build();
    }
}
