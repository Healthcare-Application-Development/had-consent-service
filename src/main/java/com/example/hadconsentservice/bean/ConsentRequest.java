package com.example.hadconsentservice.bean;


public class ConsentRequest {
    private String requestBody;
    private Integer itemId;



    private Integer doctorId;
    private Integer patientId;
    private Boolean consentAcknowledged;
    private String fromDate;
    private String toDate;

    private Boolean approved;

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
    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public ConsentRequest(String requestBody, Integer itemId, Integer doctorId, Integer patientId, Boolean consentAcknowledged, String fromDate, String toDate, Boolean approved, Integer hospitalId) {
        this.requestBody = requestBody;
        this.itemId = itemId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.consentAcknowledged = consentAcknowledged;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.approved = approved;
        this.hospitalId = hospitalId;
    }

    public void setPatientId(Integer patientId) {
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
}
