package com.example.sogong.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.example.sogong.Control.ControlEmailVerification_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailVerificationActivity extends AppCompatActivity {

    EditText email_at, code_at;
    Button sendcode_button, check_button;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그

    public static int responseCode;
    public static int destination = 0; // 액티비티 이동 분기, 회원가입으로 or 닉네임 변경으로
    private boolean isFinish = false; // 스레드의 접근권한 제어

    Custon_ProgressDialog custon_progressDialog;
    public static Activity EmailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);

        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);
        EmailActivity = EmailVerificationActivity.this;
        // flags
        responseCode = 0;
        threadFlag.set(true);

        //UI controller
        EV_UI eu = new EV_UI();

        // 사용할 컴포넌트 초기화
        email_at = findViewById(R.id.email_at);
        code_at = findViewById(R.id.code_at);
        sendcode_button = findViewById(R.id.sendcode_button);
        check_button = findViewById(R.id.check_button);

        // code전송
        sendcode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이메일 입력 양식과 다른 경우와 입력하지 않은 경우
                if (email_at.getText().toString().equals("") || !isValidEmail(email_at.getText().toString())) {
                    responseCode = 0;
                    eu.startToast("잘못된 형식의 입력입니다.");
                } else {
                    custon_progressDialog.show();
                    String email = email_at.getText().toString();
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinish) {
                                if (responseCode == 200) {//이메일로 코드를 성공적으로 보냄
                                    responseCode = -2;
                                    threadFlag.set(false);
                                    eu.startToast("이메일을 전송했습니다.");
                                    isFinish = true;
                                    custon_progressDialog.dismiss();
                                } else if (responseCode == 404) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    eu.startToast("존재하지 않는 이메일입니다.");
                                    custon_progressDialog.dismiss();
                                } else if (responseCode == 501) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    eu.startDialog(0, "서버 오류", "서버 연결에 실패했습니다.", new ArrayList<String>(Arrays.asList("확인")));
                                    custon_progressDialog.dismiss();
                                }
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
                                    Log.d("Verification thread", "working");
                                } else {
                                    i = 30;
                                    Log.d("Verification thread", "down");
                                }
                            }
                        }
                    }

                    if (responseCode == 0) {
                        responseCode = -1;

                        ControlEmailVerification_f cef = new ControlEmailVerification_f();
                        // 비밀변경에는 사용자의 email과 대조하는 과정이 추가로 필요하다.
                        if (destination == 0) {
                            cef.authStart(email);
                        } else if (destination == 1) {
                            eu.startToast("이메일을 전송했습니다.");
                            cef.authStart(email);

                        }

                    }

                    NewRunnable nr = new NewRunnable();
                    Thread t = new Thread(nr);
                    t.start();

                }
            }

        });

        // 인증 완료
        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_at.getText().toString().equals("") | code_at.getText().toString().equals("")) {
                    eu.startDialog(0, "양식 오류", "양식에 맞지 않은 입력입니다.", new ArrayList<>(Arrays.asList("확인")));
                } else {

                    String email = email_at.getText().toString();
                    String code = code_at.getText().toString();

                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //코드 전송을 성공하였을 때 실행
                            if (isFinish) {
                                if (responseCode == 200) {
                                    responseCode = -4;
                                    threadFlag.set(false);
                                    eu.startToast("인증 완료");
                                    if (destination == 0) {
                                        SignupActivity.authEmail = email; // 회원가입 페이지에 email 넘겨줌, Intent 방식으로 할까?
                                        eu.changePage(0);
                                    } else if (destination == 1) {
                                        eu.changePage(1);
                                    }
                                } else if (responseCode == 400) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    eu.startToast("잘못된 이메일 또는 인증코드입니다.");
                                } else if (responseCode == 500) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    eu.startDialog(0, "서버 오류", "이메일 등록에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                                } else if (responseCode == 502) {
                                    responseCode = 0;
                                    threadFlag.set(false);
                                    eu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                                }
                            }
                        }
                    };

                    class NewRunnable implements Runnable {
                        @Override
                        public void run() {
                            int i = 0;
                            while (i < 30) {
                                i++;

                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (threadFlag.get()) {
                                    runOnUiThread(runnable);
                                    Log.d("Verification thread", "working");
                                } else {
                                    i = 30;
                                    Log.d("Verification thread", "down");
                                }
                            }
                        }
                    }


                    ControlEmailVerification_f cef = new ControlEmailVerification_f();
                    threadFlag.set(true);
                    cef.authFinish(email, code);


                    NewRunnable nr = new NewRunnable();
                    Thread t = new Thread(nr);
                    t.start();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadFlag.set(false);
        isFinish = false;
    }

    class EV_UI implements Control {

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
            Custom_Dialog cd = new Custom_Dialog(EmailVerificationActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 회원가입으로
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(EmailVerificationActivity.this, SignupActivity.class);
                startActivity(intent);
            } else if (dest == 1) {
                Intent intent = new Intent(EmailVerificationActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        }

    }

    //이메일 정규식
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }
}