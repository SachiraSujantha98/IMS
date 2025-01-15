package com.hospital.controller;

import com.hospital.model.User;
import com.hospital.model.Patient;
import com.hospital.model.UserRole;
import com.hospital.service.UserService;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/dashboard";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        // Get all doctors for patient registration
        List<User> doctors = userService.getUsersByRole(UserRole.DOCTOR);
        model.addAttribute("doctors", doctors);
        return "admin/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, @RequestParam(required = false) Long doctorId) {
        User savedUser = userService.createUser(user);

        // If registering a patient, create patient record with assigned doctor
        if (user.getRole() == UserRole.PATIENT && doctorId != null) {
            Patient patient = new Patient();
            patient.setUser(savedUser);
            patient.setDoctor(userService.getUserById(doctorId));
            patientService.createPatient(patient);
        }

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/dashboard";
    }
}