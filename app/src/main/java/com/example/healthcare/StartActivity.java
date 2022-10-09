package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.healthcare.fragment.ChatFragment;
import com.example.healthcare.fragment.FoodFragment;
import com.example.healthcare.fragment.PeopleFragment;
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
                    case R.id.action_people:
                        getSupportFragmentManager().beginTransaction().replace(R.id.startactivity_framelayout,new PeopleFragment()).commit();
                        return true;
                    case R.id.action_chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.startactivity_framelayout,new ChatFragment()).commit();
                        return true;
                    case R.id.action_food:
                        getSupportFragmentManager().beginTransaction().replace(R.id.startactivity_framelayout,new FoodFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }
}