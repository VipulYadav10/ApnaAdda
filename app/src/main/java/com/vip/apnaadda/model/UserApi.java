package com.vip.apnaadda.model;

import android.app.Application;

public class UserApi extends Application {

    private String name;
    private String gender;
    private String userUid;

    private static UserApi instance;

    public static UserApi getInstance() {
        if(instance == null) {
            instance = new UserApi();
        }
        return instance;
    }

    public UserApi() {}

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

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

}
