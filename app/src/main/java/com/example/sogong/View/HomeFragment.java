package com.example.sogong.View;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogout_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.R;

import java.util.List;

public class HomeFragment extends Fragment {
    public static String str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        Home_UI hu = new Home_UI();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                hu.startToast(str);
                if (MainActivity.responseCode == 200) {
                    MainActivity.responseCode = -1;
                    hu.startToast(str);
                } else {
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

        ControlRecipeList_f crlf = new ControlRecipeList_f();
        crlf.lookupRecipeList(19);


        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();

        return rootview;
    }

    class Home_UI implements Control {
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
        }

        @Override
        public void changePage(int dest) {

        }
    }
}

// json object
// java json parser