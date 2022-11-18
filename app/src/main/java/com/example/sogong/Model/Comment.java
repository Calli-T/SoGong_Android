package com.example.sogong.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment implements Parcelable {
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("comment_id")
    @Expose
    private int comment_id;
    @SerializedName("post_id")
    @Expose
    private int post_id;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("comment_time")
    @Expose
    private String comment_time;

    protected Comment(Parcel in) {
        nickname = in.readString();
        comment_id = in.readInt();
        post_id = in.readInt();
        comments = in.readString();
        comment_time = in.readString();
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeInt(comment_id);
        dest.writeInt(post_id);
        dest.writeString(comments);
        dest.writeString(comment_time);
    }
}

//public class Comments
//{
//    private String comment_time;
//
//    private String comments;
//
//    private String post_id;
//
//    private String nickname;
//
//    private String comment_id;
//
//    public String getComment_time ()
//    {
//        return comment_time;
//    }
//
//    public void setComment_time (String comment_time)
//    {
//        this.comment_time = comment_time;
//    }
//
//    public String getComments ()
//    {
//        return comments;
//    }
//
//    public void setComments (String comments)
//    {
//        this.comments = comments;
//    }
//
//    public String getPost_id ()
//    {
//        return post_id;
//    }
//
//    public void setPost_id (String post_id)
//    {
//        this.post_id = post_id;
//    }
//
//    public String getNickname ()
//    {
//        return nickname;
//    }
//
//    public void setNickname (String nickname)
//    {
//        this.nickname = nickname;
//    }
//
//    public String getComment_id ()
//    {
//        return comment_id;
//    }
//
//    public void setComment_id (String comment_id)
//    {
//        this.comment_id = comment_id;
//    }
//
//    @Override
//    public String toString()
//    {
//        return "ClassPojo [comment_time = "+comment_time+", comments = "+comments+", post_id = "+post_id+", nickname = "+nickname+", comment_id = "+comment_id+"]";
//    }
//}
