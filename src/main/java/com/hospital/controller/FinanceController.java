package com.hospital.controller;

import com.hospital.model.MedicalTest;
import com.hospital.model.Patient;
import com.hospital.service.MedicalTestService;
import com.hospital.service.PatientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/finance")
public class FinanceController {
    @Autowired
    private MedicalTestService medicalTestService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        List<MedicalTest> pendingTests = medicalTestService.getPendingCostTests();
        System.out.println("Found " + pendingTests.size() + " tests pending cost assignment");
        model.addAttribute("completedTests", pendingTests);
        return "finance/dashboard";
    }

    @PostMapping("/add-cost/{testId}")
    public String addCost(@PathVariable Long testId,
                          @RequestParam Double cost,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        try {
            MedicalTest test = medicalTestService.getTestById(testId);
            if (test == null) {
                redirectAttributes.addFlashAttribute("error", "Test not found");
                return "redirect:/finance/dashboard";
            }

            test.setCost(cost);
            test = medicalTestService.updateTest(test);

            // Update patient's total cost
            Patient patient = test.getPatient();
            patient.setTotalCost(patient.getTotalCost() + cost);
            patientService.updatePatient(patient);

            redirectAttributes.addFlashAttribute("success", "Cost added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding cost: " + e.getMessage());
        }

        return "redirect:/finance/dashboard";
    }
}