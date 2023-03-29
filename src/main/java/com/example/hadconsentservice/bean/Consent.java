package com.example.hadconsentservice.bean;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "consent_request")
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer doctorID;

    private Integer patientID;

    private String consentMessage;

    private Boolean consentAcknowledged;

    private Boolean approved;
    private Date fromDate;
    private Date toDate;
    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    private Integer hospitalId;

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Consent() {

    }

    public Consent(Integer id, Integer doctorID, Integer patientID, String consentMessage, Boolean consentAcknowledged, Boolean approved, Date fromDate, Date toDate, Integer hospitalId) {
        this.id = id;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.consentMessage = consentMessage;
        this.consentAcknowledged = consentAcknowledged;
        this.approved = approved;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.hospitalId = hospitalId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getConsentMessage() {
        return consentMessage;
    }

    public void setConsentMessage(String consentRequest) {
        this.consentMessage = consentRequest;
    }

    public Boolean getConsentAcknowledged() {
        return consentAcknowledged;
    }

    public void setConsentAcknowledged(Boolean consentAcknowledged) {
        this.consentAcknowledged = consentAcknowledged;
    }
}
