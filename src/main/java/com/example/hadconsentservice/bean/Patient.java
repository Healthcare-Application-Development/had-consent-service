package com.example.hadconsentservice.bean;

import jakarta.persistence.*;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientID;

    private String patientName;

    private String dob;

    private Integer phone_number;

    public Patient(Integer patientID, String patientName, String dob, Integer phone_number) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.dob = dob;
        this.phone_number = phone_number;
    }

    public Patient() {
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return dob;
    }

    public void setPatientAge(Integer patientAge) {
        this.dob = dob;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }
}
