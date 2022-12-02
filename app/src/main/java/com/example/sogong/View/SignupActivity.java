package com.example.sogong.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlSignup_f;
import com.example.sogong.Model.User;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignupActivity extends AppCompatActivity {

    EditText userid_et, passwd_et, passwdcheck_et, nickname_et;
    Button join_button, cancel_button;

    public static int responseCode;
    public static String authEmail;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 쓰레드 제어용 플래그
    Custon_ProgressDialog custon_progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        responseCode = 0;
        threadFlag.set(true);

        //UI controler
        SignupActivity_UI su = new SignupActivity_UI();

        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);

        // 사용할 컴포넌트 초기화
        userid_et = findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        passwdcheck_et = findViewById(R.id.passwdcheck_et);
        nickname_et = findViewById(R.id.nickname_et);
        join_button = findViewById(R.id.join_button);
        cancel_button = findViewById(R.id.cancel_button);

        // 취소 버튼
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                su.changePage(0);
            }
        });

        // 회원가입 버튼
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 공백 검출
                if (userid_et.getText().toString().equals("")
                        | passwd_et.getText().toString().equals("")
                        | passwdcheck_et.getText().toString().equals("")
                        | nickname_et.getText().toString().equals("")) {
                    su.startDialog(0, "양식 오류", "양식에 맞지 않은 입력입니다.", new ArrayList<>(Arrays.asList("확인")));
                } else { // 양식 검출
                    if (!passwd_et.getText().toString().matches("(?=.*[0-9]{1,})(?=.*[?!@<>]{1,})(?=.*[a-z]{1,}).{6,}$")) {
                        Log.d("비밀번호", passwd_et.getText().toString());
                        su.startDialog(0, "양식 오류", "비번 양식에 맞지 않은 입력입니다.", new ArrayList<>(Arrays.asList("확인")));
                    } else {
                        custon_progressDialog.show();
                        final Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (responseCode == 200) {
                                    responseCode = -2;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    su.startToast("회원가입 완료");
                                    su.changePage(0);
                                } else if (responseCode == 400) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    su.startToast("중복된 아이디입니다.");
                                } else if (responseCode == 401) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    su.startToast("중복된 닉네임입니다.");
                                } else if (responseCode == 402) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    su.startToast("중복된 아이디와 닉네임입니다.");
                                } else if (responseCode == 500) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    su.startDialog(0, "서버 오류", "정보 등록에 실패했습니다. 재시도 해주십시오.", new ArrayList<String>(Arrays.asList("확인")));
                                } else if (responseCode == 502) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    su.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<String>(Arrays.asList("확인")));
                                }
                            }

                        };

                        class NewRunnable implements Runnable {
                            @Override
                            public void run() {
                                for (int i = 0; i < 30; i++) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (threadFlag.get()) {
                                        runOnUiThread(runnable);
                                        Log.d("Sign up thread", "working");
                                    } else {
                                        i = 30;
                                        Log.d("Sign up thread", "down");
                                    }
                                }
                            }
                        }
                        // 비밀번호와 확인이 일치
                        if (passwd_et.getText().toString().equals(passwdcheck_et.getText().toString())) {
                            // 자동 로그인 default는 false
                            ControlLogin_f clf = new ControlLogin_f();
                            String hashpw = String.valueOf(clf.hashCode(passwd_et.getText().toString()));
                            User temp = new User(nickname_et.getText().toString(), userid_et.getText().toString(), hashpw, authEmail, false);
                            responseCode = -1;
                            ControlSignup_f csf = new ControlSignup_f();
                            csf.signUp(temp);
                            threadFlag.set(true);
                            NewRunnable nr = new NewRunnable();
                            Thread t = new Thread(nr);
                            t.start();
                        } else { // 미일치
                            su.startDialog(0, "양식 오류", "양식에 맞지 않은 입력입니다.", new ArrayList<>(Arrays.asList("확인")));
                            custon_progressDialog.dismiss();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadFlag.set(false);
    }

    class SignupActivity_UI implements Control {

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
            Custom_Dialog cd = new Custom_Dialog(SignupActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }

    }
}