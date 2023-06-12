package com.familydoctor.exceptions;

public class MedicalRecordNotFoundException extends RuntimeException {
    public MedicalRecordNotFoundException(String medicalRecord, Long id) {
    }
}
