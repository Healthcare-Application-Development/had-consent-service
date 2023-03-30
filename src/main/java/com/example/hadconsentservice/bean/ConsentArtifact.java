package com.example.hadconsentservice.bean;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Entity
@Table(name = "consent_artifact")
public class ConsentArtifact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artifactId;

//    @ManyToOne
//    @JoinColumn(name = "uprnID", nullable = false)
    private Integer doctorID;

//    @ManyToOne
//    @JoinColumn(name = "patientID", nullable = false)
    private Integer patientID;

    private String timestamp;

    private boolean emergency;

    public ConsentArtifact() {
    }

    public ConsentArtifact(Integer artifactId, Integer doctorID, Integer patientID, String timestamp, boolean emergency, List<ConsentItem> consentItems) {
        this.artifactId = artifactId;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.timestamp = timestamp;
        this.emergency = emergency;
        this.consentItems = consentItems;
    }

    @OneToMany
    private List<ConsentItem> consentItems = new ArrayList<>();

    public Integer getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(Integer artifactId) {
        this.artifactId = artifactId;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public void setConsentItems(List<ConsentItem> consentItems) {
        this.consentItems = consentItems;
    }

    public List<ConsentItem> getConsentItems() {
        return Collections.unmodifiableList(consentItems);
    }

    @Override
    public String toString() {
        return "ConsentArtifact{" +
                "artifactId=" + artifactId +
                ", doctorID=" + doctorID +
                ", patientID=" + patientID +
                ", timestamp='" + timestamp + '\'' +
                ", emergency=" + emergency +
                ", consentItems=" + consentItems +
                '}';
    }
}
