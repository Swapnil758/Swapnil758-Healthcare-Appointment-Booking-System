package com.mediscan.controller;

import com.mediscan.dto.PrescriptionResponseDTO;
import com.mediscan.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/prescription")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/scan")
    public ResponseEntity<?> scanPrescription(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload an image!");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("Only image files allowed!");
        }

        System.out.println("=== New Request ===");
        System.out.println("File : " + file.getOriginalFilename());
        System.out.println("Size : " + file.getSize() + " bytes");

        try {
            PrescriptionResponseDTO response = prescriptionService.scanPrescription(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("MediScan AI is running!");
    }
}