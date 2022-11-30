package com.example.sogong.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sogong.Model.Mail;
import com.example.sogong.Model.MailList;
import com.example.sogong.View.MailLookupActivity;
import com.example.sogong.View.MailSendActivity;
import com.example.sogong.View.RetrofitClient;
import com.example.sogong.View.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlMail_f {

    Mail mail;

    public void lookupMail(int mailId) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Mail> call = sv.LookupMail(mailId);

        call.enqueue(new Callback<Mail>() {
            @Override
            public void onResponse(@NonNull Call<Mail> call, @NonNull Response<Mail> response) {
                MailLookupActivity.responseCode = response.code();
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            mail = response.body();
                            Log.d("result", mail.toString());
                            MailLookupActivity.mail = response.body();
                        }
                    }
                } else { // 404
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Mail> call, @NonNull Throwable t) { // 500
                MailLookupActivity.responseCode = 500;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void sendMail(Mail mail) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Call<Mail> call = sv.SendMail(mail);
        Log.d("send mail", mail.toString());
        call.enqueue(new Callback<Mail>() {
            @Override
            public void onResponse(@NonNull Call<Mail> call, @NonNull Response<Mail> response) {
                MailSendActivity.responseCode = response.code();

                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", response.body().toString());
                        }
                    }
                } else { // 404
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Mail> call, @NonNull Throwable t) { // 500
                MailSendActivity.responseCode = 500;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void deleteMail(String nickname, int mailId) {

        RetrofitService sv = RetrofitClient.getClient().create(RetrofitService.class);
        Mail mail = new Mail();
        mail.setMail_id(mailId);
        mail.setNickname(nickname);
        Call<Integer> call = sv.DeleteMail(mail);
        Log.d("delete mail", mail.toString());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                MailLookupActivity.responseCode = response.code();
                // 200
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.code() == 200) {
                            Log.d("result", "" + response.body());
                        }
                    }
                } else { // 404
                    Log.d("result", "디비 오류");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) { // 500
                MailLookupActivity.responseCode = 500;
                Log.d("result", "알 수 없는 오류");
            }
        });
    }

    public void showMail(Mail mail) {
    }
}
