package com.hospital.repository;

import com.hospital.model.MedicalTest;
import com.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {
    List<MedicalTest> findByPatient(Patient patient);
    List<MedicalTest> findByCompletedFalse();

    // query to find tests that need cost assignment
    @Query("SELECT t FROM MedicalTest t WHERE t.completed = true AND t.prescription IS NOT NULL AND (t.cost IS NULL OR t.cost = 0)")
    List<MedicalTest> findTestsNeedingCostAssignment();
}