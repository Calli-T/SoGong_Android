package com.example.sogong.View;

import android.content.Intent;
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
import com.example.sogong.Model.Post;
import com.example.sogong.Model.PostObject;
import com.example.sogong.R;
import com.example.sogong.View.Legacy.PostActivity;
import com.example.sogong.View.Legacy.PostAdapter;
import com.example.sogong.View.Legacy.RetrofitClient;
import com.example.sogong.View.Legacy.RetrofitService;
import com.example.sogong.View.Legacy.Server;
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
    PostAdapter postAdapter;

    HomeFragment homeFragment;
    RecipeFragment recipeFragment;
    PhotoFragment photoFragment;
    MyPageFragment myPageFragment;

    //------------------------------------------------------------

    //------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_postlist);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        recipeFragment = new RecipeFragment();
        photoFragment = new PhotoFragment();
        myPageFragment = new MyPageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
        NavigationBarView navigationBarView = findViewById(R.id.bottomNavi);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                        return true;
                    case R.id.recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, recipeFragment).commit();
                        return true;
                    case R.id.photo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, photoFragment).commit();
                        return true;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, myPageFragment).commit();
                        return true;
                }
                return false;
            }
        });
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

    // 게시물 리스트를 읽어오는 함수
    class GetBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute");
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute, " + result);

// 배열들 초기화
            titleList.clear();
            seqList.clear();

            // 결과물이 JSONArray 형태로 넘어오기 때문에 파싱
            //JSONArray jsonArray = new JSONArray(result);
            ArrayList<Post> postArrayList = Server.getPostlist();

            for (int i = 0; i < postArrayList.size(); i++) {
                String title = postArrayList.get(i).getTitle();
                String seq = Integer.toString(i);

                titleList.add(title);
                seqList.add(seq);
                Log.d(TAG, title + " 추가");
            }


//                for(int i=0;i<jsonArray.length();i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                    String title = jsonObject.optString("title");
//                    String seq = jsonObject.optString("seq");
//
//// title, seq 값을 변수로 받아서 배열에 추가
//                    titleList.add(title);
//                    seqList.add(seq);
//
//                }

// ListView 에서 사용할 arrayAdapter를 생성하고, ListView 와 연결
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, titleList);
            listView.setAdapter(arrayAdapter);

// arrayAdapter의 데이터가 변경되었을때 새로고침
            arrayAdapter.notifyDataSetChanged();


        }


        @Override
        protected String doInBackground(String... params) {
//
// String userid = params[0];
// String passwd = params[1];

//            String server_url = "http://15.164.252.136/load_board.php";
//
//
//            URL url;
//            String response = "";
//            try {
//                url = new URL(server_url);
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//                Uri.Builder builder = new Uri.Builder()
//                        .appendQueryParameter("userid", "");
//// .appendQueryParameter("passwd", passwd);
//                String query = builder.build().getEncodedQuery();
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(query);
//                writer.flush();
//                writer.close();
//                os.close();
//
//                conn.connect();
//                int responseCode=conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//                    String line;
//                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    while ((line=br.readLine()) != null) {
//                        response+=line;
//                    }
//                }
//                else {
//                    response="";
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return response;
            return "success";
        }
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