package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChatListModel {

    private String id, time_stamp;

    public ChatListModel() {
    }

    public ChatListModel(String id, String time_stamp) {
        this.id = id;
        this.time_stamp = time_stamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }
}
