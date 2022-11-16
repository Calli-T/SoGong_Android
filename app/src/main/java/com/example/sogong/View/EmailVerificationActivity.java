package com.example.sogong.View;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.sogong.Control.ControlEmailVerification_f;
import com.example.sogong.R;

import java.util.List;

public class EmailVerificationActivity extends AppCompatActivity {

    EditText email_at, code_at;
    Button sendcode_button, check_button;

    public static int responseCode;
    public static int destination = 0; // 액티비티 이동 분기, 회원가입으로 or 닉네임 변경으로
    public boolean isFinish = false; // 스레드의 접근권한 제어

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);

        responseCode = 0;

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
                String email = email_at.getText().toString();

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinish) {
                            if (responseCode == 200) {
                                responseCode = -2;
                                eu.startToast("코드 전송");
                                isFinish = true;
                            } else if (responseCode == 400) {
                                responseCode = 0;
                            } else if (responseCode == 404) {
                                responseCode = 0;
                            } else if (responseCode == 500) {
                                responseCode = 0;
                            } else if (responseCode == 501) {
                                responseCode = 0;
                            } else if (responseCode == 502) {
                                responseCode = 0;
                            }
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

                            runOnUiThread(runnable);
                        }
                    }
                }

                if (responseCode == 0) {
                    responseCode = -1;

                    ControlEmailVerification_f cef = new ControlEmailVerification_f();
                    cef.authStart(email);
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();

            }

        });

        // 인증 완료
        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_at.getText().toString();
                String code = code_at.getText().toString();

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (isFinish) {
                            if (responseCode == 200) {
                                responseCode = -4;
                                eu.startToast("인증 완료");
                                if (destination == 0)
                                    eu.changePage(0);
                            } else if (responseCode == 400) {
                                responseCode = 0;
                            } else if (responseCode == 404) {
                                responseCode = 0;
                            } else if (responseCode == 500) {
                                responseCode = 0;
                            } else if (responseCode == 501) {
                                responseCode = 0;
                            } else if (responseCode == 502) {
                                responseCode = 0;
                            }
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

                            runOnUiThread(runnable);
                        }
                    }
                }

                if (responseCode == -2) {
                    ControlEmailVerification_f cef = new ControlEmailVerification_f();
                    cef.authFinish(email, code);
                    responseCode = -3;
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            }
        });

    }

    class EV_UI implements Control {

        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getApplicationContext());
            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); //TODO 메시지가 표시되는 위치지정 (가운데 표시)
            //toast.setGravity(Gravity.TOP, 0, 0); //TODO 메시지가 표시되는 위치지정 (상단 표시)
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
                // stack식 액티비티 천환 해결방식을 생각해둘것
            }
        }

    }
}

// responseCode가 -1이면 authStart전송, -2이면 authStart정상처리, -3이면 authFinish전송, -4면 authFinish정상처리