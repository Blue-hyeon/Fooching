package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CalenderActivity extends AppCompatActivity {
    private String destinationUid;
    private String uid;
    private CalendarView calendarView;
    private EditText editText;
    private String stringDateSelected;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destinationUid = getIntent().getStringExtra("destinationUid");
        calendarView = findViewById(R.id.calendarView);
        editText = findViewById(R.id.editText);
        if(calendarView!=null) {
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                    stringDateSelected = Integer.toString(i) + Integer.toString(i1 + 1) + Integer.toString(i2);
                    calendarClicked();
                }
            });
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }
    //저장된 운동정보를 불러옵니다.
    private void calendarClicked(){
        databaseReference.child("users").child(destinationUid).child("Calendar").child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    editText.setText(snapshot.getValue().toString());
                }else {
                    editText.setText("null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //save 버튼을 통해서 운동정보를 설정된 날짜에 저장합니다. 정보는 users 테이블에 Calendar의 년(4자리)월(0없이)일(0없이) 형태의 key정보로 저장됩니다.
    public void buttonSaveEvent(View view){
        databaseReference.child("users").child(destinationUid).child("Calendar").child(stringDateSelected).setValue(editText.getText().toString());
    }
}