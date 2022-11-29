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
    // 같은 액티비티에서 여러 액티비티로 갈 경우를 대비해서 목적지를 int로 가름, 하나인 경우 0
    public abstract void changePage(int dest);
}
