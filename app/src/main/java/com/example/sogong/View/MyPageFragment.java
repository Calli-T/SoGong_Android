package com.example.sogong.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sogong.Control.Control;
import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Control.ControlLogout_f;
import com.example.sogong.Control.ControlMyPhoto_f;
import com.example.sogong.Control.ControlMyRecipe_f;
import com.example.sogong.Control.ControlPost_f;
import com.example.sogong.Control.ControlRefrigerator_f;
import com.example.sogong.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPageFragment extends Fragment {
    Button pwdchange_text, nicknamechange_text, logout_text;
    Button writtenRecipe, likedRecipe, commentRecipe, refrigerator, mailbox;
    ViewGroup rootview;

    ControlRefrigerator_f crf = new ControlRefrigerator_f();
    ControlMyPhoto_f cmpf = new ControlMyPhoto_f();
    ControlMyRecipe_f cmrf = new ControlMyRecipe_f();
    ControlPost_f cpf = new ControlPost_f();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 프래그먼트에 버튼 두려면 return에 ViewGroup을 바로 박아버리면 안됨
        rootview = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);
        writtenRecipe = (Button) rootview.findViewById(R.id.writtenrecipe_text);
        likedRecipe = (Button) rootview.findViewById(R.id.likedrecipe_text);
        commentRecipe = (Button) rootview.findViewById(R.id.commentrecipe_text);
        refrigerator = (Button) rootview.findViewById(R.id.refrigerator_text);
        mailbox = (Button) rootview.findViewById(R.id.mailbox_text);
        pwdchange_text = (Button) rootview.findViewById(R.id.pwdchange_text);
        nicknamechange_text = (Button) rootview.findViewById(R.id.nicknamechange_text);
        logout_text = (Button) rootview.findViewById(R.id.logout_text);

        MyPage_UI mu = new MyPage_UI();
        writtenRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mu.startDialog(1, "게시글 종류", "보실 게시글 종류를 선택하세요.", new ArrayList<>(Arrays.asList("레시피", "사진")));
                class NewRunnable implements Runnable {
                    NewRunnable() {
                    }
                    @Override
                    public void run() {
                        while(true){
                            try {
                                Thread.sleep(100);
                                if (Custom_Dialog.state == 0) {
                                    Custom_Dialog.state = -1;
                                    /* #12 사용자 작성 레시피 조회 */
                                    cmrf.lookupMyRecipeList(ControlLogin_f.userinfo.getNickname());
                                    Log.d("mypagefragment", "게시글? state = " + Custom_Dialog.state);
                                    Intent intent = new Intent(getActivity(), MyPageBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra("post_type",0);
                                    startActivity(intent);
                                    break;
                                } else if (Custom_Dialog.state == 1) {
                                    Log.d("mypagefragment", "사진? state = " + Custom_Dialog.state);
                                    /* #11 사용자 작성 요리사진 조회 */
                                    //cmpf.lookupMyPhotoList(ControlLogin_f.userinfo.getNickname());
                                    Custom_Dialog.state = -1;
                                    Intent intent = new Intent(getActivity(), MyPageBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra("post_type",1);
                                    startActivity(intent);
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            }
        });

        likedRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* #15 "좋아요"를 누른 게시글 리스트 조회 */
                //cpf.lookupMyLikeList("test", 1);
            }
        });

        commentRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* #16 댓글을 작성한 게시글들의 리스트 조회 */
                //cpf.lookupMyCommentList("test");
            }
        });
        refrigerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RefrigeratorActivity.class);
                startActivity(intent);
            }
        });
        mailbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MailBoxActivity.class);
                startActivity(intent);            }
        });

        //비밀번호 변경 버튼 리스너
        pwdchange_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mu.startToast("Test1");
                EmailVerificationActivity.destination = 1; // 이메일 인증 Activity의 분기 결정 Flag

                Intent intent = new Intent(getActivity(), EmailVerificationActivity.class);
                startActivity(intent);
            }
        });

        //닉네임 변경 버튼 리스너
        nicknamechange_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeNicknameActivity.class);
                startActivity(intent);
            }
        });
        // 프래그먼트에서 구현한 로그아웃 및 api call 로직
        logout_text.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.responseCode == 200) {
                            MainActivity.responseCode = -1;
                            mu.startToast("로그아웃");
                            mu.changePage(0);
//                            getActivity().finish();

                        } else if (MainActivity.responseCode == 500) {
                            MainActivity.responseCode = 0;
                            mu.startToast("자동 로그인 해제를 실패했습니다.");
                        } else if (MainActivity.responseCode == 502) {
                            MainActivity.responseCode = 0;
                            mu.startDialog(0, "서버 오류", "알 수 없는 오류입니다.", new ArrayList<>(Arrays.asList("확인")));
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

                if (MainActivity.responseCode == 0) {
                    MainActivity.responseCode = -1;

                    ControlLogout_f clf = new ControlLogout_f();
                    clf.logout();
                }

                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
            }
        });

        /* #6 사용자 보유 재료 조회 */
        //crf.lookupRefrigerator("test");

        /* #7 사용자 보유 재료 추가 */
        //Refrigerator ingredients = new Refrigerator(0, "yangpa", "test", "Kg", 1, "2022-11-11");
        //crf.addRefrigerator(ingredients);

        /* #8 사용자 보유 재료 삭제 */
        //crf.deleteRefrigerator(15);

        /* #9 사용자 보유 재료 수정 */
        //Refrigerator ingredients = new Refrigerator(0, "yangpa", "test", "Kg", 1, "2022-11-11");
        //crf.editRefrigerator(ingredients);

        /* #10 유통기한 표시 */
        // -> 시각적 표시라 따로 DB와의 처리는 없음

        /* #11 사용자 작성 요리사진 조회 */
        //cmpf.lookupMyPhotoList("test");

        /* #12 사용자 작성 레시피 조회 */
        //cmrf.lookupMyRecipeList("test");

        /* #13 사용자 작성 레시피 검색 */
        /*SearchInfo searchInfo = new SearchInfo();
        searchInfo.setNickname("test");
        searchInfo.setKeyword("test");
        cmrf.searchMyRecipeList(searchInfo);*/

        /* #14 사용자 작성 레시피 정렬 */
        /*SortInfo sortInfo = new SortInfo();
        sortInfo.setNickname("test");
        sortInfo.setArrangeBy("좋아요 순");
        cmrf.sortMyRecipeList(sortInfo);*/

        /* #15 "좋아요"를 누른 게시글 리스트 조회 */
        //cpf.lookupMyLikeList("test", 1);

        /* #16 댓글을 작성한 게시글들의 리스트 조회 */
        //cpf.lookupMyCommentList("test");

        return rootview;
    }

    private class stateThread extends Thread {
        private static final String TAG = "ExampleThread";

        public stateThread() {

        }

        public void run() {

        }

    }

    class MyPage_UI implements Control {
        int state = -1;

        @Override
        public void startToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, rootview.findViewById(R.id.toast_layout));
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
            Custom_Dialog cd = new Custom_Dialog(getActivity());


            cd.callFunction(title, message, type, btnTxtList);


        }

        @Override
        public void changePage(int dest) {
            if (dest == 0) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }
    }
}

/*
Intent intent = new Intent(getActivity(), Ready.class);
                intent.putExtra("exerciseCode", 3);

                startActivity(intent);
 */
