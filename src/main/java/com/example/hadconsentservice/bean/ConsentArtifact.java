package com.example.hadconsentservice.bean;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "consent_artifact")
public class ConsentArtifact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artifactId;

    @Transient
    private String delegationID;
    
    private String doctorID;

    private String patientID;

    private Date timestamp;

    private boolean emergency;

    @Column(columnDefinition = "boolean default false")
    private boolean revoked;
    private Boolean consentAcknowledged = false;

    private Boolean approved = false;
    private Boolean ongoing;

    public Boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    public ConsentArtifact() {
    }


    public ConsentArtifact(Integer artifactId, String doctorID, String patientID, Date timestamp, boolean emergency, Boolean consentAcknowledged, Boolean approved, Boolean ongoing, List<ConsentItem> consentItems,boolean revoked) {
        this.artifactId = artifactId;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.timestamp = timestamp;
        this.emergency = emergency;
        this.revoked = revoked;
        this.consentAcknowledged = consentAcknowledged;
        this.approved = approved;
        this.ongoing = ongoing;
        this.consentItems = consentItems;
    }



    @OneToMany(mappedBy = "consentArtifact")
    private List<ConsentItem> consentItems;

    public Integer getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(Integer artifactId) {
        this.artifactId = artifactId;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public void setConsentItems(List<ConsentItem> consentItems) {
        this.consentItems = consentItems;
    }

    public List<ConsentItem> getConsentItems() {
        return Collections.unmodifiableList(consentItems);
    }

    public Boolean getConsentAcknowledged() {
        return consentAcknowledged;
    }

    public void setConsentAcknowledged(Boolean consentAcknowledged) {
        this.consentAcknowledged = consentAcknowledged;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "ConsentArtifact{" +
                "artifactId=" + artifactId +
                ", doctorID=" + doctorID +
                ", patientID=" + patientID +
                ", timestamp='" + timestamp + '\'' +
                ", emergency=" + emergency +
                ", revoked=" + revoked +
                ", consentItems=" + consentItems +
                '}';
    }

    public String getDelegationID() {
        return delegationID;
    }

    public void setDelegationID(String delegationID) {
        this.delegationID = delegationID;
    }
}
