package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.healthcare.chat.ChatFragment;
import com.example.healthcare.fragment.MypageFragment;
import com.example.healthcare.home.HomeFragment;
import com.example.healthcare.matching.PeopleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        BottomNavigationView bottomNavigationView = findViewById(R.id.startactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.startactivity_framelayout,new HomeFragment()).commit();
                        return true;
                    case R.id.action_people:
                        getSupportFragmentManager().beginTransaction().replace(R.id.startactivity_framelayout,new PeopleFragment()).commit();
                        return true;
                    case R.id.action_chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.startactivity_framelayout,new ChatFragment()).commit();
                        return true;
                    case R.id.action_My:
                        getSupportFragmentManager().beginTransaction().replace(R.id.startactivity_framelayout,new MypageFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }
}