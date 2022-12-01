package com.example.sogong.View;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlMyRecipe_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.Model.SearchInfo;
import com.example.sogong.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecipeSearchResultActivity extends AppCompatActivity {
    public static int responseCode; // 응답 코드
    public static List<RecipePost_f> recipeList; // 반환값 저장
    public static int totalpage;
    String[] pagenum;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 스레드 제어용 플래그
    private AtomicBoolean pagethreadFlag = new AtomicBoolean(); // 스레드 제어용 플래그
    TextView searchContent;
    Spinner pageSpinner;
    Button researchButton;
    Custon_ProgressDialog custon_progressDialog;

    ControlRecipeList_f crlf = new ControlRecipeList_f();
    ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
    public RecipeAdapter recipeAdapter;
    public RecyclerView recipeRecyclerView;
    Boolean firstpage;
    String searchType;
    String categories;
    String keywordType;
    String keyword;
    Boolean isMyPage;
    int currentPage;

    StringBuilder searchContent_str;

    SearchResult_UI sru = new SearchResult_UI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        //사용할 컴포넌트들 등록
        searchType = getIntent().getStringExtra("searchType");
        categories = getIntent().getStringExtra("categories");
        keywordType = getIntent().getStringExtra("keywordType");
        keyword = getIntent().getStringExtra("keyword");
        isMyPage = getIntent().getBooleanExtra("isMyPage", false);

        searchContent = findViewById(R.id.search_text);
        researchButton = findViewById(R.id.re_search_button);

        //레시피 게시글 리스트 리사이클러뷰
        recipeRecyclerView = (RecyclerView) findViewById(R.id.search_recipe_recyclerview);
        recipeAdapter = new RecipeAdapter();
        recipeRecyclerView.setAdapter(recipeAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipeSearchResultActivity.this);
        recipeRecyclerView.setLayoutManager(layoutManager);

        searchContent_str = new StringBuilder();
        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(RecipeSearchResultActivity.this);
        custon_progressDialog.setCanceledOnTouchOutside(false);
        custon_progressDialog.show();
        firstpage = true;

        if (!isMyPage) {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;

                        if (searchType.equals("타이핑")) {
                            searchContent.setText("");
                            searchContent_str.append(keywordType);
                            searchContent_str.append(" : ");
                            searchContent_str.append(keyword);
                            searchContent.setText(searchContent_str);
                        } else if (searchType.equals("카테고리")) {
                            searchContent.setText("");
                            searchContent_str.append(categories);
                            searchContent.setText(searchContent_str);
                        }
                        if (recipeList.size() == 0) {
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setText("검색결과 레시피가 존재하지 않습니다.");
                            noResult.setVisibility(View.VISIBLE);
                        }
                        recipeAdapter.setRecipeList(recipeList);
                        pagenum = new String[totalpage];
                        for (int i = 1; i <= totalpage; i++) {
                            pagenum[i - 1] = String.valueOf(i);
                        }
                        //페이지 수 스피너 설정
                        pageSpinner = findViewById(R.id.recipe_page_spinner);
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RecipeSearchResultActivity.this, android.R.layout.simple_spinner_item, pagenum);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        pageSpinner.setAdapter(adapter1);
                        pageSpinner.setPrompt("이동할 페이지");
                        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //페이지 클릭 시 해당 페이지에 맞는 레시피 리스트로 전환
                                if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                    currentPage = position + 1;
                                    crlf.searchRecipeList(searchType, categories, keywordType, keyword, position + 1);
                                    custon_progressDialog.show();
                                    final Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (responseCode == 200) {
                                                responseCode = -1;
                                                pagethreadFlag.set(false);
                                                //업데이트된 레시피 리스트로 전환
                                                recipeAdapter.setRecipeList(recipeList);
                                                //레시피 리사이클러뷰 클릭 이벤트
                                                recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClicked(int position, String data) {
                                                        Intent intent = new Intent(RecipeSearchResultActivity.this, RecipeLookupActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        intent.putExtra("recipe_post", recipeList.get(position));
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
                                    t.start();
                                    //업데이트된 레시피 리스트로 전환
                                    recipeAdapter.setRecipeList(recipeList);
                                    //레시피 리사이클러뷰 클릭 이벤트
                                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClicked(int position, String data) {
                                            Intent intent = new Intent(RecipeSearchResultActivity.this, RecipeLookupActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            intent.putExtra("recipe_post", recipeList.get(position));
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
                                Intent intent = new Intent(RecipeSearchResultActivity.this, RecipeLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("recipe_post", recipeList.get(position));
                                startActivity(intent);
                                //+조회수 관련 로직 추가할 것
                            }
                        });
                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                        sru.startDialog(0,"요청 실패","검색 결과 요청에 실패하였습니다.",new ArrayList<>(Arrays.asList("확인")));
                    } else if (responseCode == 502) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                        sru.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
            crlf.searchRecipeList(searchType, categories, keywordType, keyword, 1);
            threadFlag.set(true);
            responseCode = 0;
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            t.start();
            researchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (responseCode == 200) {
                        threadFlag.set(false);
                        responseCode = -1;

                        searchContent.setText("");
                        searchContent_str.append(keyword);
                        searchContent.setText(searchContent_str);
                        if (recipeList.size() == 0) {
                            TextView noResult = findViewById(R.id.noResult);
                            noResult.setText("검색결과 레시피가 존재하지 않습니다.");
                            noResult.setVisibility(View.VISIBLE);
                        }
                        recipeAdapter.setRecipeList(recipeList);
                        //레시피 리사이클러뷰 클릭 이벤트
                        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(int position, String data) {
                                Intent intent = new Intent(RecipeSearchResultActivity.this, RecipeLookupActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("recipe_post", recipeList.get(position));
                                startActivity(intent);
                                //+조회수 관련 로직 추가할 것
                            }
                        });
                        custon_progressDialog.dismiss();//로딩창 종료
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 404) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        sru.startDialog(0,"요청 실패","검색 결과 요청에 실패했습니다.",new ArrayList<>(Arrays.asList("확인")));
                        Thread.currentThread().interrupt();
                    } else if (responseCode == 500) {
                        threadFlag.set(false);
                        responseCode = -1;
                        custon_progressDialog.dismiss();//로딩창 종료
                        sru.startDialog(0,"서버 오류","알 수 없는 오류입니다.",new ArrayList<>(Arrays.asList("확인")));
                        Thread.currentThread().interrupt();

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
            SearchInfo searchInfo = new SearchInfo(ControlLogin_f.userinfo.getNickname(), keyword);
            cmrf.searchMyRecipeList(searchInfo);
            threadFlag.set(true);
            responseCode = 0;
            NewRunnable nr = new NewRunnable();
            Thread t = new Thread(nr);
            t.start();

        }
    }
    class SearchResult_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 50); //TODO 메시지가 표시되는 위치지정 (하단 표시)
            toast.setDuration(Toast.LENGTH_SHORT); //메시지 표시 시간
            toast.setView(layout);
            toast.show();
        }

        @Override
        public void startDialog(int type, String title, String message, List<String> btnTxtList) {
            Custom_Dialog cd = new Custom_Dialog(RecipeSearchResultActivity.this);
            cd.callFunction(title, message, type, btnTxtList);
        }

        // 0은 홈, 1은 회원가입(바로 이메일 인증으로)
        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(RecipeSearchResultActivity.this, RefrigeratorActivity.class);
                startActivity(intent);
            }
        }
    }
}
