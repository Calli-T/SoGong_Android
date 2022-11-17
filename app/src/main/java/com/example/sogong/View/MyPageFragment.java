package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlEdittingInfo_f;
import com.example.sogong.Control.ControlLogout_f;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPageFragment extends Fragment {
    Button pwdchange_text, nicknamechange_text, logout_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 프래그먼트에 버튼 두려면 return에 ViewGroup을 바로 박아버리면 안됨
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);
        pwdchange_text = (Button) rootview.findViewById(R.id.pwdchange_text);
        nicknamechange_text = (Button) rootview.findViewById(R.id.nicknamechange_text);
        logout_text = (Button) rootview.findViewById(R.id.logout_text);

        MyPage_UI mu = new MyPage_UI();

        pwdchange_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mu.startToast("Test1");
                EmailVerificationActivity.destination = 1; // 이메일 인증 Activity의 분기 결정 Flag

                Intent intent = new Intent(getActivity(), EmailVerificationActivity.class);
                startActivity(intent);
            }
        });
        nicknamechange_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeNicknameActivity.class);
                startActivity(intent);
            }
        });
        // 프래그먼트에서 구현한 로그아웃 및 api call 로직
        logout_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.responseCode == 200) {
                            MainActivity.responseCode = -1;
                            mu.startToast("로그아웃");
                            mu.changePage(0);
//                            getActivity().finish();

                        } else if (MainActivity.responseCode == 500) {
                            MainActivity.responseCode = 0;
                            mu.startToast("자동 로그인 해제를 실패했습니다.");
                        } else if (MainActivity.responseCode == 502) {
                            MainActivity.responseCode = 0;
                            mu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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

                            getActivity().runOnUiThread(runnable);
                        }
                    }
                }

                if (MainActivity.responseCode == 0) {
                    MainActivity.responseCode = -1;

                    ControlLogout_f clf = new ControlLogout_f();
                    clf.logout();
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            }
        });
        return rootview;
    }

    class MyPage_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) getActivity().findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getActivity());
            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); //TODO 메시지가 표시되는 위치지정 (가운데 표시)
            //toast.setGravity(Gravity.TOP, 0, 0); //TODO 메시지가 표시되는 위치지정 (상단 표시)
            toast.setGravity(Gravity.BOTTOM, 0, 50); //TODO 메시지가 표시되는 위치지정 (하단 표시)
            toast.setDuration(Toast.LENGTH_SHORT); //메시지 표시 시간
            toast.setView(layout);
            toast.show();
        }

        @Override
        public void startDialog(int type, String title, String message, List<String> btnTxtList) {
            Custom_Dialog cd = new Custom_Dialog(getActivity());
            cd.callFunction(title, message, type, btnTxtList);
        }

        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }

        }
    }
}

/*
Intent intent = new Intent(getActivity(), Ready.class);
                intent.putExtra("exerciseCode", 3);

                startActivity(intent);
 */
