package com.hospital.controller;

import com.hospital.model.MedicalTest;
import com.hospital.service.MedicalTestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/radiologist")
public class RadiologistController {
    @Autowired
    private MedicalTestService medicalTestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        model.addAttribute("pendingTests", medicalTestService.getPendingTests());
        return "radiologist/dashboard";
    }

    @PostMapping("/upload-image/{testId}")
    public String uploadImage(@PathVariable Long testId,
                              @RequestParam("image") MultipartFile image,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        try {
            MedicalTest test = medicalTestService.getTestById(testId);
            if (test == null) {
                redirectAttributes.addFlashAttribute("error", "Test not found");
                return "redirect:/radiologist/dashboard";
            }

            // Store the image data
            test.setImageData(image.getBytes());
            test.setImageType(image.getContentType());
            test.setCompleted(true);
            medicalTestService.updateTest(test);

            redirectAttributes.addFlashAttribute("success", "Image uploaded successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
        }

        return "redirect:/radiologist/dashboard";
    }

    @GetMapping("/image/{testId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long testId) {
        MedicalTest test = medicalTestService.getTestById(testId);
        if (test != null && test.getImageData() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(test.getImageType()))
                    .body(test.getImageData());
        }
        return ResponseEntity.notFound().build();
    }
}