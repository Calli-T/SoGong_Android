package com.example.sogong.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.User;
import com.example.sogong.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class LoginActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();
    ControlLogin_f clf = new ControlLogin_f();
    // 사용할 컴포넌트 선언
    EditText userid_et, passwd_et;
    Button login_button, join_button;
    CheckBox checkbox;
    TextInputLayout textInputLayout2;
    Custon_ProgressDialog custon_progressDialog;

    public static int responseCode;
    private AtomicBoolean threadFlag = new AtomicBoolean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);

        //UI controller
        Login_UI lu = new Login_UI();

        // 사용할 컴포넌트 초기화
        userid_et = findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        login_button = findViewById(R.id.login_button);
        join_button = findViewById(R.id.join_button);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        checkbox = findViewById(R.id.checkBox);

        // 로그인 버튼 이벤트 추가
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userid_et.getText().toString().equals("") | passwd_et.getText().toString().equals("")) {
                    lu.startToast("아이디와 비밀번호를 모두 입력해주세요");
                } else {
                    lu.startToast("로그인 중");
                    custon_progressDialog.show();
                    String id = userid_et.getText().toString();
                    String pw = passwd_et.getText().toString();
                    String hashpw = String.valueOf(clf.hashCode(pw));
                    boolean auto_login = checkbox.isChecked();

                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (responseCode == 200) {
                                responseCode = 0;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                lu.startToast("로그인 성공");
                                //자동로그인을 체크한 경우
                                if (auto_login) {
                                    SharedPreferences auto = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLoginEdit = auto.edit();
                                    autoLoginEdit.putString("uid", id);
                                    autoLoginEdit.putString("password", pw);
                                    autoLoginEdit.putString("nickname", ControlLogin_f.userinfo.getNickname());
                                    autoLoginEdit.putString("email", ControlLogin_f.userinfo.getEmail());
                                    autoLoginEdit.putBoolean("auto_login", true);
                                    autoLoginEdit.apply();
                                }
                                lu.changePage(0);
                            } else if (responseCode == 400) { // custom dialog랑 toast 및 control 구현해둘것
                                responseCode = 0;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                lu.startToast("아이디 또는 비밀번호가 잘못되었습니다.");
                            } else if (responseCode == 500) {
                                responseCode = 0;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                lu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<String>(Arrays.asList("확인")));
                            }
                        }
                    };

                    class NewRunnable implements Runnable {
                        @Override
                        public void run() {
                            int i = 30;
                            while (i > 0) {
                                i--;
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } if (threadFlag.get())
                                    runOnUiThread(runnable);
                                else {
                                    i = 30;
                                }
                            }
                        }
                    }

                    if (responseCode != -1) {
                        ControlLogin_f clf = new ControlLogin_f();
                        clf.login(id, hashpw, auto_login);
                        responseCode = -1;
                    }

                    NewRunnable nr = new NewRunnable();
                    threadFlag.set(true);
                    Thread t = new Thread(nr);
                    t.start();

                }
            }
        });

        //회원가입 화면으로 이동
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lu.changePage(1);
            }
        });
    }

    class Login_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 50); //TODO 메시지가 표시되는 위치지정 (하단 표시)
            toast.setDuration(Toast.LENGTH_SHORT); //메시지 표시 시간
            toast.setView(layout);
            toast.show();
        }

        @Override
        public void startDialog(int type, String title, String message, List<String> btnTxtList) {
            Custom_Dialog cd = new Custom_Dialog(LoginActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (dest == 1) {
                // 회원가입에서 요청한 이메일 인증
                EmailVerificationActivity.destination = 0;
                Intent intent = new Intent(LoginActivity.this, EmailVerificationActivity.class);
                startActivity(intent);
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sp = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
        boolean isAuto = sp.getBoolean("auto_login", false);
        Log.d("test auto", isAuto + "");

        if (isAuto && !MainActivity.isLogout) {
            String nickname = sp.getString("nickname", "default");
            String email = sp.getString("email", "default");
            String uid = sp.getString("uid", "default");
            String password = sp.getString("password", "default");

            ControlLogin_f.userinfo = new User(nickname, uid, password, email, true);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            Log.d("test auto", ControlLogin_f.userinfo.toString());
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}