package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.accessibility.AccessibilityFragment;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEventDetailBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.Serializable;
import java.util.ArrayList;

public class EventDetailFragment extends Fragment {

    public static EventDetailFragment newInstance(Event event) {
        EventDetailFragment fragment = new EventDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    private Event event;
    private FragmentEventDetailBinding binding;
    private ArrayList<String> tabTitles;
    private PagerAdapter pagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        event = (Event) getArguments().getSerializable("event");

        tabTitles = new ArrayList<>();
        tabTitles.add("Description");
        tabTitles.add("Accessibility Info");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventDetailBinding.inflate(inflater, container, false);
        initializeViews();
        setViewPager();

        binding.backArrowImg.setOnClickListener(view -> {
            requireActivity().finish();
        });
        return binding.getRoot();
    }

    private void initializeViews() {
        binding.eventName.setText(event.getName());
        binding.includeEventDetails.eventDateTv.setText(event.getDateStart() + " - " + event.getDateEnd());
        binding.includeEventDetails.eventLocationTv.setText(event.getLocation());
        binding.includeEventDetails.eventTimeTv.setText(event.getTimeStart() + " - " + event.getTimeEnd() + " " + event.getTimeZone());
        binding.includeEventDetails.eventCheckInsTv.setText("" + event.getCheckIns());
    }

    private void setViewPager() {
        pagerAdapter = new PagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(tabTitles.get(position));
        }).attach();
    }

    private class PagerAdapter extends FragmentStateAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        public PagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment;
            if (position == 0) {
                fragment = EventDescriptionFragment.newInstance(event.getDescription());
            } else {
                fragment = AccessibilityFragment.newInstance(event.getAccessibilities());
            }
            addFragment(fragment);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return tabTitles.size();
        }

        private void addFragment(Fragment fragment) {
            this.fragments.add(fragment);
        }
    }
}
