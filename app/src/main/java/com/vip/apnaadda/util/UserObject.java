package com.vip.apnaadda.util;

import com.google.firebase.Timestamp;

public class UserObject {

    private String name;
    private String gender;
    private String uid;
    private String imageUrl;
    private String status;
    private Timestamp timeAdded;

    public UserObject() {}

    public UserObject(String name, String gender, String uid, String imageUrl, String status, Timestamp timeAdded) {
        this.name = name;
        this.gender = gender;
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.status = status;
        this.timeAdded = timeAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
