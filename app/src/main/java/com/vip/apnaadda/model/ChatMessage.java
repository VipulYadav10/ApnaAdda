package com.vip.apnaadda.model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class ChatMessage {

    private String text;
    private String from;
    private int state;
    private Timestamp time;

    public ChatMessage(String text, String from, int state, Timestamp time) {
        this.text = text;
        this.from = from;
        this.state = state;
        this.time = time;
    }

    public ChatMessage() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
