package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Model.RecipeList;
import com.example.sogong.Model.RecipePost;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeFragment extends Fragment {

    String[] pagenum;
    public static int totalpage;
    public static List<RecipePost> recipelist = null;
    public static int responseCode = 0;
    private boolean threadFlag; // 프래그먼트 전환에서 스레드를 잠재울 플래그
    public RecipeAdapter recipeAdapter;
    public RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipeboard, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recipe_recyclerview);

        recipeAdapter = new RecipeAdapter();

        recyclerView.setAdapter(recipeAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        responseCode = 0;

        // UI controller
        RecipeList_UI rlu = new RecipeList_UI();

        // t.start까지 코드는 시작하자마자 불러오는 레시피 리스트
        threadFlag = true;

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    responseCode = -1;
                    recipeAdapter.setRecipeList(recipelist);

                    pagenum = new String[totalpage];
                    for (int i = 1; i <= totalpage; i++) {
                        pagenum[i - 1] = String.valueOf(i);
                    }
                    Spinner spinner1 = view.findViewById(R.id.recipe_page_spinner);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pagenum);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter1);
                    spinner1.setPrompt("이동할 페이지");
                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int position, String data) {
                            Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("recipe_post_id", recipelist.get(position).getPost_id());
                            startActivity(intent);
                        }
                    });

                } else if(responseCode == 500){
                    rlu.startDialog(0,"서버 오류","서버 연결에 실패하였습니다.",new ArrayList<>(Arrays.asList("확인")));
                }else if(responseCode == 502){
                    rlu.startDialog(0,"서버 오류","알 수 없는 오류입니다.",new ArrayList<>(Arrays.asList("확인")));
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

                    if (threadFlag)
                        getActivity().runOnUiThread(runnable);
                    else {
                        i = 30;
                    }
                }
            }
        }
        ControlRecipeList_f crlf = new ControlRecipeList_f();
        crlf.lookupRecipeList(1);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadFlag = false;
    }

    class RecipeList_UI implements Control {
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
