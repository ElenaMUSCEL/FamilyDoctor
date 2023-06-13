package com.familydoctor.repositories;

import com.familydoctor.models.entities.Doctor;
import com.familydoctor.models.entities.MedicalRecord;
import com.familydoctor.models.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByDoctorAndPatient(Doctor doctor, Patient patient);

    List<MedicalRecord> findByPatient(Patient patient);
    @Modifying
    @Query("UPDATE MedicalRecord m SET m.doctor.firstName = :firstName WHERE m.id = :recordId")
    void updateDoctorFirstName(@Param("firstName") String firstName, @Param("recordId") Long recordId);

}
