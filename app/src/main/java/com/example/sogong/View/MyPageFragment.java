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
import com.example.sogong.R;

import java.util.List;

public class MyPageFragment extends Fragment {
    Button pwdchange_text, nicknamechange_text, logout_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // 프래그먼트에 버튼 두려면 return에 ViewGroup을 바로 박아버리면 안됨
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_mypage,container,false);
        pwdchange_text = (Button)rootview.findViewById(R.id.pwdchange_text);
        nicknamechange_text = (Button)rootview.findViewById(R.id.nicknamechange_text);
        logout_text = (Button)rootview.findViewById(R.id.logout_text);

        MyPage_UI mu = new MyPage_UI();

        pwdchange_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                mu.startToast("Test1");
            }
        });
        nicknamechange_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                mu.startToast("Test2");
            }
        });
        logout_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                mu.startToast("Test3");
            }
        });

        return rootview;
    }

    class MyPage_UI implements Control{
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)getActivity().findViewById(R.id.toast_layout));
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

        }
    }
}

/*
Intent intent = new Intent(getActivity(), Ready.class);
                intent.putExtra("exerciseCode", 3);

                startActivity(intent);
 */
