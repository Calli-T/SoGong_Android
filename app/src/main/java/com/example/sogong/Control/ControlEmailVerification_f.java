package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.TempEmail;
import com.example.sogong.View.EmailVerificationActivity;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlEmailVerification_f {
    public void authStart(String email) {
        Log.d("받은 email", "email = " + email);
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.AuthStart(new TempEmail(email, "123456"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful())// && response.body() != null)
                {
                    EmailVerificationActivity.responseCode = response.code();

                } else {
                    EmailVerificationActivity.responseCode = response.code();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void authFinish(String email, String code) {
        Log.d("받은 email과 code", "email = " + email + " code = " + code);
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.AuthFinish(new TempEmail(email, code));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                EmailVerificationActivity.responseCode = response.code();

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }

}
