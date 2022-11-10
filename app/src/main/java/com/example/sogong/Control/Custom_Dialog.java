package com.example.sogong.Control;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sogong.R;


public class Custom_Dialog {

    private Context context;

    public Custom_Dialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(String title, String msg, int type) {

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
        final TextView errortitle= (TextView) dlg.findViewById(R.id.errortitle);
        final TextView errormsg= (TextView) dlg.findViewById(R.id.errormessage);
        final Button Button1 = (Button) dlg.findViewById(R.id.okButton);
        final Button Button2 = (Button) dlg.findViewById(R.id.cancelButton);

        errortitle.setText(title);
        errormsg.setText(msg);
        if(type==0){
            Button1.setVisibility(View.INVISIBLE);
            Button2.setText("확인");
            Button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "확인했습니다.", Toast.LENGTH_SHORT).show();

                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            });
        }else if (type==1){
            Button1.setText("취소");
            Button2.setText("삭제");
            Button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                }
            });
            Button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "삭제했습니다.", Toast.LENGTH_SHORT).show();

                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            });
        }


    }
}