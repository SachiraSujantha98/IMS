package com.hospital.repository;

import com.hospital.model.Patient;
import com.hospital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByDoctor(User doctor);
    Patient findByUser_Id(Long userId);
}