package com.example.sogong.View;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sogong.R;

import java.util.List;


public class Custom_Dialog {

    private Context context;

    public static int state = -1;

    public Custom_Dialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(String title, String msg, int type, List<String> btnTxtList) {
        state = -1;
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView errortitle = (TextView) dlg.findViewById(R.id.errortitle);
        final TextView errormsg = (TextView) dlg.findViewById(R.id.errormessage);
        final Button Button1 = (Button) dlg.findViewById(R.id.okButton);
        final Button Button2 = (Button) dlg.findViewById(R.id.cancelButton);

        errortitle.setText(title);
        errormsg.setText(msg);
        if (type == 0) {
            Button1.setVisibility(View.INVISIBLE);
            Button2.setText(btnTxtList.get(0));
            Button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                }
            });
        } else if (type == 1) {
            Button1.setText(btnTxtList.get(0));
            Button2.setText(btnTxtList.get(1));
            Button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    state = 0;
                    dlg.dismiss();
                }
            });
            Button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    state = 1;
                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            });
        }
    }
}