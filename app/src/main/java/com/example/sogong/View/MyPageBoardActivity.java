package com.example.sogong.View;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlComment_f;
import com.example.sogong.Control.ControlIngredients_f;
import com.example.sogong.Control.ControlLike_f;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlMailList_f;
import com.example.sogong.Control.ControlMail_f;
import com.example.sogong.Control.ControlMyPhoto_f;
import com.example.sogong.Control.ControlMyRecipe_f;
import com.example.sogong.Control.ControlPhotoList_f;
import com.example.sogong.Control.ControlPhoto_f;
import com.example.sogong.Control.ControlPost_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Control.ControlReport_f;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyPageBoardActivity extends AppCompatActivity {

    int type;
    String[] pagenum;
    public static int totalpage;
    public static List<RecipePost_f> recipelist;
    public static List<PhotoPost> photolist;

    public static int responseCode = 0;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    private AtomicBoolean sortthreadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그

    public RecipeAdapter recipeAdapter;
    public RecyclerView recipeRecyclerView;
    Custon_ProgressDialog custon_progressDialog;
    ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
    ControlMyPhoto_f cmpf = new ControlMyPhoto_f();

    Spinner sortspinner;
    ImageButton searchButton;

    Boolean firstpage;
    String currentSort;

    private GridView gridview = null;
    private PhotoAdapter photoAdapter = null;

    MyPageBoard_UI mpbu = new MyPageBoard_UI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("post_type", -1);
        Log.d("type", "type = " + type);

        if (type == 0) {//자기가 작성한 레시피 게시판
            Log.d("mypageRecipe", "시작");
            setContentView(R.layout.activity_myboard);
            searchButton = findViewById(R.id.search_button);//검색 버튼
            searchButton.setVisibility(View.VISIBLE);
            //레시피 게시글 리스트 리사이클러뷰
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);

            responseCode = 0;

            //로딩창 구현
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            //현재 정렬 기준
            currentSort = "";
            // 추가) 레시피 게시판 조회 호출 코드
            threadFlag.set(true);
            firstpage = true;
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;

                        if (recipelist.size() == 0) {
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setVisibility(View.VISIBLE);
                        }
                        recipeAdapter.setRecipeList(recipelist);
//                        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                Log.d("recipefragment", "sort spinner " + sortspinner.getSelectedItem().toString() + " 클릭");
//                                String sort_str = sortspinner.getSelectedItem().toString();
//                                // #29 레시피 게시글 정렬 별표 주석으로 바꿀것
//                                //crlf.sortRecipeList(sort_str, 1);
//                                SortInfo sortInfo = new SortInfo(ControlLogin_f.userinfo.getNickname(),sortspinner.getSelectedItem().toString());
//                                cmrf.sortMyRecipeList(sortInfo);
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });
                        //자기가 작성한 레시피 게시글에만 정렬 기준이 있으므로 visible로 바꿈
                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setVisibility(View.VISIBLE);
                        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("recipefragment", "sort spinner " + sortspinner.getSelectedItem().toString() + " 클릭");
                                String sort_str = sortspinner.getSelectedItem().toString();
                                if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                    currentSort = sort_str;
                                    SortInfo sortInfo = new SortInfo(ControlLogin_f.userinfo.getNickname(), sort_str);
                                    cmrf.sortMyRecipeList(sortInfo);
                                    custon_progressDialog.show();
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (responseCode == 200) {
                                                responseCode = -1;
                                                sortthreadFlag.set(false);
                                                //업데이트된 레시피 리스트로 전환
                                                recipeAdapter.setRecipeList(recipelist);
                                                //레시피 리사이클러뷰 클릭 이벤트
                                                recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClicked(int position, String data) {
                                                        Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        intent.putExtra("recipe_post", recipelist.get(position));
                                                        startActivity(intent);
                                                    }
                                                });
                                                custon_progressDialog.dismiss();
                                            } else if (responseCode == 404) {
                                                responseCode = -1;
                                                sortthreadFlag.set(false);
                                                custon_progressDialog.dismiss();
                                                mpbu.startDialog(0, "요청 실패", "정렬 정보 요청을 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                                            } else if (responseCode == 500) {
                                                responseCode = -1;
                                                sortthreadFlag.set(false);
                                                custon_progressDialog.dismiss();
                                                mpbu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                                            }

                                        }
                                    };
                                    class NewRunnable implements Runnable {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < 30; i++) {
                                                try {
                                                    sleep(1000);
                                                    Log.d("thread2", "thread2");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                if (sortthreadFlag.get()) {
                                                    runOnUiThread(runnable);
                                                    Log.d("thread2", "thread3");
                                                } else {
                                                    i = 30;
                                                    Log.d("thread2", "thread4");
                                                }
                                            }
                                            Log.d("thread2", "thread1");
                                        }
                                    }
                                    NewRunnable nr = new NewRunnable();
                                    Thread t = new Thread(nr);
                                    sortthreadFlag.set(true);
                                    //정렬 기준을 선택하였을 때 작동하는 쓰레드
                                    t.start();
                                    //업데이트된 레시피 리스트로 전환
                                    recipeAdapter.setRecipeList(recipelist);
                                    //레시피 리사이클러뷰 클릭 이벤트
                                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClicked(int position, String data) {
                                            Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            intent.putExtra("recipe_post", recipelist.get(position));
                                            startActivity(intent);
                                        }
                                    });
                                    Log.d("recipefragment", "page spinner " + position + " 클릭");
                                }
                                firstpage = false;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        //레시피 리사이클러뷰 클릭 이벤트
                        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, String data) {
                                Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("recipe_post", recipelist.get(position));
                                startActivity(intent);
                            }
                        });
                        //검색화면으로 이동
                        searchButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MyPageBoardActivity.this, RecipeSearchActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("isMyPage", true);
                                startActivity(intent);
                                //recipesearchActivity로 마이페이지에서 왔다는 정보를 보내줌
                            }
                        });
                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 400) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        mpbu.startDialog(0, "레시피 요청 실패", "레시피 게시글 요청에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        mpbu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                    }
                }
            };

            class NewRunnable implements Runnable {
                @Override
                public void run() {
                    for (int i = 0; i < 30; i++) {
                        try {
                            Thread.sleep(1000);
                            Log.d("thread1", "thread2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("thread1", "thread5");
                        }

                        if (threadFlag.get()) {
                            runOnUiThread(runnable);
                            Log.d("thread1", "thread3");

                        } else {
                            i = 30;
                            Log.d("thread1", "thread4");
                            Thread.currentThread().interrupt();
                        }
                    }
                    Log.d("thread1", "thread1");
                }
            }
            ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
            cmrf.lookupMyRecipeList(ControlLogin_f.userinfo.getNickname());

            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            //자기가 작성한 레시피를 요청받는 쓰레드
            t.start();
        } else if (type == 1) {
            //사진게시판
            setContentView(R.layout.activity_myphotoboard);
            gridview = (GridView) findViewById(R.id.photo_grid);
            photoAdapter = new PhotoAdapter();
            gridview.setAdapter(photoAdapter);

            responseCode = 0;
            //로딩창 구현
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            threadFlag.set(true);
            firstpage = true;

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;
                        if (photolist.size() == 0) {
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setVisibility(View.VISIBLE);
                        }
                        photoAdapter.setPhotoList(photolist);
                        //전달받은 사진리스트 설정
                        gridview.setAdapter(photoAdapter);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("사진", "position = " + position + " id = " + id);
                                Intent intent = new Intent(MyPageBoardActivity.this, PhotoLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("photo_post", photolist.get(position));
                                startActivity(intent);
                            }
                        });
                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 404) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        mpbu.startDialog(0, "사진 요청 실패", "사진 게시글 요청에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        mpbu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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

                        if (threadFlag.get())
                            runOnUiThread(runnable);
                        else {
                            i = 30;
                        }
                    }
                }
            }

            cmpf.lookupMyPhotoList(ControlLogin_f.userinfo.getNickname());

            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            t.start();


        } else if (type == 2) {
            //좋아요한 리스트
            Log.d("mypageRecipe", "시작");
            setContentView(R.layout.activity_myboard);
            searchButton = findViewById(R.id.search_button);//검색 버튼
            searchButton.setVisibility(View.INVISIBLE);
            //레시피 게시글 리스트 리사이클러뷰
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);

            responseCode = 0;

            //로딩창 구현
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            //검색화면으로 이동
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyPageBoardActivity.this, RecipeSearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            // 추가) 레시피 게시판 조회 호출 코드
            threadFlag.set(true);
            firstpage = true;
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;
                        if (recipelist.size() == 0) {//좋아요한 게시글이 없는 경우
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setVisibility(View.VISIBLE);
                        }
                        recipeAdapter.setRecipeList(recipelist);
                        pagenum = new String[totalpage];
                        for (int i = 1; i <= totalpage; i++) {
                            pagenum[i - 1] = String.valueOf(i);
                        }

                        //레시피 리사이클러뷰 클릭 이벤트
                        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, String data) {
                                Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("recipe_post", recipelist.get(position));
                                startActivity(intent);
                            }
                        });
                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setVisibility(View.INVISIBLE);
                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        mpbu.startDialog(0, "정보 요청 실패", "정보 요청에 실패했습니다.", new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode == 502) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        mpbu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                    }
                }
            };

            class NewRunnable implements Runnable {
                @Override
                public void run() {
                    for (int i = 0; i < 30; i++) {
                        try {
                            Thread.sleep(1000);
                            Log.d("thread1", "thread2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("thread1", "thread5");
                        }

                        if (threadFlag.get()) {
                            runOnUiThread(runnable);
                            Log.d("thread1", "thread3");

                        } else {
                            i = 30;
                            Log.d("thread1", "thread4");
                            Thread.currentThread().interrupt();
                        }
                    }
                    Log.d("thread1", "thread1");
                }
            }
            ControlPost_f cpf = new ControlPost_f();
            cpf.lookupMyLikeList(ControlLogin_f.userinfo.getNickname(), 1);
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            //좋아요 누른 리스트를 확인하는 쓰레드
            t.start();
        } else if (type == 3) {
            //댓글을 작성한 리스트
            Log.d("mypageRecipe", "시작");
            setContentView(R.layout.activity_myboard);
            searchButton = findViewById(R.id.search_button);//검색 버튼
            searchButton.setVisibility(View.INVISIBLE);
            //레시피 게시글 리스트 리사이클러뷰
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);

            responseCode = 0;

            //로딩창 구현
            custon_progressDialog = new Custon_ProgressDialog(this);
            custon_progressDialog.setCanceledOnTouchOutside(false);
            custon_progressDialog.show();

            //검색화면으로 이동
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyPageBoardActivity.this, RecipeSearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            // 추가) 레시피 게시판 조회 호출 코드
            threadFlag.set(true);
            firstpage = true;
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;
                        if (recipelist.size() == 0) {//댓글 작성한 게시글이 없는 경우
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setVisibility(View.VISIBLE);
                        }
                        recipeAdapter.setRecipeList(recipelist);
                        pagenum = new String[totalpage];
                        for (int i = 1; i <= totalpage; i++) {
                            pagenum[i - 1] = String.valueOf(i);
                        }
                        //레시피 리사이클러뷰 클릭 이벤트
                        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, String data) {
                                Intent intent = new Intent(MyPageBoardActivity.this, RecipeLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("recipe_post", recipelist.get(position));
                                startActivity(intent);
                            }
                        });
                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setVisibility(View.INVISIBLE);
                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        mpbu.startDialog(0, "요청 실패", "정보 요청에 실패함.", new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode == 502) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        mpbu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
                    }
                }
            };

            class NewRunnable implements Runnable {
                @Override
                public void run() {
                    for (int i = 0; i < 30; i++) {
                        try {
                            Thread.sleep(1000);
                            Log.d("thread1", "thread2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("thread1", "thread5");
                        }

                        if (threadFlag.get()) {
                            runOnUiThread(runnable);
                            Log.d("thread1", "thread3");

                        } else {
                            i = 30;
                            Log.d("thread1", "thread4");
                            Thread.currentThread().interrupt();
                        }
                    }
                    Log.d("thread1", "thread1");
                }
            }
            ControlPost_f cpf = new ControlPost_f();
            cpf.lookupMyCommentList(ControlLogin_f.userinfo.getNickname());
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            //댓글을 작성한 게시글을 확인하는 쓰레드
            t.start();
        }

    }


    class MyPageBoard_UI implements Control {
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
            Custom_Dialog cd = new Custom_Dialog(MyPageBoardActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            /*if (dest == 0) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }*/
        }
    }
}
