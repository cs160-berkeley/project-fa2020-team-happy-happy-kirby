package com.gmail.kingarthuralagao.us.civicengagement.presentation.bottomnav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.event_detail.EventDetailFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.my_events.MyEventsBottomNavFragment;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentMyEventContainerBinding;

public class MyEventFragmentContainer extends Fragment implements MyEventsBottomNavFragment.IOnNavigateToEventDetailListener {

    public MyEventFragmentContainer() {
        // Required empty public constructor
    }

    public static MyEventFragmentContainer newInstance() {
        MyEventFragmentContainer fragment = new MyEventFragmentContainer();
        return fragment;
    }

    private MyEventsBottomNavFragment myEventsFragment;
    private FragmentMyEventContainerBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMyEventContainerBinding.inflate(getLayoutInflater());

        myEventsFragment = MyEventsBottomNavFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.fragmentContainer.getId(), myEventsFragment)
                .addToBackStack(null)
                .commit();
        return binding.getRoot();
    }

    @Override
    public void onNavigateToEvent(Event e) {
        EventDetailFragment eventDetailFragment = EventDetailFragment.newInstance(e);
        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.fragmentContainer.getId(), eventDetailFragment)
                .addToBackStack("eventDetail")
                .commit();
    }
}