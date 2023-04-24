package com.example.hadconsentservice.bean;

public class AuthenticationResponse {

    private String accessToken;
    private String name;
    private String id;
    private String role;

    private String guardianID;

    public String getGuardianID() {
        return guardianID;
    }

    public void setGuardianID(String guardianID) {
        this.guardianID = guardianID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}