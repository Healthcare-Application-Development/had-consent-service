package com.example.hadconsentservice.bean;


public class ConsentRequest {
    private String requestBody;
    private Integer itemId;



    private String doctorId;
    private String patientId;
    private Boolean consentAcknowledged;
    private String fromDate;
    private String toDate;

    private Boolean approved;
    private Boolean ongoing;

    public Boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    public String getFromDate() {
        return fromDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    private Integer hospitalId;

    private Boolean isDelegated;
    
    private Boolean delegationApproved;

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }


    public Boolean getConsentAcknowledged() {
        return consentAcknowledged;
    }

    public void setConsentAcknowledged(Boolean consentAcknowledged) {
        this.consentAcknowledged = consentAcknowledged;
    }
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public ConsentRequest(String requestBody, Integer itemId, String doctorId, String patientId, Boolean consentAcknowledged, String fromDate, String toDate, Boolean approved, Boolean ongoing, Integer hospitalId) {
        this.requestBody = requestBody;
        this.itemId = itemId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.consentAcknowledged = consentAcknowledged;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.approved = approved;
        this.ongoing = ongoing;
        this.hospitalId = hospitalId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public ConsentRequest() {

    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Boolean getIsDelegated() {
        return isDelegated;
    }

    public void setIsDelegated(Boolean isDelegated) {
        this.isDelegated = isDelegated;
    }

    public Boolean getDelegationApproved() {
        return delegationApproved;
    }

    public void setDelegationApproved(Boolean delegationApproved) {
        this.delegationApproved = delegationApproved;
    }
}
