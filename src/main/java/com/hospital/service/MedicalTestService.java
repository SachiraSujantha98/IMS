package com.hospital.service;

import com.hospital.model.MedicalTest;
import com.hospital.model.Patient;
import com.hospital.repository.MedicalTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalTestService {
    @Autowired
    private MedicalTestRepository medicalTestRepository;

    @Autowired
    private PatientService patientService;

    public MedicalTest createTest(MedicalTest test) {
        test.setCreatedAt(LocalDateTime.now());
        test.setUpdatedAt(LocalDateTime.now());
        return medicalTestRepository.save(test);
    }

    public MedicalTest getTestById(Long id) {
        return medicalTestRepository.findById(id).orElse(null);
    }

    public List<MedicalTest> getTestsByPatient(Patient patient) {
        return medicalTestRepository.findByPatient(patient);
    }

    public List<MedicalTest> getPendingTests() {
        return medicalTestRepository.findByCompletedFalse();
    }

    public List<MedicalTest> getPendingCostTests() {
        List<MedicalTest> tests = medicalTestRepository.findTestsNeedingCostAssignment();
        // Debug logging
        System.out.println("Found " + tests.size() + " tests needing cost assignment");
        for (MedicalTest test : tests) {
            System.out.println("Test ID: " + test.getId() +
                    ", Completed: " + test.getCompleted() +
                    ", Has Prescription: " + (test.getPrescription() != null) +
                    ", Current Cost: " + test.getCost());
        }
        return tests;
    }

    public MedicalTest updateTest(MedicalTest test) {
        test.setUpdatedAt(LocalDateTime.now());

        // If prescription is being added, ensure test is marked as completed
        if (test.getPrescription() != null && !test.getCompleted()) {
            test.setCompleted(true);
        }

        MedicalTest savedTest = medicalTestRepository.save(test);

        // Update patient's diagnosis when prescription is added
        if (savedTest.getPrescription() != null) {
            Patient patient = savedTest.getPatient();
            if (patient.getDiagnosis() == null || patient.getDiagnosis().equals("Not yet diagnosed")) {
                patient.setDiagnosis(savedTest.getPrescription());
                patientService.updatePatient(patient);
            }
        }

        return savedTest;
    }

    public void deleteTest(Long id) {
        medicalTestRepository.deleteById(id);
    }
}