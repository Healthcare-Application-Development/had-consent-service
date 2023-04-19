package com.example.hadconsentservice.bean;

import java.util.Date;

public class PatientHealthRecord {
    public PatientHealthRecord(String abhaId, String recordType, Date timestamp, String hospitalName) {
        this.abhaId = abhaId;
        this.recordType = recordType;
        this.timestamp=timestamp;
        this.hospitalName=hospitalName;
    }
    public PatientHealthRecord(){}
    private String abhaId;
    String recordType;
    Date timestamp;
    String hospitalName;
    String description;
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAbhaId() {
        return abhaId;
    } 
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAbhaId(String abhaId) {
        this.abhaId = abhaId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
}
