package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityAddEventBinding;

public class AddEventActivity extends AppCompatActivity {

    ActivityAddEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), AddEventFragment.newInstance())
                .commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_bottom_fake, R.anim.pull_out_to_bottom);
    }
}