package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class PhotoPost {
    @SerializedName("nickname")
    private String author;
    @SerializedName("post_id")
    private int post_id;
    @SerializedName("photo_link")
    private String photo_link;
    @SerializedName("like_count")
    private int like_count;
    @SerializedName("upload_time")
    private String upload_time;

    public PhotoPost() {

    }

    public PhotoPost(String author, int post_id, String photo_link, int like_count, String upload_time) {
        this.author = author;
        this.post_id = post_id;
        this.photo_link = photo_link;
        this.like_count = like_count;
        this.upload_time = upload_time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPhoto_link() {
        return photo_link;
    }

    public void setPhoto_link(String photo_link) {
        this.photo_link = photo_link;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    @Override
    public String toString() {
        return getAuthor() + " " + getPhoto_link();
    }
}
