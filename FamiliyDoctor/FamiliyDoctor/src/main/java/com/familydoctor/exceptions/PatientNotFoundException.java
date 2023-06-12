package com.familydoctor.exceptions;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class PatientNotFoundException extends RuntimeException{

    private final long patientId;

    public PatientNotFoundException(String patient, long patientId) {

        super(new StringBuilder().append("Patient not found: ").append(patient).append(" with ").append(id).append(" ").append(patientId).toString());
        this.patientId = patientId;
    }
}
