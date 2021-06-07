package com.example.localdiskieapp;

public class StoreUser {
    private String UserId,usernames;

    public StoreUser(String userId, String usernames) {
        UserId = userId;
        this.usernames = usernames;
    }

    public StoreUser() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsernames() {
        return usernames;
    }

    public void setUsernames(String usernames) {
        this.usernames = usernames;
    }
}
