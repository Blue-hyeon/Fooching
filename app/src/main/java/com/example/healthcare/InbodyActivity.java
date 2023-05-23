package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.healthcare.model.OCRModel;
import com.example.healthcare.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InbodyActivity extends AppCompatActivity {
    private EditText Abdominal_fat_percentage_info;
    private EditText BMI_info;
    private EditText muscle_info;
    private EditText protein_info;
    private EditText kidney_info;
    private EditText weight_info;
    private EditText body_water_info;
    private EditText body_fat_info;
    private Button Inbody_button;
    private String destinationUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbody);
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
        destinationUid = getIntent().getStringExtra("destinationUid");
        FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).child("ocrinfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                OCRModel ocrModel = dataSnapshot.getValue(OCRModel.class);
                if(ocrModel.getAbdominal_fat_percentage()!=null) {
                    Abdominal_fat_percentage_info.setText(ocrModel.getAbdominal_fat_percentage());
                    BMI_info.setText(ocrModel.getBMI());
                    muscle_info.setText(ocrModel.getmuscle());
                    protein_info.setText(ocrModel.getprotein());
                    kidney_info.setText(ocrModel.getkidney());
                    weight_info.setText(ocrModel.getweight());
                    body_water_info.setText(ocrModel.getbody_water());
                    body_fat_info.setText(ocrModel.getbody_fat());
                }
                else{
                    Abdominal_fat_percentage_info.setText("");
                    BMI_info.setText("");
                    muscle_info.setText("");
                    protein_info.setText("");
                    kidney_info.setText("");
                    weight_info.setText("");
                    body_water_info.setText("");
                    body_fat_info.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}