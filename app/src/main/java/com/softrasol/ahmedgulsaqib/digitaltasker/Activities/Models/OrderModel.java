package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;

public class OrderModel {

    private String time, budget, description, sender_id, reciever_id, request_id, uid, time_stamp, is_accepted, status;

    public OrderModel() { }

    public OrderModel(String time, String budget, String description, String sender_id, String reciever_id, String request_id, String uid, String time_stamp, String is_accepted, String status) {
        this.time = time;
        this.budget = budget;
        this.description = description;
        this.sender_id = sender_id;
        this.reciever_id = reciever_id;
        this.request_id = request_id;
        this.uid = uid;
        this.time_stamp = time_stamp;
        this.is_accepted = is_accepted;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(String is_accepted) {
        this.is_accepted = is_accepted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
