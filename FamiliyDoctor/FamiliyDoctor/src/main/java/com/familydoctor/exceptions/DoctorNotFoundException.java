package com.familydoctor.exceptions;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(String patient, Long id) {
    }
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
