package com.example.sogong.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Report;
import com.example.sogong.R;

public class ReportActivity extends AppCompatActivity {
    EditText reportReason;
    Button report_btn;
    Button cancel_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        int comment_id = getIntent().getExtras().getInt("comment_id");
        int recipe_id = getIntent().getExtras().getInt("recipe_id");
        int posttype = getIntent().getExtras().getInt("report_post_type");
        reportReason = findViewById(R.id.report_edit);
        report_btn = findViewById(R.id.report_button);
        cancel_btn = findViewById(R.id.cancel_button);

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(posttype){
                    //댓글 신고
                    case -1:
                        Report commentReport = new Report(ControlLogin_f.userinfo.getNickname(),reportReason.getText().toString(),-1,comment_id);
                        //cref.reportComment(reportInfo);
                        Log.d("report","댓글 신고하러 왔어요");
                        break;
                    case 1:
                        Report recipeReport = new Report(ControlLogin_f.userinfo.getNickname(),reportReason.getText().toString(),1,recipe_id);
                        Log.d("report","레시피 신고하러 왔어요");
                        break;
                    case 2:
                        Log.d("report","사진 신고하러 왔어요");
                        break;
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
