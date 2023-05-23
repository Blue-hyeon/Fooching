package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.healthcare.model.OCRModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OcrActivity extends AppCompatActivity {
    private EditText Abdominal_fat_percentage_info;
    private EditText BMI_info;
    private EditText muscle_info;
    private EditText protein_info;
    private EditText kidney_info;
    private EditText weight_info;
    private EditText body_water_info;
    private EditText body_fat_info;
    private Button Inbody_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        OCRModel inbody_info = new OCRModel();
        Abdominal_fat_percentage_info=findViewById(R.id.Abdominal_fat_percentage_info);
        BMI_info=findViewById(R.id.BMI_info);
        muscle_info=findViewById(R.id.muscle_info);
        protein_info=findViewById(R.id.protein_info);
        kidney_info=findViewById(R.id.kidney_info);
        weight_info=findViewById(R.id.weight_info);
        body_water_info=findViewById(R.id.body_water_info);
        body_fat_info=findViewById(R.id.body_fat_info);
        Inbody_button=findViewById(R.id.Inbody_button);
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        OCRModel group=(OCRModel) intent.getSerializableExtra("OCR");
        Abdominal_fat_percentage_info.setText(group.getAbdominal_fat_percentage());
        BMI_info.setText(group.getBMI());
        muscle_info.setText(group.getmuscle());
        protein_info.setText(group.getprotein());
        kidney_info.setText(group.getkidney());
        weight_info.setText(group.getweight());
        body_water_info.setText(group.getbody_water());
        body_fat_info.setText(group.getbody_fat());
        Inbody_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inbody_info.setAbdominal_fat_percentage(Abdominal_fat_percentage_info.getText().toString());
                inbody_info.setBMI(BMI_info.getText().toString());
                inbody_info.setmuscle(muscle_info.getText().toString());
                inbody_info.setprotein(protein_info.getText().toString());
                inbody_info.setkidney(kidney_info.getText().toString());
                inbody_info.setweight(weight_info.getText().toString());
                inbody_info.setbody_water(body_water_info.getText().toString());
                inbody_info.setbody_fat(body_fat_info.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("ocrinfo").setValue(inbody_info).addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        //SignupActivity.this.finish();
                        Log.e("33333333","수정되었습니다.");
                    }
                });
            }
        });
    }
}