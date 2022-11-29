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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}