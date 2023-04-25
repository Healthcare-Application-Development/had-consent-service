package com.example.hadconsentservice.bean;

import jakarta.persistence.*;


//Get request from app
//Get ID and password of patient from the db
//login as a patient and return page
@Entity
public class Guardian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guardianID;

    private String guardianName;

    private Long assignedPatientID;

    public Guardian(Long guardianID, String guardianName, Long assignedPatientID) {
        this.guardianID = guardianID;
        this.guardianName = guardianName;
        this.assignedPatientID = assignedPatientID;
    }

    public Guardian() {
    }

    public Long getGuardianID() {
        return guardianID;
    }

    public void setGuardianID(Long guardianID) {
        this.guardianID = guardianID;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public Long getAssignedPatientID() {
        return assignedPatientID;
    }

    public void setAssignedPatientID(Long assignedPatientID) {
        this.assignedPatientID = assignedPatientID;
    }
}
