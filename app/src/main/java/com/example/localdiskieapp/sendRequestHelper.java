package com.example.localdiskieapp;

public class sendRequestHelper {
    String teamname,username,logo,tournamentname;

    public sendRequestHelper(String teamname, String username, String logo, String tournamentname) {
        this.teamname = teamname;
        this.username = username;
        this.logo = logo;
        this.tournamentname = tournamentname;
    }

    public String getTournamentname() {
        return tournamentname;
    }

    public void setTournamentname(String tournamentname) {
        this.tournamentname = tournamentname;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public sendRequestHelper() {
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }
}
