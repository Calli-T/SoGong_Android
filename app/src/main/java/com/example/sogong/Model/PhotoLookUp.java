package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class PhotoLookUp {
    @SerializedName("photoInfo")
    private PhotoPost photoPost;

    @SerializedName("likeInfo")
    private boolean likeInfo;

    public PhotoLookUp(PhotoPost photoPost, boolean likeInfo) {
        this.photoPost = photoPost;
        this.likeInfo = likeInfo;
    }

    public PhotoPost getPhotoPost() {
        return photoPost;
    }

    public void setPhotoPost(PhotoPost photoPost) {
        this.photoPost = photoPost;
    }

    public boolean isLikeInfo() {
        return likeInfo;
    }

    public void setLikeInfo(boolean likeInfo) {
        this.likeInfo = likeInfo;
    }

    @Override
    public String toString() {
        return "PhotoLookUp{" +
                "photoPost=" + photoPost +
                ", likeInfo=" + likeInfo +
                '}';
    }
}

