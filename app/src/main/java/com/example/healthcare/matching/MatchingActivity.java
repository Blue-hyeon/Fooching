package com.example.healthcare.matching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcare.R;
import com.example.healthcare.chat.ChatFragment;
import com.example.healthcare.chat.MessageActivity;
import com.example.healthcare.model.ChatModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchingActivity extends AppCompatActivity {
    private String destinationUid;
    private Button button;
    private EditText editText;
    private String uid;
    private String chatRoomUid;
    private RecyclerView recyclerView;
    TextView textbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destinationUid = getIntent().getStringExtra("destinationUid");
        textbutton = (TextView) findViewById(R.id.matchingBtn);
        textbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid, true);
                chatModel.users.put(destinationUid, true);
                if (chatRoomUid == null) {
                    textbutton.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                            finish();

                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"이미 매칭이 된 상태입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkChatRoom();
    }
    void checkChatRoom() {

//        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue() == null){
//                    ChatModel newRoom = new ChatModel();
//                    newRoom.users.put(uid, true);
//                    newRoom.users.put(destinationUid, true);
//                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(newRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            checkChatRoom();
//                        }
//                    });
//                    return;
//                }
//
//                for (DataSnapshot item : dataSnapshot.getChildren()) {
//                    ChatModel chatModel = item.getValue(ChatModel.class);
//                    if (chatModel.users.containsKey(destinationUid) && chatModel.users.size() == 2) {
//                        chatRoomUid = item.getKey();
//                        textbutton.setEnabled(true);
//                    }
//                }
//            }
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    ChatModel  chatModel = item.getValue(ChatModel.class);
                    if(chatModel.users.containsKey(destinationUid)){
                        chatRoomUid = item.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}