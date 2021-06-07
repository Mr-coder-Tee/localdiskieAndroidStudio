package com.example.localdiskieapp;

public class TeamMemberType {
    private String Type,UID;

    public TeamMemberType(String type, String UID) {
        Type = type;
        this.UID = UID;
    }

    public TeamMemberType() {
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
