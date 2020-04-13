package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ProfileSetupModel {

    private String name, email, price , description, address, category,
            profile_img, cnic_front_img, cnic_back_img, lat, lng, is_verified, is_restrict
            , date, uid, phone;


    public ProfileSetupModel() {

    }

    public ProfileSetupModel(String name, String email, String price, String description,
                             String address, String category, String profile_img,
                             String cnic_front_img, String cnic_back_img, String lat, String lng,
                             String is_verified, String is_restrict, String date, String uid,
                             String phone) {
        this.name = name;
        this.email = email;
        this.price = price;
        this.description = description;
        this.address = address;
        this.category = category;
        this.profile_img = profile_img;
        this.cnic_front_img = cnic_front_img;
        this.cnic_back_img = cnic_back_img;
        this.lat = lat;
        this.lng = lng;
        this.is_verified = is_verified;
        this.is_restrict = is_restrict;
        this.date = date;
        this.uid = uid;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getCnic_front_img() {
        return cnic_front_img;
    }

    public void setCnic_front_img(String cnic_front_img) {
        this.cnic_front_img = cnic_front_img;
    }

    public String getCnic_back_img() {
        return cnic_back_img;
    }

    public void setCnic_back_img(String cnic_back_img) {
        this.cnic_back_img = cnic_back_img;
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

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getIs_restrict() {
        return is_restrict;
    }

    public void setIs_restrict(String is_restrict) {
        this.is_restrict = is_restrict;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
