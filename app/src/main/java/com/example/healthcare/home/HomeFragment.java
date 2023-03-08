package com.example.healthcare.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.healthcare.R;
import com.example.healthcare.SettingActivity;
import com.example.healthcare.TrainerInfoActivity;
import com.example.healthcare.UploadActivity;
import com.example.healthcare.model.CalorieModel;
import com.example.healthcare.model.OCRModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    FloatingActionButton floatingTrainerInfoButton;
    ImageButton button;
    TextView today_carb_cal;
    SimpleDateFormat simpleDate;
    long now;
    Date mDate;
    String getTime;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.homefragment_floatingButton);
        floatingTrainerInfoButton = (FloatingActionButton)view.findViewById(R.id.homefragment_trainerInfoButton);
        button = (ImageButton)view.findViewById(R.id.setting_button);
        today_carb_cal=(TextView)view.findViewById(R.id.home_today_carb_cal_tv);
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        simpleDate = new SimpleDateFormat("yyyyMd");
        now = System.currentTimeMillis();
        mDate = new Date(now);
        getTime = simpleDate.format(mDate);
        Log.e("33333333",getTime);
        FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("Calendar").child(getTime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                String[] results = value.split(", ");
                for (int i = 0; i < results.length; i++) {
                    Log.e("3333333","results[" +i + "] = " + results[i]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                today_carb_cal.setText("0");
            }
        });
        //users테이블에 자기정보에 저장된 calinfo정보에 데이터가 들어가 있으면 가져오고 아니면 0으로 설정합니다.
        FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("calinfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CalorieModel today_cal = dataSnapshot.getValue(CalorieModel.class);
                if(today_cal != null) {
                    today_carb_cal.setText(today_cal.getcalorie());
                }
                else{
                    today_carb_cal.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                today_carb_cal.setText("0");
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), UploadActivity.class));
            }
        });

        //트레이너 정보 기입창으로 이동하는 floatingButton
        floatingTrainerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), TrainerInfoActivity.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), SettingActivity.class));
            }
        });
        return view;
    }
}