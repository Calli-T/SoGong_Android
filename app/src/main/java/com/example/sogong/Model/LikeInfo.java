package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class LikeInfo {
    @SerializedName("like_id")
    private int like_id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("postType")
    private int postType;
    @SerializedName("postId")
    private int postId;
    @SerializedName("task")
    private String task;

    public LikeInfo() {}
    public LikeInfo(int like_id, String nickname, int postType, int postId, String task) {
        this.like_id = like_id;
        this.nickname = nickname;
        this.postType = postType;
        this.postId = postId;
        this.task = task;
    }

    public int getLike_id() {
        return like_id;
    }

    public void setLike_id(int like_id) {
        this.like_id = like_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}

/*
{
	"like_id": 0 or null,
	"nickname": 시스템을 사용중인 사용자 닉네임,
	"postType": 게시글 종류 1==레시피, -1==사진,
	"postId": 게시글 아이디,
	"task": "취소" or "등록" -> "좋아요"를 취소할지 등록할지 정하는 것
}
 */