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
    public static int totalpage;
    public static List<RecipePost_f> recipelist;
    public static int responseCode = 0;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    private AtomicBoolean pagethreadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    private AtomicBoolean sortthreadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
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

    Spinner pagespinner;
    Spinner sortspinner;
    ImageButton searchButton;
    private View view;
    Boolean firstpage;

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

        // UI controller
        RecipeList_UI rlu = new RecipeList_UI();

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
      /*  // 추가) 레시피 게시판 조회 호출 코드
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
                                threadFlag.set(true);
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
        crlf.lookupRecipeList(1);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
*/

        // #23 레시피 게시글 열람 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeLookupActivity.responseCode == 200) {
                    plu.startToast(RecipeLookupActivity.recipePostF.toString());
                    responseCode = -1;
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlRecipe_f crf = new ControlRecipe_f();
        crf.lookupRecipe(2);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        */

        // #24 레시피 게시글 등록 호출 코드
        /*
        Recipe_Ingredients recipe_ingredients = new Recipe_Ingredients(0, "asparagus", "g", 0, 200); // 레시피의 식재료 추가
        List<Recipe_Ingredients> recipe_ingredientsList = new ArrayList<Recipe_Ingredients>(); // newRecipe 의 매개변수로
        recipe_ingredientsList.add(recipe_ingredients); // 위에서 추가한 식재료들 리스트에 추가
        RecipePost_f newRecipe = new RecipePost_f(0, "test", "Heartaches", "test99", 3, "Heartaches", 0, 0, 0, "", recipe_ingredientsList, null);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeAddActivity.responseCode == 200) {
                    plu.startToast(RecipeAddActivity.newRecipe.toString());
                    RecipeAddActivity.responseCode = -1;
                } else if (RecipeAddActivity.responseCode == 500) {
                } else if (responseCode == 502) {

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

        ControlRecipe_f crf = new ControlRecipe_f();
        crf.addRecipe(newRecipe);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
         */

        // #25 레시피 게시글 수정 호출 코드
        // 이거 가져온걸 '그대로' 쓰던지, 아니면 테스트용으로 쓸거면 완벽하게 똑같이 만드시오
        // 이거 약간 버그있음, 레시피 재료 번호 옮기기 가능함
        /*
        Recipe_Ingredients recipe_ingredients = new Recipe_Ingredients(33, "asparagus", "g", 42, 200); // 레시피의 식재료 추가
        List<Recipe_Ingredients> recipe_ingredientsList = new ArrayList<Recipe_Ingredients>(); // newRecipe 의 매개변수로
        recipe_ingredientsList.add(recipe_ingredients); // 위에서 추가한 식재료들 리스트에 추가
        RecipePost_f editRecipe = new RecipePost_f(41, "test", "Heartaches", "test99", 3, "I can't believe it's just a burning memory", 0, 0, 0, "2022-11-21T02:34:18.212072+09:00", recipe_ingredientsList, null);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeAddActivity.responseCode == 200) {
                    plu.startToast(RecipeAddActivity.newRecipe.toString());
                    RecipeAddActivity.responseCode = -1;
                } else if (RecipeAddActivity.responseCode == 500) {
                } else if (responseCode == 502) {

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

        ControlRecipe_f crf = new ControlRecipe_f();
        crf.editRecipe(editRecipe);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
         */

        // #26 레시피 게시글 삭제 호출 코드
        // Lookup 액티비티 보면서 삭제할거라 거기 responseCode씀
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeLookupActivity.responseCode == 200) {
                    plu.startToast("삭제완료");
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlRecipe_f crf = new ControlRecipe_f();
        crf.deleteRecipe("test", 44);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
         */

        // #27 레시피 게시글 "좋아요" 등록/취소 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeLookupActivity.responseCode == 200) {
                    plu.startToast("좋아요");
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlLike_f clf = new ControlLike_f();
        clf.unLikePost("test", 1, 42); //실전에서 쓸 때는 userInfo에서 닉하고 가져오셈
        clf.likePost("test", 1, 42); // 좋아요/취소는 알아서 골라쓰셈

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        */

        // #28 레시피 게시글 검색 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeFragment.responseCode == 200) {
                    plu.startToast(RecipeFragment.recipelist.get(0).toString());
                    RecipeFragment.responseCode = -1;
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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
        crlf.searchRecipeList("타이핑", "", "작성자", "test2", 1);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
         */

        // #29 레시피 게시글 정렬 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeFragment.responseCode == 200) {
                    plu.startToast(RecipeFragment.recipelist.get(0).toString());
                    RecipeFragment.responseCode = -1;
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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
        crlf.sortRecipeList("좋아요 순", 1);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
         */

        // #30 레시피 게시글 신고 & #36 레시피 게시글 신고 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeLookupActivity.responseCode == 200) {
                    plu.startToast(RecipeLookupActivity.responseCode+"");
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        // 댓글 신고는 양식을 -1로, 댓글 id에 맞추고, 함수를 reportComment로 쓰기만 하면 끝!
        ControlReport_f cref = new ControlReport_f();
        Report reportInfo_recipe = new Report("test", "What does it matter how my heart breaks", 42, 1);
        //Report reportInfo_comment = new Report("test", "But my heart aches for you", 37, -1);
        cref.reportPost(reportInfo_recipe);
        //cref.reportComment(reportInfo_comment);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
         */

        // #33 레시피 게시판 댓글 작성 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeLookupActivity.responseCode == 200) {
                    rlu.startToast("댓글 작성 완료");
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlComment_f ccf = new ControlComment_f();
        Comment newComment = new Comment(0, "test", 42, "Heartaches, heartaches", "");
        ccf.writeComment(newComment);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
         */

        // #34 레시피 게시판 댓글 수정 호출 코드
        /*final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeLookupActivity.responseCode == 200) {
                    rlu.startToast("댓글 수정 완료");
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlComment_f ccf = new ControlComment_f();
        Comment newComment = new Comment(12, "test", 42, "지친 어깰 돌아서", "2022-11-21T19:33:51.863823+09:00");
        ccf.editComment(newComment);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        */

        // #35 레시피 게시판 댓글 삭제 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (RecipeLookupActivity.responseCode == 200) {
                    rlu.startToast("댓글 삭제 완료");
                    RecipeLookupActivity.responseCode = -1;
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlComment_f ccf = new ControlComment_f();
        ccf.deleteComment("test", 9);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
         */

        // 추가) 쪽지함 조회 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (MailBoxActivity.responseCode == 200) {
                    rlu.startToast(MailBoxActivity.list.toString());
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlMailList_f cmlf = new ControlMailList_f();
        cmlf.lookupMailList(1, "jin92");

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        */

        // #37 쪽지 열람 호출 코드
        /*final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (MailLookupActivity.responseCode == 200) {
                    rlu.startToast(MailLookupActivity.mail.toString());
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlMail_f cmf = new ControlMail_f();
        cmf.lookupMail(36);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        */

        // #38 쪽지 보내기 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (MailSendActivity.responseCode == 200) {
                    rlu.startToast(MailSendActivity.responseCode+"");
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlMail_f cmf = new ControlMail_f();
        cmf.sendMail(new Mail(0,"test", "jin92", "지중해", "죽음보다 더 깊은", "", false, false));

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        */

        // #39 쪽지 삭제하기 호출 코드
        /*
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {

                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlMail_f cmf = new ControlMail_f();
        cmf.deleteMail("jin92", 36);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
        */

        /* 추가) 레시피 게시판 조회 */
        //crlf.lookupRecipeList(1);

        /* #23 레시피 게시글 열람 */
        //crf.lookupRecipe(24);

        /* #24 레시피 게시글 등록 */
        /*
        Recipe_Ingredients recipe_ingredients = new Recipe_Ingredients(0, "yangpa", "g", 0, 500); // 레시피의 식재료 추가
        List<Recipe_Ingredients> recipe_ingredientsList = new ArrayList<Recipe_Ingredients>(); // newRecipe 의 매개변수로
        recipe_ingredientsList.add(recipe_ingredients); // 위에서 추가한 식재료들 리스트에 추가
        RecipePost_f newRecipe = new RecipePost_f(0, "test", "android test2", "android test2", 0, "android test2", 0, 0, 0, "", recipe_ingredientsList, null);
        crf.addRecipe(newRecipe);
        */

        /* #25 레시피 게시글 수정 */
        /*
        Recipe_Ingredients recipe_ingredients = new Recipe_Ingredients(25, "yangpa", "g", 30, 1000); // 수정이기 때문에 원래 갖고있던 데이터 쓰면됨. 나는 데이터 없이 하는거라 일부러 만든거
        List<Recipe_Ingredients> recipe_ingredientsList = new ArrayList<Recipe_Ingredients>(); // 역시 갖고있던거 쓰면됨
        recipe_ingredientsList.add(recipe_ingredients);
        RecipePost_f edittedRecipe = new RecipePost_f(30, "test", "android test2", "android update", 0, "android update", 0, 0, 0, "", recipe_ingredientsList, null);
        // 얘도 역시 기존에 있던거 수정하면됨
        crf.editRecipe(edittedRecipe);
        */

        /* #26 레시피 게시글 삭제 */
        //crf.deleteRecipe("test", 31);

        /* #27 레시피 게시글 "좋아요" 등록 */
        //clf.likePost("test", 1, 28);

        /* #27 레시피 게시글 "좋아요" 취소 */
        //clf.unLikePost("test", 1, 28);

        /* #28 레시피 게시글 검색 */
        //crlf.searchRecipeList("카테고리", "test99-null test", "", "", 1);

        /* #29 레시피 게시글 정렬 */
        //crlf.sortRecipeList("좋아요 순", 1);

        /* #30 레시피 게시글 신고 */
        //Report reportInfo = new Report("test", "android recipe", 28, 1);
        //cref.reportPost(reportInfo);

        /* #31 없는 재료 보여주기 */
        //cif.lookupUnExistIngredients("test", 19);

        /* #32 남은 재료 계산하기 */
        //cif.remainAmmounts("test", 19);

        /* #33 레시피 게시판 댓글 작성 */
        //Comment comment = new Comment(0, "test", 28, "android test", "");
        //ccf.writeComment(comment);

        /* #34 레시피 게시판 댓글 수정 */
        //Comment comment = new Comment(8, "android fix", 28, "android fix", "");
        //ccf.editComment(comment);

        /* #35 레시피 게시판 댓글 삭제 */
        //ccf.deleteComment("test", 8);

        /* #36 레시피 게시판 댓글 신고 */
        //Report reportInfo = new Report("test", "android comment report test", 28, -1);
        //cref.reportComment(reportInfo);

        /* 추가) 쪽지함 열람 */
        //cmlf.lookupMailList(1, "test");

        /* #37 쪽지 열람 */
        //cmf.lookupMail(3);

        /* #38 쪽지 보내기 */
        //Mail mail = new Mail(0, "test", "test2", "android test", "android test", "", false, false);
        //cmf.sendMail(mail);

        /* #39 쪽지 삭제하기 */
        //cmf.deleteMail("test", 13);
        currentPage = 1;
        currentSort = "최근 순";
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("recipefragment", "재시작");
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
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();
        Log.d("recipefragment", "새로고침");
//        custon_progressDialog = new Custon_ProgressDialog(requireActivity());
//        custon_progressDialog.setCanceledOnTouchOutside(false);
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
                    }else if(currentSort.equals("조회수 순")){
                        sortspinner.setSelection(1);
                    }else if(currentSort.equals("좋아요 순")){
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

// 0단계: 주석 보고 주석 써놓기

// 1단계
/*
final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    rlu.startToast();
                } else if(responseCode == 500){

                }else if(responseCode == 502){

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

        ControlRecipe_f crf = new ControlRecipe_f();
        crf.lookupRecipe(2);

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
 */

// 2단계: 컨트롤에서 static 건드리기

// 3단계: 테스트

// 4단계: 원래 있어야할 장소에 코드 냅두기