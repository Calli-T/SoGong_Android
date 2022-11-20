package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.Comment;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlComment_f {
    public void writeComment(Comment comment){

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Integer> call = sv.WriteComment(comment);

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
                } else { // 403
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void editComment(Comment comment){

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Integer> call = sv.EditComment(comment);

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
                } else { // 403
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
    public void deleteComment(String nickname, int commentId){

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Comment deleteTarget = new Comment();
        deleteTarget.setNickname(nickname);
        deleteTarget.setComment_id(commentId);
        Call<Integer> call = sv.DeleteComment(deleteTarget);

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
                } else { // 403
                    Log.d("result", "디비 오류");
                }
            }
            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 500
                Log.d("result", "알 수 없는 오류");
            }
        });
    }
}
