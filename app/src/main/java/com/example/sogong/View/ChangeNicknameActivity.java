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
import com.example.sogong.Control.ControlEdittingInfo_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChangeNicknameActivity extends AppCompatActivity {

    EditText nickname_et;
    Button change_button, cancel_button;
    Custon_ProgressDialog custon_progressDialog;

    public static int responseCode;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 스레드 제어용 플래그

    //UI controller
    CN_UI cu = new CN_UI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changenickname);

        // 사용할 컴포넌트 초기화
        nickname_et = findViewById(R.id.nickname_et);
        change_button = findViewById(R.id.change_button);
        cancel_button = findViewById(R.id.cancel_button);

        responseCode = 0;
        custon_progressDialog = new Custon_ProgressDialog(this);
        custon_progressDialog.setCanceledOnTouchOutside(false);

        //닉네임 변경 버튼 클릭 시
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //닉네임을 비우고 클릭한 경우
                if (nickname_et.getText().toString().equals("")) {
                    cu.startDialog(0, "입력", "새로운 닉네임을 입력해주세요.", new ArrayList<>(Arrays.asList("확인")));
                } else {
                    String nickname = nickname_et.getText().toString();
                    //입력한 닉네임으로 api 호출
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (responseCode == 200) {
                                responseCode = -2;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                ControlLogin_f.userinfo.setNickname(nickname);
                                cu.startToast("닉네임이 변경되었습니다.");
                                finish();
                            } else if (responseCode == 400) {
                                responseCode = 0;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                cu.startDialog(0, "닉네임 중복", "중복된 닉네임입니다.", new ArrayList<>(Arrays.asList("확인")));
                            } else if (responseCode == 500) {
                                responseCode = 0;
                                threadFlag.set(false);
                                custon_progressDialog.dismiss();
                                cu.startDialog(0, "서버 오류", "닉네임 변경에 실패했습니다. 재시도 해주십시오.", new ArrayList<>(Arrays.asList("확인")));
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
                            for (int i = 0; i < 30; i++) {
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
                    custon_progressDialog.show();
                    threadFlag.set(true);
                    ControlEdittingInfo_f cef = new ControlEdittingInfo_f();
                    cef.editNickname(nickname);//닉네임 변경 로직

                    NewRunnable nr = new NewRunnable();
                    Thread t = new Thread(nr);
                    t.start();
                }
            }

        });

        //취소 버튼
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    class CN_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(ChangeNicknameActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {

        }
    }
}