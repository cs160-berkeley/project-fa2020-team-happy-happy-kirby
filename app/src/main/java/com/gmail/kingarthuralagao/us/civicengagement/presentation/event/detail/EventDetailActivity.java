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

        Event event4 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), EventDetailFragment.newInstance(event4))
                .commit();
    }
}