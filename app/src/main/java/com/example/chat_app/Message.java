package com.example.chat_app;

import android.provider.ContactsContract;

import java.util.Date;

public class Message {
    public String username;
    public String textmessage;
    private long messagetime;

    public Message(){}
    public Message (String username, String textmessage){

        this.username = username;
        this.textmessage = textmessage;

        this.messagetime = new Date().getTime();


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTextmessage() {
        return textmessage;
    }

    public void setTextmessage(String textmessage) {
        this.textmessage = textmessage;
    }

    public long getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(long messagetime) {
        this.messagetime = messagetime;
    }
}
