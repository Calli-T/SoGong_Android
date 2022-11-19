package com.example.sogong.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sogong.Control.ControlLike_f;
import com.example.sogong.Control.ControlPhotoList_f;
import com.example.sogong.Control.ControlPhoto_f;
import com.example.sogong.Control.ControlReport_f;
import com.example.sogong.Model.PhotoPost;
import com.example.sogong.Model.Report;
import com.example.sogong.Model.SortInfo;
import com.example.sogong.R;

public class PhotoFragment extends Fragment {
    ControlPhotoList_f cplf = new ControlPhotoList_f();
    ControlPhoto_f cpf = new ControlPhoto_f();
    ControlLike_f clf = new ControlLike_f();
    ControlReport_f crf = new ControlReport_f();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_photoboard,container,false);
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

        return view;
    }
}
