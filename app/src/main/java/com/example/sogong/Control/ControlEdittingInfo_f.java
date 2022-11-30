package com.example.sogong.Control;

import android.util.Log;

import com.example.sogong.Model.User;
import com.example.sogong.View.ChangeNicknameActivity;
import com.example.sogong.View.ChangePasswordActivity;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlEdittingInfo_f {
    public void editPassword(String pw) {
        Log.d("받은 pw", "pw = " + pw);
        User old = ControlLogin_f.userinfo;
        User newUser = new User(old.getNickname(), old.getUid(), pw, old.getEmail(), old.isAuto_login());

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.EditPassword(newUser);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // 코드 전송
                ChangePasswordActivity.responseCode = response.code();
                Log.d("result", response.code()+"");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ChangePasswordActivity.responseCode = 502;
                Log.d("result", "실패");
            }
        });
    }

    public void editNickname(String new_nickname) {
        Log.d("받은 new_nickname", "new_nickname = " + new_nickname);
        User old = ControlLogin_f.userinfo;
        User newUser = new User(new_nickname, old.getUid(), old.getPassword(), old.getEmail(), old.isAuto_login());

        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.EditNickname(newUser);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                ChangeNicknameActivity.responseCode = response.code();
                Log.d("result", response.code()+"");
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ChangeNicknameActivity.responseCode = 502;
            }
        });

    }

}
