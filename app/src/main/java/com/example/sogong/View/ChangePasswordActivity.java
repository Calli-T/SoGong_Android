package com.example.sogong.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlEdittingInfo_f;
import com.example.sogong.Control.ControlEmailVerification_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText passwd_et, passwdcheck_et;
    Button change_button, cancel_button;
    Custon_ProgressDialog custon_progressDialog;

    public static int responseCode = 0;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    
    // UI controller
    CP_UI cu = new CP_UI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        Activity emailActivity = (EmailVerificationActivity) EmailVerificationActivity.EmailActivity;
        // 사용할 컴포넌트 초기화
        passwd_et = findViewById(R.id.passwd_et);
        passwdcheck_et = findViewById(R.id.passwdcheck_et);
        change_button = findViewById(R.id.change_button);
        cancel_button = findViewById(R.id.cancel_button);

        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);

        // 비밀번호 변경 버튼 클릭 시
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwd = passwd_et.getText().toString();
                String passwdcheck = passwdcheck_et.getText().toString();
                //양식에 맞지 않게 입력한 경우
                if (passwd.equals("") | passwdcheck.equals("")) {
                    cu.startDialog(0, "양식 오류", "양식에 맞지않은 비밀번호입니다", new ArrayList<>(Arrays.asList("확인")));
                } else if (!passwd.equals(passwdcheck)) {
                    //비밀번호가 다른 경우
                    cu.startDialog(0, "양식 오류", "양식에 맞지않은 비밀번호입니다", new ArrayList<>(Arrays.asList("확인")));
                } else {
                    if (!passwd.matches("(?=.*[0-9]{1,})(?=.*[?!@<>]{1,})(?=.*[a-z]{1,}).{6,}$")) {
                        //비밀번호 정규식과 맞지 않은 경우
                        cu.startDialog(0, "양식 오류", "양식에 맞지않은 비밀번호입니다", new ArrayList<>(Arrays.asList("확인")));
                    } else {
                        final Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (responseCode == 200) {
                                    responseCode = -2;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    cu.startToast("비밀번호가 변경되었습니다.");
                                    emailActivity.finish();
                                    finish();
                                } else if (responseCode == 500) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    cu.startToast("비밀번호 변경 요청 실패했습니다.");
                                } else if (responseCode == 502) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    custon_progressDialog.dismiss();
                                    cu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                                    }
                                    if (threadFlag.get())
                                        runOnUiThread(runnable);
                                    else {
                                        i = 30;
                                    }
                                }
                            }
                        }


                        responseCode = -1;

                        ControlEdittingInfo_f cef = new ControlEdittingInfo_f();
                        ControlLogin_f clf = new ControlLogin_f();
                        custon_progressDialog.show();
                        threadFlag.set(true);
                        cef.editPassword(String.valueOf(clf.hashCode(passwd)));//비밀번호 변경 api 호출
                        NewRunnable nr = new NewRunnable();
                        Thread t = new Thread(nr);
                        t.start();
                    }
                }
            }
        });

        //취소 버튼 클릭 시
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailActivity.finish();
                finish();
            }
        });
    }

    class CP_UI implements Control {

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
            Custom_Dialog cd = new Custom_Dialog(ChangePasswordActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

    }
}