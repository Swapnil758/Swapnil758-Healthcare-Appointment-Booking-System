package com.mediscan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrescriptionResponseDTO {

    private String doctor;
    private String patient;
    private String date;
    private List<MedicineDTO> medicines;
    private String notes;
    private String error;

    public PrescriptionResponseDTO() {}

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getPatient() { return patient; }
    public void setPatient(String patient) { this.patient = patient; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<MedicineDTO> getMedicines() { return medicines; }
    public void setMedicines(List<MedicineDTO> medicines) { this.medicines = medicines; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}