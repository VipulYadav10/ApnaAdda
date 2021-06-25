package com.vip.apnaadda.util;

public class RequestUserObject {

    private String name;
    private String uid;
    private int state;

    public RequestUserObject(String name, String uid, int state) {
        this.name = name;
        this.uid = uid;
        this.state = state;
    }

    public RequestUserObject() {}

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
