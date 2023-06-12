package com.familydoctor.models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DoctorDTO {

    @NotBlank
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
   @NotBlank
    private String email;
   @NotBlank
    private String address;
   @NotBlank
    private int age;
}
