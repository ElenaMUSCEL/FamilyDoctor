package com.familydoctor.controllers;

import com.familydoctor.models.DTO.MedicalRecordDTO;
import com.familydoctor.services.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        MedicalRecordDTO createdMedicalRecord = medicalRecordService.createMedicalRecord(medicalRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedicalRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
            @PathVariable Long id, @RequestBody MedicalRecordDTO medicalRecordDTO) {
        MedicalRecordDTO updatedMedicalRecord = medicalRecordService.updateMedicalRecord(id, medicalRecordDTO);
        return ResponseEntity.ok(updatedMedicalRecord);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Long id) {
        MedicalRecordDTO medicalRecordDTO = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok(medicalRecordDTO);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        List<MedicalRecordDTO> medicalRecords = medicalRecordService.getMedicalRecordsByPatientId(patientId);
        return ResponseEntity.ok(medicalRecords);
    }
}
