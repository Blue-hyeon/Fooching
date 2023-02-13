package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthcare.login.LoginActivity;
import com.example.healthcare.login.SignupActivity;
import com.example.healthcare.model.TrainerModel;
import com.example.healthcare.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainerInfoActivity extends AppCompatActivity {
    private EditText trainer_License;
    private EditText trainer_Location;
    private EditText trainer_Award;
    private EditText trainer_Introduce;
    private Button trainer_Info_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_info);
        trainer_License=findViewById(R.id.trainer_License);
        trainer_Location=findViewById(R.id.trainer_Location);
        trainer_Award=findViewById(R.id.trainer_Award);
        trainer_Introduce=findViewById(R.id.trainer_Introduce);
        trainer_Info_button=findViewById(R.id.trainer_Info_button);
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("33333333",myUid);
        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("uid").equalTo(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserModel> userModels = new ArrayList<>();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    userModels.add(userModel);

                }
                Log.e("33333333", String.valueOf(userModels.size()));
                UserModel updateuserModel = new UserModel();
                if(userModels.get(0).trainerLocation!=null) {
                    trainer_Location.setText(userModels.get(0).trainerLocation);
                }
                if(userModels.get(0).trainerLicense!=null) {
                    trainer_License.setText(userModels.get(0).trainerLicense);
                }
                if(userModels.get(0).trainerAward!=null) {
                    trainer_Award.setText(userModels.get(0).trainerAward);
                }
                if(userModels.get(0).trainerIntroduce!=null) {
                    trainer_Introduce.setText(userModels.get(0).trainerIntroduce);
                }


//                HashMap<String,Object> hashMap =new HashMap<>();
//                hashMap.put("userName",userModels.get(0).userName);
//                hashMap.put("profileImageUrl",userModels.get(0).profileImageUrl);
//                hashMap.put("uid",userModels.get(0).uid);
//                hashMap.put("height",userModels.get(0).height);
//                hashMap.put("weight",userModels.get(0).weight);
//                hashMap.put("level",userModels.get(0).level);
//                hashMap.put("comment",userModels.get(0).comment);
//                hashMap.put("trainerLocation",trainer_Location.getText().toString());
//                hashMap.put("trainerLicense",trainer_License.getText().toString());
//                hashMap.put("trainerAward",trainer_Award.getText().toString());
//                hashMap.put("trainerIntroduce",trainer_Introduce.getText().toString());
                trainer_Info_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateuserModel.comment=userModels.get(0).comment;
                        updateuserModel.userName = userModels.get(0).userName;
                        updateuserModel.weight=userModels.get(0).weight;
                        updateuserModel.height=userModels.get(0).height;
                        updateuserModel.level=userModels.get(0).level;
                        updateuserModel.profileImageUrl=userModels.get(0).profileImageUrl;
                        updateuserModel.uid=userModels.get(0).uid;
                        updateuserModel.trainerLocation=trainer_Location.getText().toString();
                        updateuserModel.trainerLicense=trainer_License.getText().toString();
                        updateuserModel.trainerAward=trainer_Award.getText().toString();
                        updateuserModel.trainerIntroduce=trainer_Introduce.getText().toString();
                        FirebaseDatabase.getInstance().getReference().child("users").child(userModels.get(0).uid).setValue(updateuserModel).addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void aVoid) {
                                //SignupActivity.this.finish();
                                Log.e("33333333","수정되었습니다.");
                            }
                        });
                    }
                });






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}