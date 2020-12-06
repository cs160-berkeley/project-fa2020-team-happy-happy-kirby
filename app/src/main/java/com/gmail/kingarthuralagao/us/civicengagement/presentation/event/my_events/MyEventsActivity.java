package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.my_events;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityMyEventsBinding;

public class MyEventsActivity extends AppCompatActivity {

    ActivityMyEventsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), MyEventsFragment.newInstance(), "")
        .commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}