package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;

public class RatingsModel {

    private String comment, reciever_uid, sender_uid, time_stamp, rating, uid, is_reviewed;

    public RatingsModel() {
    }

    public RatingsModel(String comment, String reciever_uid, String sender_uid, String time_stamp, String rating, String uid, String is_reviewed) {
        this.comment = comment;
        this.reciever_uid = reciever_uid;
        this.sender_uid = sender_uid;
        this.time_stamp = time_stamp;
        this.rating = rating;
        this.uid = uid;
        this.is_reviewed = is_reviewed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReciever_uid() {
        return reciever_uid;
    }

    public void setReciever_uid(String reciever_uid) {
        this.reciever_uid = reciever_uid;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIs_reviewed() {
        return is_reviewed;
    }

    public void setIs_reviewed(String is_reviewed) {
        this.is_reviewed = is_reviewed;
    }
}
