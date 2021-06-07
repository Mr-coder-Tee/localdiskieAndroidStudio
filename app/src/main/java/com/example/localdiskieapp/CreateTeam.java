package com.example.localdiskieapp;

public class CreateTeam {
    private String teamname,UID,picture,location;

    public CreateTeam(String teamname, String UID, String picture, String location) {
        this.teamname = teamname;
        this.UID = UID;
        this.picture = picture;
        this.location = location;
    }

    public CreateTeam() {
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
