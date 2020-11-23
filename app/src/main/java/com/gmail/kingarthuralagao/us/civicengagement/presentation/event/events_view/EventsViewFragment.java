package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.detail.EventDetailActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.adapter.EventsAdapter;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEventsViewBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class EventsViewFragment extends Fragment {

    interface IRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, Integer position);
    }

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventsViewBinding.inflate(inflater, container, false);
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

            HashMap<String, Boolean> accessibilities = new HashMap<>();
            accessibilities.put("Curb cuts for wheelchair", true);
            accessibilities.put("Medic station with supplies", true);
            accessibilities.put("Medic station with trained staff", false);
            accessibilities.put("Easy access to seating", false);
            ArrayList<Event> events = new ArrayList<>();
            Event event1 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

            Event event2 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

            Event event3 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);


            Event event4 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

            events.add(event1);
            events.add(event2);
            events.add(event3);
            events.add(event4);

            eventsAdapter.setData(events);
        });

        binding.includeNowSoon.happeningSoonBtn.setOnClickListener(view -> {

            binding.includeNowSoon.happeningNowBtn.setEnabled(true);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(false);

            HashMap<String, Boolean> accessibilities = new HashMap<>();
            accessibilities.put("Curb cuts for wheelchair", true);
            accessibilities.put("Medic station with supplies", true);
            accessibilities.put("Medic station with trained staff", false);
            accessibilities.put("Easy access to seating", false);
            ArrayList<Event> events = new ArrayList<>();
            Event event1 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

            Event event2 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

            Event event3 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);


            Event event4 = new Event("#SchoolStrike4Climate", "11/03/20", "11/05/20", "8:00AM", "4:00PM",
                    "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

            events.add(event1);
            events.add(event2);
            events.add(event3);
            events.add(event4);

            eventsAdapter.setData(events);
        });


        binding.filterIv.setOnClickListener(view -> {
            FilterDialog dialog = new FilterDialog();
            dialog.show(getFragmentManager(), "");
        });

        binding.eventsRv.addOnItemTouchListener(new RecyclerTouchListener(requireContext(), new IRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View view, Integer position) {
                Toast.makeText(requireContext(), "Event name is: " + eventsAdapter.getEvent(position).getName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(requireActivity(), EventDetailActivity.class);
                i.putExtra("event", eventsAdapter.getEvent(position));
                startActivity(i);
                Toast.makeText(requireContext(), "Postion is: " + position, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initializeRecyclerView() {
        HashMap<String, Boolean> accessibilities = new HashMap<>();
        accessibilities.put("Curb cuts for wheelchair", true);
        accessibilities.put("Medic station with supplies", true);
        accessibilities.put("Medic station with trained staff", false);
        accessibilities.put("Easy access to seating", false);
        ArrayList<Event> events = new ArrayList<>();
        Event event1 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

        Event event2 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

        Event event3 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);


        Event event4 = new Event("#SchoolStrike4Climate", "11/01/20", "11/03/20", "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

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

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private Context context;
        private IRecyclerViewItemClickListener iRecyclerViewItemClickListener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, IRecyclerViewItemClickListener iRecyclerViewItemClickListener) {
            this.context = context;
            this.iRecyclerViewItemClickListener = iRecyclerViewItemClickListener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());

            if (child != null && iRecyclerViewItemClickListener != null && gestureDetector.onTouchEvent(e))
                iRecyclerViewItemClickListener.onRecyclerViewItemClick(child, rv.getChildAdapterPosition(child));

            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
    }
}
