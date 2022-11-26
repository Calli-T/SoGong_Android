package com.example.sogong.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {
    @SerializedName("comment_id")
    private int comment_id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("post_id")
    private int post_id;
    @SerializedName("comments")
    private String comments;
    @SerializedName("comment_time")
    private String comment_time;

    public Comment() {}
    public Comment(int comment_id, String nickname, int post_id, String comments, String comment_time) {
        this.comment_id = comment_id;
        this.nickname = nickname;
        this.post_id = post_id;
        this.comments = comments;
        this.comment_time = comment_time;
    }

    protected Comment(Parcel in) {
        comment_id = in.readInt();
        nickname = in.readString();
        post_id = in.readInt();
        comments = in.readString();
        comment_time = in.readString();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id=" + comment_id +
                ", nickname='" + nickname + '\'' +
                ", post_id=" + post_id +
                ", comments='" + comments + '\'' +
                ", comment_time='" + comment_time + '\'' +
                '}';
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(comment_id);
        parcel.writeString(nickname);
        parcel.writeInt(post_id);
        parcel.writeString(comments);
        parcel.writeString(comment_time);
    }
}