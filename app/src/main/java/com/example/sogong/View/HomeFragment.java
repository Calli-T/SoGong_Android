package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogout_f;
import com.example.sogong.Control.ControlPhotoList_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {
    public static int totalpage;
    public static List<RecipePost_f> recipelist;
    public static int responseCode = 0;
    public static boolean isInHome;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그

    Custon_ProgressDialog custon_progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecipeAdapter recipeAdapter;
    private RecyclerView recipeRecyclerView;
    private GridView gridview = null;
    private PhotoAdapter photoAdapter = null;

    private ViewGroup rootview;

    private ControlRecipeList_f crlf = new ControlRecipeList_f();
    private ControlPhotoList_f cplf = new ControlPhotoList_f();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("homefragment", "시작");

        isInHome = true;

        //로딩
        custon_progressDialog = new Custon_ProgressDialog(requireActivity());
        custon_progressDialog.setCanceledOnTouchOutside(false);
        custon_progressDialog.show();
        
        //swipe 레이아웃
        swipeRefreshLayout = rootview.findViewById(R.id.home_swipeLayout);
        //swipeRefreshLayout.setOnRefreshListener(this::onRefresh); // 갱신 함수 제작할 것

        //레시피 Recycler 선언
        recipeRecyclerView = (RecyclerView) rootview.findViewById(R.id.home_recipe_recyclerview);
        recipeAdapter = new RecipeAdapter();
        recipeRecyclerView.setAdapter(recipeAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeRecyclerView.setLayoutManager(layoutManager);
        
        //사진 Grid 선언
        gridview = (GridView) rootview.findViewById(R.id.home_photo_grid);
        photoAdapter = new PhotoAdapter();
        gridview.setAdapter(photoAdapter);

        //맨 앞으로
        gridview.bringToFront();

        Log.d("homefragment", "재시작");
        // UI controller
        Home_UI hu = new Home_UI();
        custon_progressDialog.show();

        threadFlag.set(true);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeFragment.responseCode == 200 && PhotoFragment.responseCode == 200) {
                    RecipeFragment.responseCode = 0;
                    PhotoFragment.responseCode = 0;
                    threadFlag.set(false);
                    Log.d("homefragment", "레시피리스트와 포토리스트 가져옴");

                    // 레시피 리사이클러뷰 생성
                    recipeAdapter.setRecipeList(RecipeFragment.recipelist);

                    // 레시피 리사이클러뷰 클릭 이벤트
                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                        // 원소 번호와 정보를 긁어다 intent에 넣어 lookup으로 이동?
                        @Override
                        public void onItemClicked(int position, String data) {
                            Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("recipe_post", recipelist.get(position));
                            startActivity(intent);
                        }
                    });

                    // 사진 그리드뷰의 생성 및 클릭 이벤트
                    photoAdapter.setPhotoList(PhotoFragment.photoList);
                    gridview.setAdapter(photoAdapter);
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("사진", PhotoFragment.photoList.get(position).toString());
                            Intent intent = new Intent(getActivity(), PhotoLookupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("photo_post", PhotoFragment.photoList.get(position));
                            //startActivity(intent);
                        }
                    });

                    custon_progressDialog.dismiss();//로딩창 종료
                } else if (RecipeFragment.responseCode == 500 || PhotoFragment.responseCode == 500) {
                    RecipeFragment.responseCode = 0;
                    PhotoFragment.responseCode = 0;
                    threadFlag.set(false);
                    custon_progressDialog.dismiss();//로딩창 종료
                    hu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                } else if (RecipeFragment.responseCode == 502 || PhotoFragment.responseCode == 502) {
                    RecipeFragment.responseCode = 0;
                    PhotoFragment.responseCode = 0;
                    threadFlag.set(false);
                    custon_progressDialog.dismiss();//로딩창 종료
                    hu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                }
            }
        };

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < 30; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (threadFlag.get()) {
                        getActivity().runOnUiThread(runnable);

                    } else {
                        i = 30;
                        //Thread.currentThread().interrupt();
                    }
                }
            }
        }


        crlf.sortRecipeList("최근 순", 1);
        cplf.sortPhotoList("최근 순", 1);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();


        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadFlag.set(false);
        isInHome = false;
    }

    // swipe에 사용할 함수
    @Override
    public void onRefresh() {
        //새로 고침 코드
        //updateLayoutView();
        //새로 고침 완료
        //swipeRefreshLayout.setRefreshing(false);
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
/*
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
        crlf.lookupRecipeList(1);


        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// json object
// java json parser

/*
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
        crlf.lookupRecipeList(1);


        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */