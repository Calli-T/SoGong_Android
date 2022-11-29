package com.example.sogong.View;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLike_f;
import com.example.sogong.Control.ControlPhotoList_f;
import com.example.sogong.Control.ControlPhoto_f;
import com.example.sogong.Control.ControlRecipeList_f;
import com.example.sogong.Control.ControlRecipe_f;
import com.example.sogong.Control.ControlReport_f;
import com.example.sogong.Model.PhotoList;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.RecipePost_f;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.Model.Report;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PhotoFragment extends Fragment {
    FloatingActionButton addPhotoBtn;

    ControlPhotoList_f cplf = new ControlPhotoList_f();
    ControlPhoto_f cpf = new ControlPhoto_f();
    ControlLike_f clf = new ControlLike_f();
    ControlReport_f crf = new ControlReport_f();
    public int requestCode;
    private Uri mImageCaptureUri;
    int currentPage;
    String currentSort;
    String[] pagenum;
    public static int totalpage;
    public static int responseCode = 0;
    private AtomicBoolean threadFlag = new AtomicBoolean(); // 프래그먼트 전환에서 스레드를 잠재울 플래그
    private AtomicBoolean pagethreadFlag = new AtomicBoolean();
    private AtomicBoolean sortthreadFlag = new AtomicBoolean();
    Boolean firstpage;
    public static List<PhotoPost> photoList;
    private View view;
    private GridView gridview = null;
    private PhotoAdapter photoAdapter = null;
    Custon_ProgressDialog custon_progressDialog;

    Spinner pagespinner;
    Spinner sortspinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photoboard, container, false);

        gridview = (GridView) view.findViewById(R.id.photo_grid);
        photoAdapter = new PhotoAdapter();
        gridview.setAdapter(photoAdapter);

        responseCode = 0;

        // UI controller
        PhotoList_UI plu = new PhotoList_UI();

        // 사용할 컴포넌트 초기화
        FloatingActionButton fab = view.findViewById(R.id.photo_add_button);
        fab.setOnClickListener(new FABClickListener());
        // 추가) 요리 사진 게시판

        threadFlag.set(true);

        //로딩창 구현
        custon_progressDialog = new Custon_ProgressDialog(requireActivity());
        custon_progressDialog.setCanceledOnTouchOutside(false);
        custon_progressDialog.show();
        currentSort = "최근 순";
        currentPage = 1;

        return view;
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("photofragment", "재시작");
        // UI controller
        PhotoList_UI plu = new PhotoList_UI();

        // 사용할 컴포넌트 초기화
        FloatingActionButton fab = view.findViewById(R.id.photo_add_button);
        fab.setOnClickListener(new FABClickListener());
        // 추가) 요리 사진 게시판


        custon_progressDialog.show();

        threadFlag.set(true);
        firstpage = true;

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (responseCode == 200) {
                    threadFlag.set(false);
                    responseCode = -1;
                    pagenum = new String[totalpage];
                    for (int i = 1; i <= totalpage; i++) {
                        pagenum[i - 1] = String.valueOf(i);
                    }

                    photoAdapter.setPhotoList(photoList);
                    gridview.setAdapter(photoAdapter);
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("사진", "position = " + position + " id = " + id);
                            Intent intent = new Intent(getActivity(), PhotoLookupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("photo_post", photoList.get(position));
                            startActivity(intent);
                        }
                    });
                    pagespinner = view.findViewById(R.id.photo_page_spinner);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pagenum);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pagespinner.setAdapter(adapter1);
                    pagespinner.setPrompt("이동할 페이지");
                    pagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (!firstpage) {//기본적으로 1페이지로 설정되어있어서 1페이지를 다시 불러오게 되서 제일 처음에 불러오는 경우는 무시하도록 불리언값 주었음
                                currentPage = position + 1;
                                cplf.sortPhotoList(currentSort, position + 1);
                                custon_progressDialog.show();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (responseCode == 200) {
                                            responseCode = -1;
                                            pagethreadFlag.set(false);
                                            //업데이트된 레시피 리스트로 전환
                                            photoAdapter.setPhotoList(photoList);
                                            gridview.setAdapter(photoAdapter);
                                            //사진 리사이클러뷰 클릭 이벤트
                                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Log.d("사진", "position = " + position + " id = " + id);
                                                    Intent intent = new Intent(getActivity(), PhotoLookupActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    intent.putExtra("photo_post", photoList.get(position));
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
                                photoAdapter.setPhotoList(photoList);
                                gridview.setAdapter(photoAdapter);
                                //레시피 리사이클러뷰 클릭 이벤트
                                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("사진", "position = " + position + " id = " + id);
                                        Intent intent = new Intent(getActivity(), PhotoLookupActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("photo_post", photoList.get(position));
                                        startActivity(intent);
                                    }
                                });
                                Log.d("recipefragment", "page spinner " + position + " 클릭");
                            }
                            firstpage = false;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

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
                                cplf.sortPhotoList(sort_str, currentPage);
                                custon_progressDialog.show();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (responseCode == 200) {
                                            responseCode = -1;
                                            sortthreadFlag.set(false);
                                            //업데이트된 레시피 리스트로 전환
                                            photoAdapter.setPhotoList(photoList);
                                            gridview.setAdapter(photoAdapter);
                                            //레시피 리사이클러뷰 클릭 이벤트
                                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Log.d("사진", "position = " + position + " id = " + id);
                                                    Intent intent = new Intent(getActivity(), PhotoLookupActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    intent.putExtra("photo_post", photoList.get(position));
                                                    startActivity(intent);
                                                }
                                            });
                                            custon_progressDialog.dismiss();
                                        } else if(responseCode == 500){
                                            responseCode = -1;
                                            sortthreadFlag.set(false);
                                            custon_progressDialog.dismiss();
                                            plu.startDialog(0, "서버 오류", "게시글 요청을 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                                        }else if(responseCode == 502){
                                            responseCode = -1;
                                            sortthreadFlag.set(false);
                                            custon_progressDialog.dismiss();
                                            plu.startDialog(0,"서버 오류","알 수 없는 오류입니다.",new ArrayList<>(Arrays.asList("확인")));
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
                                photoAdapter.setPhotoList(photoList);
                                gridview.setAdapter(photoAdapter);
                                //레시피 리사이클러뷰 클릭 이벤트
                                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("사진", "position = " + position + " id = " + id);
                                        Intent intent = new Intent(getActivity(), PhotoLookupActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("photo_post", photoList.get(position));
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
                    gridview.setAdapter(photoAdapter);
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("사진", "position = " + position + " id = " + id);
                            Intent intent = new Intent(getActivity(), PhotoLookupActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("photo_post", photoList.get(position));
                            startActivity(intent);
                        }
                    });
                    custon_progressDialog.dismiss();//로딩창 종료
                    Thread.currentThread().interrupt();
                    // UI 코드 작성해주세요
                } else if (responseCode == 500) {
                    custon_progressDialog.dismiss();//로딩창 종료
                    plu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                } else if (responseCode == 502) {
                    custon_progressDialog.dismiss();//로딩창 종료
                    plu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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
                        getActivity().runOnUiThread(runnable);
                    else {
                        i = 30;
                    }
                }
            }
        }

        cplf.sortPhotoList(currentSort, currentPage); // 시작은 첫 페이지 고정

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadFlag.set(false);
    }

    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            FAB Click 이벤트 처리 구간
            Intent intent = new Intent(getActivity(), PhotoAddActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }

    class PhotoList_UI implements Control {
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
