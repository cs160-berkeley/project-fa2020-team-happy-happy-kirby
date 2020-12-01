package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.event_detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_bottom_fake, R.anim.pull_out_to_bottom);
    }
}