package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.PhotoList;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlPhoto_f {

    PhotoPost photoPost;

    public void lookupPhoto(int postId){
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<PhotoPost> call = sv.LookupPhoto(postId);

        call.enqueue(new Callback<PhotoPost>() {
            @Override
            public void onResponse(@NonNull Call<PhotoPost> call, @NonNull Response<PhotoPost> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            photoPost = response.body();
                            Log.d("result", photoPost.toString());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoPost> call, @NonNull Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void addPhoto(PhotoPost newPhoto){
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<PhotoPost> call = sv.AddPhoto(newPhoto);

        call.enqueue(new Callback<PhotoPost>() {
            @Override
            public void onResponse(@NonNull Call<PhotoPost> call, @NonNull Response<PhotoPost> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            photoPost = response.body();
                            Log.d("result", photoPost.toString());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoPost> call, @NonNull Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void deletePhoto(String nickname, int postId){
        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        PhotoPost deleteTarget = new PhotoPost();
        deleteTarget.setAuthor(nickname);
        deleteTarget.setPost_id(postId);
        Call<Integer> call = sv.DeletePhoto(deleteTarget);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            Log.d("result", ""+response.body());
                        }
                    }
                } else { // 500
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 502
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void showPhoto(PhotoPost photoPost){}
    public Boolean isMine(String nickname){
        Boolean result = true;
        return result;
    }
}
