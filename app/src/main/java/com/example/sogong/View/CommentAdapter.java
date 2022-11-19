package com.example.sogong.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.ControlLogin_f;
import com.example.sogong.Model.Comment;
import com.example.sogong.R;

import java.util.List;
import java.util.Objects;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> mData;
    private Context context;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView comment1;
        TextView date;
        ImageButton leftbutton;
        ImageButton rightbutton;

        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)

            author = (TextView) itemView.findViewById(R.id.author);
            comment1 = (TextView) itemView.findViewById(R.id.comment);
            date = (TextView) itemView.findViewById(R.id.date);
            leftbutton = (ImageButton) itemView.findViewById(R.id.left_button);
            rightbutton = (ImageButton) itemView.findViewById(R.id.right_button);

            leftbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemLeftButtonClickListener.onItemLeftButtonClick(v, position);
                        Log.d("adapter", "leftbuttonclick");
                    }
                }
            });

            rightbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemRightButtonClickListener.onItemRightButtonClick(v, position);
                        Log.d("adapter", "rightbuttonclick");
                    }
                }
            });


        }

        void onBind(Comment comment) {
            if (Objects.equals(ControlLogin_f.userinfo.getNickname(), comment.getNickname())) {
                author.setText(comment.getNickname());
                comment1.setText(comment.getComments());
                String time = comment.getComment_time();
                String[] time1 = time.split("T");
                date.setText(time1[0]);
            } else {
                author.setText(comment.getNickname());
                comment1.setText(comment.getComments());
                String time = comment.getComment_time();
                String[] time1 = time.split("T");
                date.setText(time1[0]);
                leftbutton.setVisibility(View.INVISIBLE);
            }

        }

        public TextView getAuthor() {
            return author;
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_commentlist, parent, false);
        CommentAdapter.ViewHolder vh = new CommentAdapter.ViewHolder(view);

        //리사이클러 클릭 이벤트
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String data = "";
//                int position = vh.getAbsoluteAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    data = vh.getAuthor().getText().toString();
//                }
//                itemClickListener.onItemClicked(position, data);
//
//            }
//        });

        return vh;
    }

    public void setCommentList(List<Comment> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position));
        //사용자의 따라 적절한 아이콘 설정
        if (!Objects.equals(ControlLogin_f.userinfo.getNickname(), mData.get(position).getNickname())) {
            holder.rightbutton.setBackground(ContextCompat.getDrawable(context, R.drawable.report));
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        else return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClicked(int position, String data);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    //왼쪽버튼 클릭 리스너
    public interface OnItemLeftButtonClickListener{
        void onItemLeftButtonClick(View view,int position);
    }
    private OnItemLeftButtonClickListener onItemLeftButtonClickListener;

    public void setOnItemLeftButtonClickListener(OnItemLeftButtonClickListener listener){
        onItemLeftButtonClickListener = listener;
    }
    //오른쪽버튼 클릭 리스너
    public interface OnItemRightButtonClickListener{
        void onItemRightButtonClick(View view,int position);
    }
    private OnItemRightButtonClickListener onItemRightButtonClickListener;

    public void setOnItemRightButtonClickListener(OnItemRightButtonClickListener listener){
        onItemRightButtonClickListener = listener;
    }
}