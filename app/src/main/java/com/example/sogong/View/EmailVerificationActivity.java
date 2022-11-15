package com.example.sogong.View;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverification);

        responseCode = 0;

        //UI controller
        EV_UI_Control euc = new EV_UI_Control();

        // 사용할 컴포넌트 초기화
        email_at = findViewById(R.id.email_at);
        code_at = findViewById(R.id.code_at);
        sendcode_button = findViewById(R.id.sendcode_button);
        check_button = findViewById(R.id.check_button);

        // code버튼 추가
        sendcode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_at.getText().toString();

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (responseCode == 200) {
                            responseCode = -1;
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

                        if(responseCode != 0) euc.startToast("코드 전송");
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
                    ControlEmailVerification_f cef = new ControlEmailVerification_f();
                    cef.authStart(email);
                    responseCode = -1;
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();

            }

        });

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_at.getText().toString();
                String code = code_at.getText().toString();

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(responseCode != 0) euc.startToast(""+responseCode);

                        if (responseCode == 200) {
                            euc.startToast("쩨발, 테스트를 확인해줘");
                            responseCode = 0;
                            //luc.changePage(0);
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

                if (responseCode == -1) {
                    ControlEmailVerification_f cef = new ControlEmailVerification_f();
                    cef.authFinish(email, code);
                    //responseCode = -2;
                    //responseCode = 0;
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();

            }
        });

    }

    class EV_UI_Control implements Control {

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

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            /*if (dest == 0) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }*/
        }

    }
}