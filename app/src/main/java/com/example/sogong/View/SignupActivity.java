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
import com.example.sogong.R;

import java.util.List;

public class SignupActivity extends AppCompatActivity {

    EditText userid_et, passwd_et, passwdcheck_et, nickname_et;
    Button join_button, cancel_button;

    public static int responseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        // 사용할 컴포넌트 초기화
        userid_et = findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        passwdcheck_et = findViewById(R.id.passwdcheck_et);
        nickname_et = findViewById(R.id.nickname_et);
        join_button = findViewById(R.id.join_button);
        cancel_button = findViewById(R.id.cancel_button);

        // 취소
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class SignupActivity_UI implements Control {

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
            Custom_Dialog cd = new Custom_Dialog(SignupActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(SignupActivity.this, SignupActivity.class);
                startActivity(intent);
                // stack식 액티비티 천환 해결방식을 생각해둘것
            }
        }

    }
}