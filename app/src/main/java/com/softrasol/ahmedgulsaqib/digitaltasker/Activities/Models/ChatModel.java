package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;

public class ChatModel {

    public ChatModel() {
    }

    private String sender_id, reciever_id, message, is_seen, uid, date_time;

    public ChatModel(String sender_id, String reciever_id, String message, String is_seen, String uid, String date_time) {
        this.sender_id = sender_id;
        this.reciever_id = reciever_id;
        this.message = message;
        this.is_seen = is_seen;
        this.uid = uid;
        this.date_time = date_time;
    }


    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIs_seen() {
        return is_seen;
    }

    public void setIs_seen(String is_seen) {
        this.is_seen = is_seen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
