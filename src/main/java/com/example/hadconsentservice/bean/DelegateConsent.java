package com.example.hadconsentservice.bean;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class DelegateConsent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fromDocID;

    private String toDocID;

    private String patientID;
    
    @OneToOne
    @JoinColumn(name="consentItemID", referencedColumnName = "id")
    private ConsentItem consentItemID;

    private Date fromDate;

    private Date toDate;

    private String recordType;

    private Boolean isDelegated;

    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromDocID() {
        return fromDocID;
    }

    public void setFromDocID(String fromDocID) {
        this.fromDocID = fromDocID;
    }

    public String getToDocID() {
        return toDocID;
    }

    public void setToDocID(String toDocID) {
        this.toDocID = toDocID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
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

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public ConsentItem getConsentItemID() {
        return consentItemID;
    }

    public void setConsentItemID(ConsentItem consentItemID) {
        this.consentItemID = consentItemID;
    }

    public Boolean getIsDelegated() {
        return isDelegated;
    }

    public void setIsDelegated(Boolean isDelegated) {
        this.isDelegated = isDelegated;
    }

    
}
