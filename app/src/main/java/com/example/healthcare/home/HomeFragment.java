package com.example.healthcare.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.healthcare.R;
import com.example.healthcare.SettingActivity;
import com.example.healthcare.TrainerInfoActivity;
import com.example.healthcare.UploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    FloatingActionButton floatingTrainerInfoButton;
    ImageButton button;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.homefragment_floatingButton);
        floatingTrainerInfoButton = (FloatingActionButton)view.findViewById(R.id.homefragment_trainerInfoButton);
        button = (ImageButton)view.findViewById(R.id.setting_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), UploadActivity.class));
            }
        });
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