package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityEventsViewBinding;

public class EventsViewActivity extends AppCompatActivity implements FilterDialog.IFilterClickListener {

    private ActivityEventsViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventsViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeFragment();
    }

    private void initializeFragment() {
        EventsViewFragment fragment = EventsViewFragment.newInstance("Engage me at 1234 Main Street");

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), fragment)
                .commit();
    }

    @Override
    public void onApplyClick() {
        //
    }

    @Override
    public void onCloseClick() {
        //
    }
}