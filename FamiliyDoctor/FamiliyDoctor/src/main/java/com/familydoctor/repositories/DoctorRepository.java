package com.familydoctor.repositories;

import com.familydoctor.models.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Object> findByFirstNameAndLastName(String doctorFirstName, String doctorLastName);
}
