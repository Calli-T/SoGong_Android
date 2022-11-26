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
import com.example.sogong.Control.ControlMyRecipe_f;
import com.example.sogong.Control.ControlPost_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Control.ControlReport_f;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipePost_f;
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
    private AtomicBoolean pagethreadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그

    public RecipeAdapter recipeAdapter;
    public RecyclerView recipeRecyclerView;
    Custon_ProgressDialog custon_progressDialog;
    ControlRecipeList_f crlf = new ControlRecipeList_f();
    ControlRecipe_f crf = new ControlRecipe_f();
    ControlLike_f clf = new ControlLike_f();
    ControlReport_f cref = new ControlReport_f();
    ControlIngredients_f cif = new ControlIngredients_f();
    ControlComment_f ccf = new ControlComment_f();
    ControlMail_f cmf = new ControlMail_f();

    Spinner pagespinner;
    Spinner sortspinner;
    ImageButton searchButton;
    private View view;
    Boolean firstpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("post_type", -1);
        Log.d("type","type = "+type);

        if (type == 0) {//레시피 게시판
            Log.d("mypageRecipe", "시작");
            setContentView(R.layout.fragment_recipeboard);
            searchButton = findViewById(R.id.search_button);//검색 버튼
            //레시피 게시글 리스트 리사이클러뷰
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);
            //플로팅 버튼
            FloatingActionButton fab = findViewById(R.id.recipe_add_button);
            fab.setVisibility(View.INVISIBLE);
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
                        recipeAdapter.setRecipeList(recipelist);
                        /*pagenum = new String[totalpage];
                        for (int i = 1; i <= totalpage; i++) {
                            pagenum[i - 1] = String.valueOf(i);
                        }
                        //페이지 수 스피너 설정
                        pagespinner = findViewById(R.id.recipe_page_spinner);
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MyPageBoardActivity.this, android.R.layout.simple_spinner_item, pagenum);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        pagespinner.setAdapter(adapter1);
                        pagespinner.setPrompt("이동할 페이지");
                        pagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //페이지 클릭 시 해당 페이지에 맞는 레시피 리스트로 전환
                                if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                    crlf.lookupRecipeList(position + 1);
                                    custon_progressDialog.show();
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (responseCode == 200) {
                                                responseCode = -1;
                                                pagethreadFlag.set(false);
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
                                                        //+조회수 관련 로직 추가할 것
                                                    }
                                                });
                                                custon_progressDialog.dismiss();
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

                                                if (pagethreadFlag.get()) {
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
                                    pagethreadFlag.set(true);
                                    threadFlag.set(true);
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
                                            //+조회수 관련 로직 추가할 것
                                        }
                                    });
                                    Log.d("recipefragment", "page spinner " + position + " 클릭");
                                }
                                firstpage = false;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("recipefragment", "sort spinner " + sortspinner.getSelectedItem().toString() + " 클릭");
                                String sort_str = sortspinner.getSelectedItem().toString();
                                // #29 레시피 게시글 정렬 별표 주석으로 바꿀것
                                //crlf.sortRecipeList(sort_str, 1);
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
                                //+조회수 관련 로직 추가할 것
                            }
                        });

                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        custon_progressDialog.dismiss();//로딩창 종료
//                            rlu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode == 502) {
                        custon_progressDialog.dismiss();//로딩창 종료
//                            rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
            t.start();
        } else if (type == 1) {
            //사진게시판
        } else if (type == 2) {
            //좋아요한 리스트
            Log.d("mypageRecipe", "시작");
            setContentView(R.layout.fragment_recipeboard);
            searchButton = findViewById(R.id.search_button);//검색 버튼
            //레시피 게시글 리스트 리사이클러뷰
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);
            //플로팅 버튼
            FloatingActionButton fab = findViewById(R.id.recipe_add_button);
            fab.setVisibility(View.INVISIBLE);
            responseCode = 0;

//                //swipe레이아웃
//                swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
//                swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

            // UI controller
//                RecipeList_UI rlu = new RecipeFragment.RecipeList_UI();

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
                        recipeAdapter.setRecipeList(recipelist);
                        pagenum = new String[totalpage];
                        for (int i = 1; i <= totalpage; i++) {
                            pagenum[i - 1] = String.valueOf(i);
                        }
                        //페이지 수 스피너 설정
                        pagespinner = findViewById(R.id.recipe_page_spinner);
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MyPageBoardActivity.this, android.R.layout.simple_spinner_item, pagenum);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        pagespinner.setAdapter(adapter1);
                        pagespinner.setPrompt("이동할 페이지");
                        pagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //페이지 클릭 시 해당 페이지에 맞는 레시피 리스트로 전환
                                if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                    crlf.lookupRecipeList(position + 1);
                                    custon_progressDialog.show();
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (responseCode == 200) {
                                                responseCode = -1;
                                                pagethreadFlag.set(false);
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
                                                        //+조회수 관련 로직 추가할 것
                                                    }
                                                });
                                                custon_progressDialog.dismiss();
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

                                                if (pagethreadFlag.get()) {
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
                                    pagethreadFlag.set(true);
                                    threadFlag.set(true);
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
                                            //+조회수 관련 로직 추가할 것
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

                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("recipefragment", "sort spinner " + sortspinner.getSelectedItem().toString() + " 클릭");
                                String sort_str = sortspinner.getSelectedItem().toString();
                                // #29 레시피 게시글 정렬 별표 주석으로 바꿀것
                                //crlf.sortRecipeList(sort_str, 1);
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
                                //+조회수 관련 로직 추가할 것
                            }
                        });

                        custon_progressDialog.dismiss();//로딩창 종료
                        Log.d("recipefragment", recipelist.get(0).toString());
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        custon_progressDialog.dismiss();//로딩창 종료
//                            rlu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode == 502) {
                        custon_progressDialog.dismiss();//로딩창 종료
//                            rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
            ControlRecipeList_f crlf = new ControlRecipeList_f();
            ControlPost_f cpf = new ControlPost_f();
            cpf.lookupMyLikeList("test", 1);
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            t.start();
        } else if (type == 3) {
            //댓글을 작성한 리스트
            Log.d("mypageRecipe", "시작");
            setContentView(R.layout.fragment_recipeboard);
            searchButton = findViewById(R.id.search_button);//검색 버튼
            //레시피 게시글 리스트 리사이클러뷰
            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerview);
            recipeAdapter = new RecipeAdapter();
            recipeRecyclerView.setAdapter(recipeAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(layoutManager);
            //플로팅 버튼
            FloatingActionButton fab = findViewById(R.id.recipe_add_button);
            fab.setVisibility(View.INVISIBLE);
            responseCode = 0;

//                //swipe레이아웃
//                swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
//                swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

            // UI controller
//                RecipeList_UI rlu = new RecipeFragment.RecipeList_UI();

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
                        recipeAdapter.setRecipeList(recipelist);
                        pagenum = new String[totalpage];
                        for (int i = 1; i <= totalpage; i++) {
                            pagenum[i - 1] = String.valueOf(i);
                        }
                        //페이지 수 스피너 설정
                        pagespinner = findViewById(R.id.recipe_page_spinner);
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MyPageBoardActivity.this, android.R.layout.simple_spinner_item, pagenum);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        pagespinner.setAdapter(adapter1);
                        pagespinner.setPrompt("이동할 페이지");
                        pagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //페이지 클릭 시 해당 페이지에 맞는 레시피 리스트로 전환
                                if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                    crlf.lookupRecipeList(position + 1);
                                    custon_progressDialog.show();
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (responseCode == 200) {
                                                responseCode = -1;
                                                pagethreadFlag.set(false);
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
                                                        //+조회수 관련 로직 추가할 것
                                                    }
                                                });
                                                custon_progressDialog.dismiss();
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

                                                if (pagethreadFlag.get()) {
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
                                    pagethreadFlag.set(true);
                                    threadFlag.set(true);
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
                                            //+조회수 관련 로직 추가할 것
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

                        sortspinner = findViewById(R.id.sort_spinner);
                        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.d("recipefragment", "sort spinner " + sortspinner.getSelectedItem().toString() + " 클릭");
                                String sort_str = sortspinner.getSelectedItem().toString();
                                // #29 레시피 게시글 정렬 별표 주석으로 바꿀것
                                //crlf.sortRecipeList(sort_str, 1);
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
                                //+조회수 관련 로직 추가할 것
                            }
                        });

                        custon_progressDialog.dismiss();//로딩창 종료
                        Log.d("recipefragment", recipelist.get(0).toString());
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        custon_progressDialog.dismiss();//로딩창 종료
//                            rlu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode == 502) {
                        custon_progressDialog.dismiss();//로딩창 종료
//                            rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
            t.start();
        }

    }


    class MyPageRecipe_UI implements Control {
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
