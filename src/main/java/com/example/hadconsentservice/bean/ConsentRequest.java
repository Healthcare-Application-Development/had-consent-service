package com.example.hadconsentservice.bean;

public class ConsentRequest {
    private String requestBody;
    private Integer doctorId;
    private Integer patientId;
    private Boolean consentAcknowledged;

    private Boolean approved;
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

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public ConsentRequest() {

    }

    public ConsentRequest(String requestBody, Integer doctorId, Integer patientId, Boolean consentAcknowledged, Boolean approved, Integer hospitalId) {
        this.requestBody = requestBody;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.consentAcknowledged = consentAcknowledged;
        this.approved = approved;
        this.hospitalId = hospitalId;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
