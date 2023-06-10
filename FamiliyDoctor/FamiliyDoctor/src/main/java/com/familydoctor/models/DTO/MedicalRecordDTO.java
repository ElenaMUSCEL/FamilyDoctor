package com.familydoctor.models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MedicalRecordDTO {
    @NotBlank
    private Long id;
    @NotBlank
    private String patientFirstName;
    @NotBlank
    private String patientLastName;
    @NotBlank
    private String doctorFirstName;
    @NotBlank
    private String doctorLastName;
    @NotBlank
    private String symptoms;
    @NotBlank
    private String diagnostic;
    @NotBlank
    private String treatment;
    @NotBlank
    private Long patientId;
    @NotBlank
    public Long DoctorId;
}
