package com.gmail.kingarthuralagao.us.civicengagement.presentation.bottomnav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.EventsViewBottomNavFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.home.HomeBottomNavFragment;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentHomeContainerBinding;

public class HomeFragmentContainer extends Fragment implements HomeBottomNavFragment.IOnNavigateToEventsView,
        EventsViewBottomNavFragment.IOnBackArrowPressed{

    public HomeFragmentContainer() {
        // Required empty public constructor
    }

    public static HomeFragmentContainer newInstance() {
        HomeFragmentContainer fragment = new HomeFragmentContainer();
        return fragment;
    }

    private FragmentHomeContainerBinding binding;
    private HomeBottomNavFragment homeBottomNavFragment;
    private static EventsViewBottomNavFragment eventsViewBottomNavFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeContainerBinding.inflate(getLayoutInflater());

        homeBottomNavFragment = HomeBottomNavFragment.newInstance();

        getChildFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), homeBottomNavFragment)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onNavigateToEventsView(String location) {
        eventsViewBottomNavFragment = EventsViewBottomNavFragment.newInstance(location);

        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .add(binding.fragmentContainer.getId(), eventsViewBottomNavFragment)
                .hide(homeBottomNavFragment)
                .commit();
        /*
        Intent i = new Intent(requireActivity(), EventsViewActivity.class);
        i.putExtra("Address", city);
        /*
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(i);
        requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
    }

    public void navigateBackToHome() {
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .show(homeBottomNavFragment)
                .remove(eventsViewBottomNavFragment)
                .commit();
        eventsViewBottomNavFragment = null;
    }

    public static EventsViewBottomNavFragment getEventsViewBottomNavFragment() {
        return eventsViewBottomNavFragment;
    }

    @Override
    public void onBackArrowPressed() {
        navigateBackToHome();
    }
}