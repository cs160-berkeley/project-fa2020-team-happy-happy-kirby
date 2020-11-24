package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Window;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityEventsViewBinding;

public class EventsViewActivity extends AppCompatActivity implements FilterDialog.IFilterClickListener {

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
    public void onApplyClick() {
        //
    }

    @Override
    public void onCloseClick() {
        //
    }
}