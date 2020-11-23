package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLogTags;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
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

        Event event = (Event) getIntent().getSerializableExtra("event");

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), EventDetailFragment.newInstance(event))
                .commit();
    }
}