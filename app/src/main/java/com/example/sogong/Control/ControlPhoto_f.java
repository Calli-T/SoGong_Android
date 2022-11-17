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
    RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
    public void lookupPhoto(int postId){
        Call<PhotoPost> call = sv.LookupPhoto(postId);

        call.enqueue(new Callback<PhotoPost>() {
            @Override
            public void onResponse(@NonNull Call<PhotoPost> call, @NonNull Response<PhotoPost> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            Log.d("result", response.body().toString());
                        }
                    } else { // 500
                        Log.d("result", "디비 오류");
                    }
                } else { // 502
                    Log.d("result", "알 수 없는 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoPost> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
                Log.d("result", t.getMessage());
            }
        });
    }
    public void addPhoto(PhotoPost newPhoto){
        Call<PhotoPost> call = sv.AddPhoto(newPhoto);

        call.enqueue(new Callback<PhotoPost>() {
            @Override
            public void onResponse(@NonNull Call<PhotoPost> call, @NonNull Response<PhotoPost> response) {
                // 200
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        if(response.code() == 200) {
                            Log.d("result", response.body().toString());
                        }
                    } else { // 500
                        Log.d("result", "디비 오류");
                    }
                } else { // 502
                    Log.d("result", "알 수 없는 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotoPost> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
                Log.d("result", t.getMessage());
            }
        });
    }
    public void deletePhoto(String nickname, int postId){
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
                    } else { // 500
                        Log.d("result", "디비 오류");
                    }
                } else { // 502
                    Log.d("result", "알 수 없는 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
                Log.d("result", t.getMessage());
            }
        });
    }
    public void showPhoto(PhotoPost photoPost){}
    public Boolean isMine(String nickname){
        Boolean result = true;
        return result;
    }
}
