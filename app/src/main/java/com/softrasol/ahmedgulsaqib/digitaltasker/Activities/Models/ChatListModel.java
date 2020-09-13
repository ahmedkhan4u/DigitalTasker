package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChatListModel {

    private String id;

    public ChatListModel() {
    }

    public ChatListModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
