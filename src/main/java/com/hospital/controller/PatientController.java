package com.hospital.controller;

import com.hospital.model.Patient;
import com.hospital.model.MedicalTest;
import com.hospital.service.MedicalTestService;
import com.hospital.service.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicalTestService medicalTestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Get logged in user id
        Long patientId = (Long) session.getAttribute("userId");
        if (patientId == null) {
            return "redirect:/login";
        }

        // Get patient info
        Patient patient = patientService.getPatientByUserId(patientId);
        if (patient == null) {
            return "redirect:/login";
        }

        model.addAttribute("patient", patient);
        model.addAttribute("tests", medicalTestService.getTestsByPatient(patient));
        return "patient/dashboard";
    }

    @GetMapping("/test/{id}")
    public String viewTest(@PathVariable Long id, Model model, HttpSession session) {
        // Get logged in user id
        Long patientId = (Long) session.getAttribute("userId");
        if (patientId == null) {
            return "redirect:/login";
        }

        // Get the test
        MedicalTest test = medicalTestService.getTestById(id);
        if (test == null) {
            return "redirect:/patient/dashboard";
        }

        // Check if this test belongs to the logged-in patient
        Patient patient = patientService.getPatientByUserId(patientId);
        if (patient == null || !test.getPatient().getId().equals(patient.getId())) {
            return "redirect:/patient/dashboard";
        }

        model.addAttribute("test", test);
        return "patient/test-details";
    }
}