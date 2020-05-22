package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;


import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class NotificationsModel {

    private String title, body, time_stamp, sender_uid, reciever_uid, is_seen, type, uid;

    public NotificationsModel() { }

    public NotificationsModel(String title, String body, String time_stamp, String sender_uid, String reciever_uid, String is_seen, String type, String uid) {
        this.title = title;
        this.body = body;
        this.time_stamp = time_stamp;
        this.sender_uid = sender_uid;
        this.reciever_uid = reciever_uid;
        this.is_seen = is_seen;
        this.type = type;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getReciever_uid() {
        return reciever_uid;
    }

    public void setReciever_uid(String reciever_uid) {
        this.reciever_uid = reciever_uid;
    }

    public String getIs_seen() {
        return is_seen;
    }

    public void setIs_seen(String is_seen) {
        this.is_seen = is_seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
