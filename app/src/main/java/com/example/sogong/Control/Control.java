package com.example.sogong.Control;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sogong.R;
import com.example.sogong.View.LoginActivity;

import java.util.List;

public interface Control {
    public abstract void startToast(String message);
    public abstract void startDialog(int type, String title, String message, List<String> btnTxtList);
    //public abstract void changePage(String nowPage, String pageName);
    /*default void changePage(String nowPage, String pageName){
        System.out.println(nowPage+pageName);
    }*/

    public abstract void changePage();
}

/* The origin
import java.util.List;

public class Control {
    public void startToast(String message){
    }
    public void startDialog(int type, String title, String message, List<String> btnTxtList){

    }
    public void changePage(String nowPage, String pageName){
//        Intent intent = new Intent(nowPage, pageName);
//        intent.putExtra("userid", userid_et.getText().toString());
//        startActivity(intent);

    }
}

 */