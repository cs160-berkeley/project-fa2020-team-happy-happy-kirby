package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEventDescriptionBinding;

public class EventDescriptionFragment extends Fragment {

    public static EventDescriptionFragment newInstance(String desc) {
        EventDescriptionFragment fragment = new EventDescriptionFragment();

        Bundle args = new Bundle();
        args.putString(KEY, desc);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String KEY = "description";
    private String description = "";
    private FragmentEventDescriptionBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        description = getArguments().getString(KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventDescriptionBinding.inflate(inflater, container, false);
        if (description.length() != 0) {
            binding.eventDescriptionTv.setText(description);
        }

        return binding.getRoot();
    }
}
