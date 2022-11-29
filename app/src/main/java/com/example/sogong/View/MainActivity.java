package com.example.sogong.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Post;
import com.example.sogong.Model.PostObject;
import com.example.sogong.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    ListView listView;
    Button reg_button;
    String userid = "";

    // 리스트뷰에 사용할 제목 배열
    ArrayList<String> titleList = new ArrayList<>();

    // 클릭했을 때 어떤 게시물을 클릭했는지 게시물 번호를 담기 위한 배열
    ArrayList<String> seqList = new ArrayList<>();

    RecyclerView recyclerView;
    HomeFragment homeFragment;
    RecipeFragment recipeFragment;
    PhotoFragment photoFragment;
    MyPageFragment myPageFragment;

    //------------------------------------------------------------
    public static int responseCode;
    public static boolean isLogout = false;
    //------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_postlist);
        setContentView(R.layout.activity_main);

        responseCode = 0;

        homeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
        NavigationBarView navigationBarView = findViewById(R.id.bottomNavi);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        if (homeFragment == null) {
                            homeFragment = new HomeFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.containers, homeFragment).commit();
                        }
                        if (homeFragment != null)
                            getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                        if (recipeFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(recipeFragment).commit();
                        if (photoFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(photoFragment).commit();
                        if (myPageFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(myPageFragment).commit();
                        return true;
                    case R.id.recipe:
                        if (recipeFragment == null) {
                            recipeFragment = new RecipeFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.containers, recipeFragment).commit();
                        }
                        if (homeFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                        if (recipeFragment != null)
                            getSupportFragmentManager().beginTransaction().show(recipeFragment).commit();
                        if (photoFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(photoFragment).commit();
                        if (myPageFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(myPageFragment).commit();
                        return true;
                    case R.id.photo:
                        if (photoFragment == null) {
                            photoFragment = new PhotoFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.containers, photoFragment).commit();
                        }
                        if (homeFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                        if (recipeFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(recipeFragment).commit();
                        if (photoFragment != null)
                            getSupportFragmentManager().beginTransaction().show(photoFragment).commit();
                        if (myPageFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(myPageFragment).commit();
                        return true;
                    case R.id.mypage:
                        if (myPageFragment == null) {
                            myPageFragment = new MyPageFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.containers, myPageFragment).commit();
                        }
                        if (homeFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                        if (recipeFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(recipeFragment).commit();
                        if (photoFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(photoFragment).commit();
                        if (myPageFragment != null)
                            getSupportFragmentManager().beginTransaction().show(myPageFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences auto = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
        if(isLogout) {
            SharedPreferences.Editor autoLoginEdit = auto.edit();
            autoLoginEdit.clear();
            autoLoginEdit.commit();
            isLogout = false;
        }
    }

    // 프래그먼트에 넣어둠
    /*
    class Main_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(MainActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
    */



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
// 이하 onCreate 아래, GetBoard 위에 있던 코드
//        recyclerView = findViewById(R.id.recyclerView);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//// LoginActivity 에서 넘긴 userid 값 받기
//        userid = getIntent().getStringExtra("userid");
//
//// 컴포넌트 초기화
////        listView = findViewById(R.id.listView);
////
////// listView 를 클릭했을 때 이벤트 추가
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////
////// 어떤 값을 선택했는지 토스트를 뿌려줌
////                Toast.makeText(ListActivity.this, adapterView.getItemAtPosition(i)+ " 클릭", Toast.LENGTH_SHORT).show();
////
////// 게시물의 번호와 userid를 가지고 DetailActivity 로 이동
////                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
////                intent.putExtra("board_seq", seqList.get(i));
////                intent.putExtra("userid", userid);
////                startActivity(intent);
////
////            }
////        });
//
//// 버튼 컴포넌트 초기화
//        reg_button = findViewById(R.id.reg_button);
//
//// 버튼 이벤트 추가
//        reg_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//// userid 를 가지고 PostActivity 로 이동
//                Intent intent = new Intent(MainActivity.this, PostActivity.class);
//                intent.putExtra("userid", userid);
//                startActivity(intent);
//            }
//        });
//
//    }


// onResume() 은 해당 액티비티가 화면에 나타날 때 호출됨
//    @Override
//    protected void onResume() {
//        super.onResume();
//        com.example.sogong.View.Legacy.RetrofitService retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
//        Call<List<PostObject>> call = retrofitService.getAllPosts();
//        call.enqueue(new Callback<List<PostObject>>() {
//            @Override
//            public void onResponse(Call<List<PostObject>> call, Response<List<PostObject>> response) {
//                if (response.isSuccessful()) {
//                    List<PostObject> posts = response.body();
//                    for (PostObject postObject : posts) {
//                        Log.d("성공", postObject.getTitle());
//                    }
//                    postAdapter = new PostAdapter(getApplicationContext(), posts);
//                    recyclerView.setAdapter(postAdapter);
//                } else {
//                    Log.d("실패", "김재환 실패");
//                    return;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<PostObject>> call, Throwable t) {
//                Log.d("실패", "김재환 실패");
//            }
//
//        });
//
////// 해당 액티비티가 활성화 될 때, 게시물 리스트를 불러오는 함수를 호출
////        GetBoard getBoard = new GetBoard();
////        getBoard.execute();
//        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClicked(View v, int position) {
//                PostObject item = PostAdapter.getItem(position);
//                Toast.makeText(getApplicationContext(), "postiion" + position + "제목" + item.getTitle(), Toast.LENGTH_LONG).show();
//            }
//        });
//        recyclerView.setAdapter(postAdapter);
//
//    }