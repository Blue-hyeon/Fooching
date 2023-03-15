package com.example.healthcare.login;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.healthcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.example.healthcare.model.UserModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class SignupActivity extends AppCompatActivity {
    private static final int PICK_FROM_ALBAM = 10;
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText weight;
    private EditText height;
    private Button signup;
    private int levelCount;
    private ImageView profileImage;
    private Uri uri;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth firebaseAuth;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFirebaseRemoteConfig= FirebaseRemoteConfig.getInstance();
        String loading_background=mFirebaseRemoteConfig.getString("loading_background");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(loading_background));
        }
        firebaseAuth = FirebaseAuth.getInstance();
        email=(EditText) findViewById(R.id.signupactivity_email);
        name=(EditText) findViewById(R.id.signupactivity_name);
        password=(EditText) findViewById(R.id.signupactivity_password);
        weight=(EditText) findViewById(R.id.signupactivity_weight);
        height=(EditText) findViewById(R.id.signupactivity_height);
        signup=(Button) findViewById(R.id.signupactivity_signup_button);
        profileImage=(ImageView) findViewById(R.id.signupactivity_profile);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBAM);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uri==null || email.getText().toString()==null || name.getText().toString()==null || password.getText().toString()==null || height.getText().toString()==null || weight.getText().toString()==null) {
                    return;
                }
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).
                        addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignupActivity.this,"Check your email",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                FirebaseStorage.getInstance().getReference().child("profileImage").child(uid).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                        while (!imageUrl.isComplete());
                                        UserModel userModel = new UserModel();
                                        userModel.userName = name.getText().toString();
                                        userModel.weight=weight.getText().toString();
                                        userModel.height=height.getText().toString();
                                        userModel.level=levelCount;
                                        userModel.profileImageUrl=imageUrl.getResult().toString();
                                        userModel.uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>(){
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //SignupActivity.this.finish();
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            }
                                        });
                                    }
                                });


                            }
                        });
            }
        });
    }
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.rg_btn1){
                levelCount=1;
            }
            else if(i == R.id.rg_btn2){
                levelCount=0;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBAM && resultCode == RESULT_OK) {
            profileImage.setImageURI(data.getData());
            uri = data.getData();
        }
    }
}