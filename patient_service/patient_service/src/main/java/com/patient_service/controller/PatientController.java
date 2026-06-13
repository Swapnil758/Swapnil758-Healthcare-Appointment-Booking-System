package com.patient_service.controller;

import com.patient_service.entity.Patient;
import com.patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")       
@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    
    @GetMapping("/getpatientbyid")
    public Patient getPatientById(@RequestParam long id) {
        return patientRepository.findById(id).get();
    }

    
    @PostMapping("/register")
    public ResponseEntity<Patient> registerPatient(
            @RequestBody Patient patient) {
        Patient saved = patientRepository.save(patient);
        return ResponseEntity.ok(saved);
    }

    
    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }
}
