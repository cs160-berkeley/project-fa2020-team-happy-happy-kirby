package com.gmail.kingarthuralagao.us.civicengagement.presentation.accessibility;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.kingarthuralagao.us.civicengagement.presentation.LoadingDialog;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityAccessibilityBinding;

public class AccessibilityActivity extends AppCompatActivity {


    private ActivityAccessibilityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccessibilityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), AccessibilityFragment.newInstance())
                .commit();*/

        /*
        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), EventsViewFragment.newInstance("Engage me at 1234 Main Street"))
                .commit();*/

        new LoadingDialog().show(getSupportFragmentManager(), "");
    }
}