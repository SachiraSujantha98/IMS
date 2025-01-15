package com.hospital.controller;

import com.hospital.model.MedicalTest;
import com.hospital.model.Patient;
import com.hospital.model.User;
import com.hospital.service.MedicalTestService;
import com.hospital.service.PatientService;
import com.hospital.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicalTestService medicalTestService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Long doctorId = (Long) session.getAttribute("userId");
        if (doctorId == null) {
            return "redirect:/login";
        }

        User doctor = userService.getUserById(doctorId);
        model.addAttribute("patients", patientService.getPatientsByDoctor(doctor));
        return "doctor/dashboard";
    }

    @GetMapping("/patient/{id}")
    public String viewPatient(@PathVariable Long id, Model model, HttpSession session) {
        Long doctorId = (Long) session.getAttribute("userId");
        if (doctorId == null) {
            return "redirect:/login";
        }

        Patient patient = patientService.getPatientById(id);
        if (patient == null || !patient.getDoctor().getId().equals(doctorId)) {
            return "redirect:/doctor/dashboard";
        }

        model.addAttribute("patient", patient);
        model.addAttribute("tests", medicalTestService.getTestsByPatient(patient));
        return "doctor/patient-details";
    }

    @PostMapping("/recommend-test")
    public String recommendTest(@ModelAttribute MedicalTest test, HttpSession session) {
        Long doctorId = (Long) session.getAttribute("userId");
        if (doctorId == null) {
            return "redirect:/login";
        }

        MedicalTest savedTest = medicalTestService.createTest(test);
        return "redirect:/doctor/patient/" + savedTest.getPatient().getId();
    }

    @PostMapping("/add-prescription/{testId}")
    public String addPrescription(@PathVariable Long testId,
                                  @RequestParam String prescription,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Long doctorId = (Long) session.getAttribute("userId");
        if (doctorId == null) {
            return "redirect:/login";
        }

        MedicalTest test = medicalTestService.getTestById(testId);
        if (test == null || !test.getPatient().getDoctor().getId().equals(doctorId)) {
            return "redirect:/doctor/dashboard";
        }

        test.setPrescription(prescription);
        test.setCompleted(true);  // Mark as completed when prescription is added
        MedicalTest updatedTest = medicalTestService.updateTest(test);

        redirectAttributes.addFlashAttribute("success", "Prescription added successfully");
        return "redirect:/doctor/patient/" + test.getPatient().getId();
    }
}