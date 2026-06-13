package com.doctor_service.controller;

import com.doctor_service.config.S3Config;
import com.doctor_service.entity.Doctor;
import com.doctor_service.repository.DoctorRepository;
import com.doctor_service.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin(origins = "*")   // ← CORS fix
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private S3Service s3Service;

    @PostMapping("/create-profile")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        if (doctor.getAppointmentSchedules() != null) {
            doctor.getAppointmentSchedules().forEach(schedule -> {
                schedule.setDoctor(doctor);
                if (schedule.getTimeSlots() != null) {
                    schedule.getTimeSlots().forEach(slot -> {
                        slot.setDoctorAppointmentSchedule(schedule);
                    });
                }
            });
        }

        Doctor savedDoctor = doctorRepository.save(doctor);
        return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/getdoctorbyid")
    public Doctor getDoctorById(@RequestParam Long id){
        return doctorRepository.findById(id).orElse(null);
    }



        @PostMapping("/upload")
        public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

            try {
                return ResponseEntity.ok(s3Service.uploadImage(file));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Upload Failed");
            }
        }

}