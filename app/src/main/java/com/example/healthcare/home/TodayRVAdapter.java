package com.example.healthcare.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.R;

import java.util.ArrayList;

public class TodayRVAdapter extends RecyclerView.Adapter<TodayRVAdapter.CustomViewHolder> {

    private ArrayList<String> arrayList;
    private Context context; // 선택한 액티비티에 대한 context를 가져올때 필요

    // Alt + Ins 키 누르고 Instructor
    public TodayRVAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    // list View가 Adapter에 연결 돼었을때 뷰 홀더를 만들어 냄
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_exercise, parent, false); // 뷰 연결 완료
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.exercise_kind.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        // 삼향 연산자 (if문과 비슷)
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView exercise_kind;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.exercise_kind = itemView.findViewById(R.id.today_kind_tv);
        }
    }
}

