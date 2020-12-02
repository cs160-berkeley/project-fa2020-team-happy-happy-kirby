package com.gmail.kingarthuralagao.us.civicengagement.presentation.virtual;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityEngageVirtuallyBinding;

public class EngageVirtuallyActivity extends AppCompatActivity {

    ActivityEngageVirtuallyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEngageVirtuallyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}