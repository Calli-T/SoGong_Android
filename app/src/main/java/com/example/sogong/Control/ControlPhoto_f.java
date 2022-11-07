package com.example.sogong.Control;

import com.example.sogong.Model.PhotoPost;

public class ControlPhoto_f {
    PhotoPost photoPost;
    public void lookupPhotoPost(int postId){}
    public void addPhoto(PhotoPost newPhoto){}
    public void deletePhoto(String nickname, int postId){}
    public void showPhoto(PhotoPost photoPost){}
    public Boolean isMine(String nickname){
        Boolean result = true;
        return result;
    }
}
