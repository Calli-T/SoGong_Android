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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlComment_f;
import com.example.sogong.Control.ControlIngredients_f;
import com.example.sogong.Control.ControlLike_f;
import com.example.sogong.Control.ControlMailList_f;
import com.example.sogong.Control.ControlMail_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Control.ControlReport_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.Model.Mail;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.Model.Report;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecipeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    String[] pagenum;
    int currentPage;
    String currentSort;
    public static int totalpage; // 반환값 저장
    public static List<RecipePost_f> recipelist; // 반환값 저장
    public static int responseCode = 0;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 스레드 제어용 플래그
    private AtomicBoolean pagethreadFlag = new AtomicBoolean(); // 스레드 제어용 플래그
    private AtomicBoolean sortthreadFlag = new AtomicBoolean(); // 스레드 제어용 플래그
    public RecipeAdapter recipeAdapter;
    public RecyclerView recipeRecyclerView;
    Custon_ProgressDialog custon_progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    ControlRecipeList_f crlf = new ControlRecipeList_f();
    ControlRecipe_f crf = new ControlRecipe_f();
    ControlLike_f clf = new ControlLike_f();
    ControlReport_f cref = new ControlReport_f();
    ControlIngredients_f cif = new ControlIngredients_f();
    ControlComment_f ccf = new ControlComment_f();
    ControlMailList_f cmlf = new ControlMailList_f();
    ControlMail_f cmf = new ControlMail_f();
    boolean isSearch;
    Spinner pagespinner;
    Spinner sortspinner;
    ImageButton searchButton;
    private View view;
    Boolean firstpage;
    // UI controller
    RecipeList_UI rlu = new RecipeList_UI();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipeboard, container, false);
        Log.d("recipefragment", "시작");
        searchButton = view.findViewById(R.id.search_button);//검색 버튼
        //레시피 게시글 리스트 리사이클러뷰
        recipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_recyclerview);
        recipeAdapter = new RecipeAdapter();
        recipeRecyclerView.setAdapter(recipeAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeRecyclerView.setLayoutManager(layoutManager);
        //플로팅 버튼
        FloatingActionButton fab = view.findViewById(R.id.recipe_add_button);
        fab.setOnClickListener(new FABClickListener());
        responseCode = 0;

        //swipe레이아웃
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);



        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(requireActivity());
        custon_progressDialog.setCanceledOnTouchOutside(false);
        custon_progressDialog.show();

        //검색화면으로 이동
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        currentPage = 1;
        currentSort = "최근 순";
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("recipefragment", "재시작");
        isSearch = getActivity().getIntent().getBooleanExtra("isSearch", false);
        // UI controller
        RecipeList_UI rlu = new RecipeList_UI();
        custon_progressDialog.show();
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
                    pagespinner = view.findViewById(R.id.recipe_page_spinner);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pagenum);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pagespinner.setAdapter(adapter1);
                    pagespinner.setPrompt("이동할 페이지");
                    pagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //페이지 클릭 시 해당 페이지에 맞는 레시피 리스트로 전환
                            if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                currentPage = position + 1;
                                crlf.sortRecipeList(currentSort, position + 1);
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
                                                    Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    intent.putExtra("recipe_post", recipelist.get(position));
                                                    startActivity(intent);
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
                                                getActivity().runOnUiThread(runnable);
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
                                recipeAdapter.setRecipeList(recipelist);
                                //레시피 리사이클러뷰 클릭 이벤트
                                recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(int position, String data) {
                                        Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
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
                    sortspinner = view.findViewById(R.id.sort_spinner);
                    sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("recipefragment", "sort spinner " + sortspinner.getSelectedItem().toString() + " 클릭");
                            String sort_str = sortspinner.getSelectedItem().toString();
                            // #29 레시피 게시글 정렬 별표 주석으로 바꿀것
                            if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                currentSort = sort_str;
                                crlf.sortRecipeList(sort_str, currentPage);
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
                                                    Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    intent.putExtra("recipe_post", recipelist.get(position));
                                                    startActivity(intent);
                                                    //+조회수 관련 로직 추가할 것
                                                }
                                            });
                                            custon_progressDialog.dismiss();
                                        } else if (responseCode == 500) {
                                            responseCode = -1;
                                            sortthreadFlag.set(false);
                                            custon_progressDialog.dismiss();
                                            rlu.startDialog(0,"정렬 실패","정렬된 게시글 목록을 가져오는데 실패했습니다.",new ArrayList<>(Arrays.asList("확인")));
                                        }else if (responseCode == 502) {
                                            responseCode = -1;
                                            sortthreadFlag.set(false);
                                            custon_progressDialog.dismiss();
                                            rlu.startDialog(0,"서버 오류","알 수 없는 오류입니다.",new ArrayList<>(Arrays.asList("확인")));
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
                                                getActivity().runOnUiThread(runnable);
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
                                t.start();
                                //업데이트된 레시피 리스트로 전환
                                recipeAdapter.setRecipeList(recipelist);
                                //레시피 리사이클러뷰 클릭 이벤트
                                recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(int position, String data) {
                                        Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
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

                    //레시피 리사이클러뷰 클릭 이벤트
                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int position, String data) {
                            Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("recipe_post", recipelist.get(position));
                            startActivity(intent);
                        }
                    });

                    custon_progressDialog.dismiss();//로딩창 종료
                    Log.d("recipefragment", recipelist.get(0).toString());
                    Thread.currentThread().interrupt();
                } else if (responseCode == 500) {
                    custon_progressDialog.dismiss();//로딩창 종료
                    rlu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                } else if (responseCode == 502) {
                    custon_progressDialog.dismiss();//로딩창 종료
                    rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                        getActivity().runOnUiThread(runnable);
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
        crlf.sortRecipeList(currentSort, currentPage);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadFlag.set(false);
    }

    @Override
    public void onRefresh() {

        //새로 고침 코드
        updateLayoutView();
        //새로 고침 완료
        swipeRefreshLayout.setRefreshing(false);

    }

    public void ShowRecipeList() {

    }

    // 당겨서 새로고침 했을 때 뷰 변경 메서드
    public void updateLayoutView() {
        Log.d("recipefragment", "새로고침");
        RecipeList_UI rlu = new RecipeList_UI();
        custon_progressDialog.show();
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
                    pagespinner = view.findViewById(R.id.recipe_page_spinner);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pagenum);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pagespinner.setAdapter(adapter1);
                    pagespinner.setPrompt("이동할 페이지");
                    pagespinner.setSelection(currentPage - 1);
                    pagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //페이지 클릭 시 해당 페이지에 맞는 레시피 리스트로 전환
                            if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                currentPage = position + 1;
                                crlf.sortRecipeList(currentSort, position + 1);
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
                                                    Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
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
                                                getActivity().runOnUiThread(runnable);
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
                                recipeAdapter.setRecipeList(recipelist);
                                //레시피 리사이클러뷰 클릭 이벤트
                                recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(int position, String data) {
                                        Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
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

                    sortspinner = view.findViewById(R.id.sort_spinner);
                    if (currentSort.equals("최근 순")) {
                        sortspinner.setSelection(0);
                    } else if (currentSort.equals("조회수 순")) {
                        sortspinner.setSelection(1);
                    } else if (currentSort.equals("좋아요 순")) {
                        sortspinner.setSelection(2);
                    }
                    sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("recipefragment", "sort spinner " + sortspinner.getSelectedItem().toString() + " 클릭");
                            String sort_str = sortspinner.getSelectedItem().toString();
                            // #29 레시피 게시글 정렬 별표 주석으로 바꿀것
                            if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                currentPage = position + 1;
                                currentSort = sort_str;
                                crlf.sortRecipeList(sort_str, position + 1);
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
                                                    Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
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

                                            if (sortthreadFlag.get()) {
                                                getActivity().runOnUiThread(runnable);
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
                                t.start();
                                //업데이트된 레시피 리스트로 전환
                                recipeAdapter.setRecipeList(recipelist);
                                //레시피 리사이클러뷰 클릭 이벤트
                                recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(int position, String data) {
                                        Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
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

                    //레시피 리사이클러뷰 클릭 이벤트
                    recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClicked(int position, String data) {
                            Intent intent = new Intent(getActivity(), RecipeLookupActivity.class);
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
                    rlu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                } else if (responseCode == 502) {
                    custon_progressDialog.dismiss();//로딩창 종료
                    rlu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                        getActivity().runOnUiThread(runnable);
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
        crlf.sortRecipeList(currentSort, currentPage);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }


    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            Intent intent = new Intent(getActivity(), RecipeAddActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    class RecipeList_UI implements Control {
        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.toast_layout));
            TextView toast_textview = layout.findViewById(R.id.toast_textview);
            toast_textview.setText(String.valueOf(message));
            Toast toast = new Toast(getActivity());
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