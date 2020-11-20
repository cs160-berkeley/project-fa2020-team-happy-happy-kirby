package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.list_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.list_event.adapter.EventsAdapter;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEventsViewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventsViewFragment extends Fragment {

    public static EventsViewFragment newInstance(String inputLocation) {
        EventsViewFragment fragment = new EventsViewFragment();
        Bundle args = new Bundle();
        args.putString(INPUT_LOCATION, inputLocation);
        fragment.setArguments(args);
        return fragment;
    }

    private String inputLocation;
    private static final String INPUT_LOCATION = "Input Location";
    private FragmentEventsViewBinding binding;
    private EventsAdapter eventsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputLocation = getArguments().getString(INPUT_LOCATION);
        binding = FragmentEventsViewBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding.locationTv.setText(inputLocation);
        initializeRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpEvents();
    }

    private void setUpEvents() {
        binding.includeNowSoon.happeningNowBtn.setOnClickListener(view -> {

            binding.includeNowSoon.happeningNowBtn.setEnabled(false);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(true);

            ArrayList<Event> events = new ArrayList<>();
            Event event1 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

            Event event2 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

            Event event3 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);


            Event event4 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

            events.add(event1);
            events.add(event2);
            events.add(event3);
            events.add(event4);

            eventsAdapter.setData(events);
        });

        binding.includeNowSoon.happeningSoonBtn.setOnClickListener(view -> {

            binding.includeNowSoon.happeningNowBtn.setEnabled(true);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(false);

            ArrayList<Event> events = new ArrayList<>();
            Event event1 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

            Event event2 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

            Event event3 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);


            Event event4 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

            events.add(event1);
            events.add(event2);
            events.add(event3);
            events.add(event4);

            eventsAdapter.setData(events);
        });
    }

    private void initializeRecyclerView() {
        ArrayList<Event> events = new ArrayList<>();
        Event event1 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

        Event event2 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

        Event event3 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);


        Event event4 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, null);

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        eventsAdapter = new EventsAdapter(events);

        binding.eventsRv.setAdapter(eventsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider_transparent, null));

        binding.eventsRv.addItemDecoration(dividerItemDecoration);
        binding.eventsRv.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}
