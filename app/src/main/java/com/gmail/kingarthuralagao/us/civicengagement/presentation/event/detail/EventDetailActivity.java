package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.gmail.kingarthuralagao.us.civicengagement.presentation.accessibility.AccessibilityActivity;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityEventDetailBinding;

public class EventDetailActivity extends AppCompatActivity {

    public ActivityEventDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpEvents();
    }

    private void setUpEvents() {
        binding.accessibilityBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, AccessibilityActivity.class);
            startActivity(i);
        });
    }
}