package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.my_events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.event_detail.EventDetailActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.adapter.EventsAdapter;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEventsViewBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyEventsFragment extends Fragment {

    interface IRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, Integer position);
    }

    public static MyEventsFragment newInstance() {
        return new MyEventsFragment();
    }

    FragmentEventsViewBinding binding;
    private EventsAdapter eventsAdapter;
    private Boolean clickedBefore = false;
    private ArrayList<Event> happeningNow = new ArrayList<>();
    private ArrayList<Event> happeningSoon = new ArrayList<>();

    public MyEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentEventsViewBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setUpViews();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpEvents();
    }

    private void setUpViews() {
        binding.filterBtn.setVisibility(View.GONE);
        binding.locationTv.setText("My Events");
        fetchMyEventsHappeningNow();
    }

    private void setUpEvents() {
        binding.includeNowSoon.happeningNowBtn.setOnClickListener(view -> {
            binding.includeNowSoon.happeningNowBtn.setEnabled(false);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(true);

            eventsAdapter.setData(happeningNow);
        });

        binding.includeNowSoon.happeningSoonBtn.setOnClickListener(view -> {

            binding.includeNowSoon.happeningNowBtn.setEnabled(true);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(false);

            if (!clickedBefore) {
                fetchMyEventsHappeningSoon();
                clickedBefore = true;
            } else {
                eventsAdapter.setData(happeningSoon);
            }
        });

        binding.eventsRv.addOnItemTouchListener(new RecyclerTouchListener(requireContext(), new IRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View view, Integer position) {
                Intent i = new Intent(requireActivity(), EventDetailActivity.class);
                i.putExtra("event", eventsAdapter.getEvent(position));
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.pull_up_from_bottom, R.anim.do_nothing);
            }
        }));

        binding.swipeRefreshLayout.setEnabled(false);
        /*
        binding.swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    return;
                    /*
                    if (binding.includeNowSoon.happeningNowBtn.isEnabled()) {
                        fetchMyEventsHappeningNow();
                    } else {
                        fetchMyEventsHappeningSoon();
                    }
                }
        );*/

        binding.backArrow.setOnClickListener(view -> {
            requireActivity().finish();
        });
    }

    private void initializeRecyclerView(ArrayList<Event> events) {
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

    private void fetchMyEventsHappeningNow() {
        binding.swipeRefreshLayout.setRefreshing(true);
        User user = CivicEngagementApp.getUser();
        List<String> checkIns = user.getCheckIns();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereIn("id", checkIns)
                .whereLessThanOrEqualTo("dateStart", System.currentTimeMillis() / 1000)
                .orderBy("dateStart")
                .get()
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        happeningNow.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event e = document.toObject(Event.class);
                            happeningNow.add(e);
                        }
                        initializeRecyclerView(happeningNow);
                    } else {
                        Toasty.error(requireContext(), "Error Fetching Events", Toasty.LENGTH_SHORT, true);
                    }
                    binding.swipeRefreshLayout.setRefreshing(false);
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toasty.error(requireContext(), "Error Fetching Events", Toasty.LENGTH_SHORT, true);
                    binding.swipeRefreshLayout.setRefreshing(false);
                });
    }

    private void fetchMyEventsHappeningSoon() {
        binding.swipeRefreshLayout.setRefreshing(true);
        binding.eventsRv.setVisibility(View.INVISIBLE);
        User user = CivicEngagementApp.getUser();
        List<String> checkIns = user.getCheckIns();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereIn("id", checkIns)
                .whereGreaterThan("dateStart", System.currentTimeMillis() / 1000)
                .orderBy("dateStart")
                .get()
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        happeningSoon.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event e = document.toObject(Event.class);
                            happeningSoon.add(e);
                        }

                        eventsAdapter.setData(happeningSoon);
                    } else {
                        Toasty.error(requireContext(), "Error Fetching Events", Toasty.LENGTH_SHORT, true);
                    }
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(requireContext(), "Error Fetching Events", Toasty.LENGTH_SHORT, true);
                binding.swipeRefreshLayout.setRefreshing(false);
                binding.eventsRv.setVisibility(View.VISIBLE);
            }
        });
    }
}