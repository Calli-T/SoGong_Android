package com.example.sogong.View;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sogong.Control.ControlRefrigerator_f;
import com.example.sogong.Model.Recipe_Ingredients;
import com.example.sogong.Model.Refrigerator;
import com.example.sogong.R;

import java.text.ParseException;
import java.util.List;

public class Refri_Ingre_Adapter extends RecyclerView.Adapter<Refri_Ingre_Adapter.ViewHolder> {

    private List<Refrigerator> mData = null;
    private Context context;
    ControlRefrigerator_f crf = new ControlRefrigerator_f();

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView amount;
        ImageButton left_btn;
        ImageButton right_btn;
        ConstraintLayout background;

        ViewHolder(View itemView) {
            super(itemView);
            // 뷰 객체에 대한 참조. (hold strong reference)
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            amount = (TextView) itemView.findViewById(R.id.amount);
            left_btn = (ImageButton) itemView.findViewById(R.id.left_button);
            right_btn = (ImageButton) itemView.findViewById(R.id.right_button);
            background = (ConstraintLayout) itemView.findViewById(R.id.ingredient_background);
            left_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemLeftButtonClickListener.onItemLeftButtonClick(v, position);
                        Log.d("adapter", "leftbuttonclick");
                    }
                }
            });

            right_btn.setOnClickListener(new View.OnClickListener() {
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

        void onBind(Refrigerator refrigerator) throws ParseException {
            if (refrigerator.getExpiry_date() != null) {
                name.setText(refrigerator.getName());
                date.setText(refrigerator.getExpiry_date().split("T")[0]);
                String amountstr = String.valueOf(refrigerator.getAmmount()) + refrigerator.getUnit();
                amount.setText(amountstr);
                long remainDate = crf.expireDateWarning(refrigerator.getExpiry_date());
                if (remainDate > 7) {
                    background.setBackgroundColor(context.getResources().getColor(R.color.pastel_green));
                } else if (remainDate > 3){
                    background.setBackgroundColor(context.getResources().getColor(R.color.pastel_yellow));
                }else if (remainDate >= 0){
                    background.setBackgroundColor(context.getResources().getColor(R.color.pastel_red));
                } else if (remainDate<0){
                    background.setBackgroundColor(context.getResources().getColor(com.google.maps.android.rx.places.R.color.quantum_grey));
                }
            }
        }

        public TextView getName() {
            return name;
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public Refri_Ingre_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_ingredient, parent, false);
        Refri_Ingre_Adapter.ViewHolder vh = new Refri_Ingre_Adapter.ViewHolder(view);

        //리사이클러 클릭 이벤트
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                int position = vh.getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    data = vh.getName().getText().toString();
                }
                itemClickListener.onItemClicked(position, data);

            }
        });

        return vh;
    }

    public void setRefriIngreList(List<Refrigerator> list) {
        this.mData = list;
        notifyDataSetChanged();

    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(Refri_Ingre_Adapter.ViewHolder holder, int position) {
        try {
            holder.onBind(mData.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
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
    public interface OnItemLeftButtonClickListener {
        void onItemLeftButtonClick(View view, int position);
    }

    private Refri_Ingre_Adapter.OnItemLeftButtonClickListener onItemLeftButtonClickListener;

    public void setOnItemLeftButtonClickListener(Refri_Ingre_Adapter.OnItemLeftButtonClickListener listener) {
        onItemLeftButtonClickListener = listener;
    }

    //오른쪽버튼 클릭 리스너
    public interface OnItemRightButtonClickListener {
        void onItemRightButtonClick(View view, int position);
    }

    private Refri_Ingre_Adapter.OnItemRightButtonClickListener onItemRightButtonClickListener;

    public void setOnItemRightButtonClickListener(Refri_Ingre_Adapter.OnItemRightButtonClickListener listener) {
        onItemRightButtonClickListener = listener;
    }

}