package com.familydoctor.models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MedicalRecordDTO {
    private Long id;

    @NotBlank(message = "Symptoms cannot be blank")
    private String symptoms;

    @NotBlank(message = "Diagnostic cannot be blank")
    private String diagnostic;

    @NotBlank(message = "Treatment cannot be blank")
    private String treatment;

    private PatientDTO patient;

    @NotBlank(message = "Doctor first name cannot be blank")
    private String doctorFirstName;

    @NotBlank(message = "Doctor last name cannot be blank")
    private String doctorLastName;
}