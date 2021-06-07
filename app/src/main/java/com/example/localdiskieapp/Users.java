package com.example.localdiskieapp;

public class Users {
   private String UID,name,teamname,picture;

    public Users(String UID, String name, String teamname, String picture) {
        this.UID = UID;
        this.name = name;
        this.teamname = teamname;
        this.picture = picture;
    }

    public Users() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
