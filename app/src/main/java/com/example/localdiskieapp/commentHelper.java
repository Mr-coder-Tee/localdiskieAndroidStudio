package com.example.localdiskieapp;

public class commentHelper {

    private String comment,name,uid,postdate,picuture;

    public commentHelper(String comment, String name, String uid, String postdate, String picuture) {
        this.comment = comment;
        this.name = name;
        this.uid = uid;
        this.postdate = postdate;
        this.picuture = picuture;
    }

    public commentHelper() {
    }

    public String getPicuture() {
        return picuture;
    }

    public void setPicuture(String picuture) {
        this.picuture = picuture;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
