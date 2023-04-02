package com.example.hadconsentservice.bean;
import jakarta.persistence.*;

@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uprnID;
    private String name;

    public Doctor(){}

    public Doctor(Integer id, String uprnID, String name) {
        this.id = id;
        this.uprnID = uprnID;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUprn_id() {
        return uprnID;
    }

    public void setUprn_id(String uprn_id) {
        this.uprnID = uprn_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


