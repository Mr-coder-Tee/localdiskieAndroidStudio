package com.example.localdiskieapp;

public class sendNotificationHelper {

   private String senderuid,from,logo,tournamentname;
   private boolean isread,isjointeam;

    public sendNotificationHelper() {
    }

    public sendNotificationHelper(String senderuid, String from, String logo, String tournamentname, boolean isread, boolean isjointeam) {
        this.senderuid = senderuid;
        this.from = from;
        this.logo = logo;
        this.tournamentname = tournamentname;
        this.isread = isread;
        this.isjointeam = isjointeam;
    }

    public String getSenderuid() {
        return senderuid;
    }

    public void setSenderuid(String senderuid) {
        this.senderuid = senderuid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTournamentname() {
        return tournamentname;
    }

    public void setTournamentname(String tournamentname) {
        this.tournamentname = tournamentname;
    }

    public boolean isIsread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    public boolean isIsjointeam() {
        return isjointeam;
    }

    public void setIsjointeam(boolean isjointeam) {
        this.isjointeam = isjointeam;
    }
}
