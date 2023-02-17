package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.healthcare.model.OCRModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.IOException;

import retrofit2.Retrofit;

public class UploadOcrActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int IMG_REQUEST =  1;

    private Retrofit retrofit;
    private APIInterface service;

    private String BMI;
    private String muscle;
    private String basal_metabolic;
    private String protein;
    private String kidney;
    private String weight;
    private String body_water;
    private String body_fat;
    //  Select image from gallery: ImageView
    ImageView imageToUpload;

    //  Image upload: Button
    Button uploadBtn,btn_get;

    //  Variable to store gallery image path: String
    String profileImgPath,ImgPath;

    //  Converting gallery image to bitmap to set it to imageview
    Bitmap bitmap;

    //  Alert box
    private AlertDialog.Builder builder;

    //  SERVER URL

    @Override
    protected void onStart() {
        getPermissions();

        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ocr);
        initVars();
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btn_get = (Button) findViewById(R.id.btn_get);
//      image view on click listener
        imageToUpload.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMG_REQUEST);
        });

//      upload btn on click listener
        uploadBtn.setOnClickListener(v-> {

            if(isEmpty(profileImgPath)) {
                //Toast.makeText(this,profileImgPath,Toast.LENGTH_LONG).show();
                displayAlert("Please select a profile picture");
                return;
            }
            Log.e("3333333", String.valueOf(profileImgPath.lastIndexOf("/")+1));
            ImgPath = profileImgPath.substring(profileImgPath.lastIndexOf("/")+1);
            ImgPath=ImgPath.replace(".","");
            Ion.with(this)
                    .load(UPLOAD_URL)
                    .setMultipartFile("image", "image/jpeg", new File(profileImgPath))
                    .asJsonObject()
                    .withResponse()
                    .setCallback((e, result) -> {
                        Toast.makeText(this,profileImgPath,Toast.LENGTH_LONG).show();

                        if(e != null) { ;
                            Toast.makeText(this, "Error is: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            switch (result.getHeaders().code()) {
                                case 500:
                                    Toast.makeText(this, "Image Uploading Failed. Unknown Server Error!", Toast.LENGTH_SHORT).show();
                                    break;
                                case 200:
                                    Toast.makeText(this, "Image Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                                            R.drawable.baseline_add_to_photos_24);
                                    imageToUpload.setImageBitmap(bitmap);
                                    profileImgPath = null;
                                    break;
                            }
                        }

                    });

        });
        btn_get.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.e("4444444444444",ImgPath);
                FirebaseDatabase.getInstance().getReference().child("OCR").child(ImgPath).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        OCRModel group = dataSnapshot.getValue(OCRModel.class);
                        BMI = group.getBMI();
                        muscle = group.getmuscle();
                        Log.e("66666666666","무게"+muscle);
                        basal_metabolic = group.getbasal_metabolic();
                        protein=group.getprotein();
                        kidney=group.getkidney();
                        weight=group.getweight();
                        body_water=group.getbody_water();
                        body_fat=group.getbody_fat();
                        FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("ocrinfo").setValue(group).addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void aVoid) {
                                //SignupActivity.this.finish();
                                Log.e("33333333","수정되었습니다.");
                            }
                        });
                        Toast.makeText(UploadOcrActivity.this,"BMI : "+BMI+" 골격근량 : "+muscle+" 기초대사량 : "+basal_metabolic+" 단백질 : "+protein+" 신장 : "+kidney+" 체중 : "+weight,Toast.LENGTH_LONG).show();
//                        String value = dataSnapshot.getValue(String.class);
//                        Log.e("5555555555",value);
//                        Toast.makeText(UploadOcrActivity.this,value,Toast.LENGTH_LONG).show();
//                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                            String name = ds.child("name").getValue(String.class);
//                            Toast.makeText(UploadActivity.this,name,Toast.LENGTH_LONG).show();
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });
                break;
            default:
                break;
        }
    }

    private void initVars() {
        builder = new AlertDialog.Builder(this);
        imageToUpload = findViewById(R.id.imagetoupload);
        uploadBtn = findViewById(R.id.uploadbtn);
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            if( requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
                Uri path = data.getData();
                if(path != null) {
                    profileImgPath = FetchPath.getPath(this, path);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                        imageToUpload.setImageBitmap(getCroppedBitmap(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static boolean isEmpty(String field) {

        return field == null || field.isEmpty();
    }

    public void displayAlert(String message) {

        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}