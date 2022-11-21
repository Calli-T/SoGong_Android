package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class Mail {
    @SerializedName("mail_id")
    private int mail_id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("receiver")
    private String receiver;
    @SerializedName("title")
    private String title;
    @SerializedName("contents")
    private String contents;
    @SerializedName("send_time")
    private String send_time;
    @SerializedName("sender_check")
    private boolean sender_check;
    @SerializedName("receiver_check")
    private boolean receiver_check;

    public Mail() {}
    public Mail(int mail_id, String nickname, String receiver, String title, String contents, String send_time, boolean sender_check, boolean receiver_check) {
        this.mail_id = mail_id;
        this.nickname = nickname;
        this.receiver = receiver;
        this.title = title;
        this.contents = contents;
        this.send_time = send_time;
        this.sender_check = sender_check;
        this.receiver_check = receiver_check;
    }

    public int getMail_id() {
        return mail_id;
    }

    public void setMail_id(int mail_id) {
        this.mail_id = mail_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public boolean isSender_check() {
        return sender_check;
    }

    public void setSender_check(boolean sender_check) {
        this.sender_check = sender_check;
    }

    public boolean isReceiver_check() {
        return receiver_check;
    }

    public void setReceiver_check(boolean receiver_check) {
        this.receiver_check = receiver_check;
    }

    @Override
    public String toString() {
        return getContents() + " " + getNickname();
    }
}
