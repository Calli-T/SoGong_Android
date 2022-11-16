package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.AuthInfo;
import com.example.sogong.Model.User;
import com.example.sogong.View.EmailVerificationActivity;
import com.example.sogong.View.LoginActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;
import com.example.sogong.View.RetrofitStringClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlEmailVerification_f {
    public void getMsg(String email) {
        RetrofitService sv = RetrofitStringClient.getClient().create(RetrofitService.class);
        Call<String> call = sv.fTest(new AuthInfo(email, "123456"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful())// && response.body() != null)
                {
                    EmailVerificationActivity.responseCode = response.code();
                    EmailVerificationActivity.testStr = response.body();
                    Log.e("String 결과값", "response.body().toString() : " + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                //Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void authStart(String email) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<AuthInfo> call = sv.AuthStart(new AuthInfo(email, "123456"));

        call.enqueue(new Callback<AuthInfo>() {
            @Override
            public void onResponse(Call<AuthInfo> call, Response<AuthInfo> response) {
                EmailVerificationActivity.responseCode = response.code();
            }

            @Override
            public void onFailure(Call<AuthInfo> call, Throwable t) {

            }
        });

    }

    public void authFinish(String email, String code) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<AuthInfo> call = sv.AuthFinish(new AuthInfo(email, code));

        call.enqueue(new Callback<AuthInfo>() {
            @Override
            public void onResponse(Call<AuthInfo> call, Response<AuthInfo> response) {
                EmailVerificationActivity.responseCode = response.code();
            }

            @Override
            public void onFailure(Call<AuthInfo> call, Throwable t) {

            }
        });

    }
}
/*
- 0. = 501
    - 이메일 전송 실패
- 1 = 200
    - 이메일 전송 완료
- 2 = 404
    - 존재하지 않는 이메일
- 3 = 400
    - 코드 틀림
- 4 = 200
    - 최종 인증 성공
- 5 = 500
    - 이메일 등록 실패
- 6 = 502
    - 알 수 없는 오류
 */

/*
/*
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            EmailVerificationActivity.responseCode = response.code();
                        }
                    } else // 404
                        LoginActivity.responseCode = response.code();
                        Log.d("404 Not Found", "with email verification");
                } else if(response.code() == 400) {
                    LoginActivity.responseCode = response.code();
                } else {
                    LoginActivity.responseCode = response.code();
                }
*/
