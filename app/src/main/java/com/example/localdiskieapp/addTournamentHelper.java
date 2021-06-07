package com.example.localdiskieapp;

public class addTournamentHelper {
   private String tournamentname,uid,description,picture,postdate;
   private int size;
   private boolean figtureset;
   private String name;
   private String dp,teamname;

    public addTournamentHelper(String tournamentname, String uid, String description, String picture, String postdate, int size, boolean figtureset, String name, String dp, String teamname) {
        this.tournamentname = tournamentname;
        this.uid = uid;
        this.description = description;
        this.picture = picture;
        this.postdate = postdate;
        this.size = size;
        this.figtureset = figtureset;
        this.name = name;
        this.dp = dp;
        this.teamname = teamname;
    }


    //    public addTournamentHelper(String tournamentname, String uid, String description, String picture, String postdate, int size, boolean figtureset, String name) {
//        this.tournamentname = tournamentname;
//        this.uid = uid;
//        this.description = description;
//        this.picture = picture;
//        this.postdate = postdate;
//        this.size = size;
//        this.figtureset = figtureset;
//        this.name = name;
//    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public addTournamentHelper() {
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTournamentname() {
        return tournamentname;
    }

    public void setTournamentname(String tournamentname) {
        this.tournamentname = tournamentname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFigtureset() {
        return figtureset;
    }

    public void setFigtureset(boolean figtureset) {
        this.figtureset = figtureset;
    }
}
