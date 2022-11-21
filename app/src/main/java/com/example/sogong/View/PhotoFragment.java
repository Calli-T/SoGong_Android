package com.example.sogong.View;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotoFragment extends Fragment {
    FloatingActionButton addPhotoBtn;

    ControlPhotoList_f cplf = new ControlPhotoList_f();
    ControlPhoto_f cpf = new ControlPhoto_f();
    ControlLike_f clf = new ControlLike_f();
    ControlReport_f crf = new ControlReport_f();
    public int requestCode;
    private Uri mImageCaptureUri;
    public static int totalpage;
    public static int responseCode = 0;
    private boolean threadFlag; // 프래그먼트 전환에서 스레드를 잠재울 플래그
    public static List<PhotoPost> list;
    private ActivityResultLauncher<PickVisualMediaRequest> pickVisualMediaActivityResultLauncher;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photoboard, container, false);
        /* 추가) 요리 사진 게시판 */
        //cplf.lookupPhotoList(1);

        /* #17 특정 요리 사진 게시글 */
        //cpf.lookupPhoto(1);

        /* #18 요리 사진 게시글 등록 */
        //PhotoPost newPhoto = new PhotoPost("test", 0, "test_link", 0, null);
        //cpf.addPhoto(newPhoto);

        /* #19 요리 사진 게시글 삭제 */
        //cpf.deletePhoto("test", 9);

        /* #20 요리 사진 게시글 정렬 */
        //cplf.sortPhotoList("좋아요 순", 1);

        /* #21 요리 사진 게시글 "좋아요" 취소 */
        //clf.unLikePost("test", -1, 1);

        /* #21 요리 사진 게시글 "좋아요" 등록 */
        //clf.likePost("test", -1, 1);

        /* #22 요리 사진 게시글 신고 */
        //Report reportInfo = new Report("test", "android test3", 1, 2);
        //crf.reportPost(reportInfo);

        responseCode = 0;

        // UI controller
        PhotoList_UI plu = new PhotoList_UI();

        // 사용할 컴포넌트 초기화
        FloatingActionButton fab = view.findViewById(R.id.photo_add_button);
        fab.setOnClickListener(new FABClickListener());
        // 추가) 요리 사진 게시판

        threadFlag = true;


//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (responseCode == 200) {
//                    responseCode = -1;
//
//                    if (list != null)
//                        plu.startToast(list.toString());
//
//                    // UI 코드 작성해주세요
//
//                } else if (responseCode == 500) {
//                    plu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
//                } else if (responseCode == 502) {
//                    plu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
//                }
//            }
//        };
//
//        class NewRunnable implements Runnable {
//            @Override
//            public void run() {
//                for (int i = 0; i < 30; i++) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    if (threadFlag)
//                        getActivity().runOnUiThread(runnable);
//                    else {
//                        i = 30;
//                    }
//                }
//            }
//        }
//
//        cplf.lookupPhotoList(1); // 시작은 첫 페이지 고정
//
//        NewRunnable nr = new NewRunnable();
//        Thread t = new Thread(nr);
//        t.start();

        // #20 요리 사진 게시글 정렬
        // 임시로 플로팅 버튼에다가 걸어 테스트해놨습니다. 나중에 스피너에 대체해서 걸어주세요
        /*
        addPhotoBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (responseCode == 200) {
                            responseCode = -2;

                            if (list != null)
                                plu.startToast(list.toString());

                            // UI 코드 작성해주세요

                        } else if (responseCode == 500) {
                            plu.startDialog(0, "서버 오류", "서버 연결에 실패하였습니다.", new ArrayList<>(Arrays.asList("확인")));
                        } else if (responseCode == 502) {
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

                            getActivity().runOnUiThread(runnable);
                        }
                    }
                }

                // responseCode가 -1일 때, 즉 최초 사진 불러오기가 있고난 다음에야 가능함
                if (responseCode == -1) {
                    responseCode = -2;

                    cplf.sortPhotoList("좋아요 순", 1); // 정렬기준, 페이지
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();

            }
        });
        */

        addPhotoBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent new Intent(getActivity(), )
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadFlag = false;
    }

    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            FAB Click 이벤트 처리 구간
            ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                    registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                        // Callback is invoked after the user selects a media item or closes the
                        // photo picker.
                        if (uri != null) {
                            Log.d("PhotoPicker", "Selected URI: " + uri);
                        } else {
                            Log.d("PhotoPicker", "No media selected");
                        }
                    });
            pickMedia.launch(new PickVisualMediaRequest());
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
