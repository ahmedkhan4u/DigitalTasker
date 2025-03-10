package com.softrasol.ahmedgulsaqib.digitaltasker.Activities.Models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class HomeCategoryModel {

    private String category_name;
    private int category_image;

    public HomeCategoryModel() {
    }

    public HomeCategoryModel(String category_name, int category_image) {
        this.category_name = category_name;
        this.category_image = category_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_image() {
        return category_image;
    }

    public void setCategory_image(int category_image) {
        this.category_image = category_image;
    }
}
