package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class WorkRequestModel {

    private String title, description, price, date_time, lat, lng, address, sender_uid, time_stamp, uid;

    public WorkRequestModel() {
    }

    public WorkRequestModel(String title, String description, String price, String date_time, String lat, String lng, String address, String sender_uid, String time_stamp, String uid) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.date_time = date_time;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.sender_uid = sender_uid;
        this.time_stamp = time_stamp;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
