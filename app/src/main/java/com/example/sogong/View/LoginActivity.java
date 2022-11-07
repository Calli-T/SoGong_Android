package com.example.sogong.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Model.User;
import com.example.sogong.R;
import com.example.sogong.View.Legacy.BoardListActivity;
import com.example.sogong.View.Legacy.JoinActivity;
import com.example.sogong.View.Legacy.RetrofitClient;
import com.example.sogong.View.Legacy.RetrofitService;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    EditText userid_et, passwd_et;
    Button login_button, join_button;
    TextInputLayout textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 사용할 컴포넌트 초기화
        userid_et = findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        login_button = findViewById(R.id.login_button);
        join_button = findViewById(R.id.join_button);
        textInputLayout2 = findViewById(R.id.textInputLayout2);

        // 로그인 버튼 이벤트 추가
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// 로그인 함수
                LoginTask loginTask = new LoginTask();
                loginTask.execute(userid_et.getText().toString(), passwd_et.getText().toString());
            }
        });

// 조인 버튼 이벤트 추가
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });


    }

    class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute, " + result);

            if (result.equals("success")) {
// 결과값이 success 이면
// 토스트 메시지를 뿌리고
// userid 값을 가지고 ListActivity 로 이동
                Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, BoardListActivity.class);
                intent.putExtra("userid", userid_et.getText().toString());
                startActivity(intent);
            } else if (result.equals("fail")) {
                textInputLayout2.setError("로그인실패");
                Toast.makeText(LoginActivity.this, "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String userid = params[0];
            String passwd = params[1];

            //final String[] result = {""};

            RetrofitService retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
            Call<User> call = retrofitService.getLogin(userid);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    String result = "";
                    if (response.isSuccessful()) {
                        User user = response.body();
                        Log.d("성공", user.getUserId());
                        if (passwd.equals(user.getPassword())) {
                            Log.d("성공", "비밀번호 일치");
                            result = "success";
                        } else {
                            Log.d("실패", "비밀번호 불일치");
                            result += "fail";

                        }
                        ;

                    } else {
                        Log.d("실패", "없는 사용자 아이디 입니다.");
                        result += "fail";
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
            String result = "success";
            return result;
        }
    }

}