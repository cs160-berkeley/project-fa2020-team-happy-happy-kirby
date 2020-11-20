package com.gmail.kingarthuralagao.us.civicengagement.presentation.accessibility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.list_event.EventsViewFragment;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityAccessibilityBinding;

public class AccessibilityActivity extends AppCompatActivity {


    private ActivityAccessibilityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccessibilityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), AccessibilityFragment.newInstance())
                .commit();*/

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), EventsViewFragment.newInstance("Engage me at 1234 Main Street"))
                .commit();
    }
}