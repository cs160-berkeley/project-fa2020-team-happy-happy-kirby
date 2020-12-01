package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityEventsViewBinding;

import java.util.List;

public class EventsViewActivity extends AppCompatActivity {

    private ActivityEventsViewBinding binding;
    private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventsViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        address = getIntent().getStringExtra("Address");
        initializeFragment();
    }

    private void initializeFragment() {
        EventsViewFragment fragment = EventsViewFragment.newInstance(address);

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), fragment)
                .commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}