package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoList {
    @SerializedName("photoList")
    private List<PhotoPost> photoList;
    @SerializedName("total_page")
    private int total_page;

    public List<PhotoPost> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PhotoPost> photoList) {
        this.photoList = photoList;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    @Override
    public String toString() {
        String msg = "";
        for(int i=0; i<getPhotoList().size(); i++) {
            msg += (getPhotoList().get(i).toString() + "\n");
        }
        return msg;
    }
}
