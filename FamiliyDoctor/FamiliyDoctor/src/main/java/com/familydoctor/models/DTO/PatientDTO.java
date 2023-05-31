package com.familydoctor.models.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientDTO implements Serializable {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 20, message = "First name must have between 2 and 20 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 20, message = "Last name must have between 2 and 20 characters")
    private String lastName;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "07\\d{8}", message = "Phone number must be in the format '07xxxxxxxx' and have 10 characters")
    private String phoneNumber;
    @NotBlank(message = "Date of birth is required")
    @Past(message = "Year of birth must be in the past")
    //@DateTimeFormat(pattern = "dd-MM-yyyy")
    private String yearOfBirth;
    @NotBlank(message = "Gender name is required")
    @Pattern(regexp = "feminine|masculine", message = "Gender must be either 'feminine' or 'masculine'")
    private String gender;
}
