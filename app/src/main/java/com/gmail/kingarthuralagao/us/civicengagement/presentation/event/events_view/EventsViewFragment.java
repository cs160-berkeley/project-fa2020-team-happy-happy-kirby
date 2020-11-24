package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.kingarthuralagao.us.civicengagement.data.Resource;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.signin.SignInFragmentViewModel;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.detail.EventDetailActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.adapter.EventsAdapter;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEventsViewBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils.getTimeStampFromDate;

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
    private final String TAG = getClass().getSimpleName();
    private static final String INPUT_LOCATION = "Input Location";
    private ArrayList<Event> happeningNow = new ArrayList<>();
    private ArrayList<Event> happeningSoon = new ArrayList<>();
    private FragmentEventsViewBinding binding;
    private EventsAdapter eventsAdapter;
    private EventsViewViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputLocation = getArguments().getString(INPUT_LOCATION);
        viewModel = new ViewModelProvider(this).get(EventsViewViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventsViewBinding.inflate(inflater, container, false);
        binding.locationTv.setText(inputLocation);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subscribeToLiveData();
        viewModel.fetchEventsHappeningNow(System.currentTimeMillis(), inputLocation);
        setUpEvents();
    }

    private void subscribeToLiveData() {
        viewModel.fetchEventsHappeningNowResponse.observe(this, mapResource -> {
            switch (mapResource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.eventsRv.setVisibility(View.INVISIBLE);
                    break;
                case SUCCESS:
                    happeningNow.clear();
                    for (DocumentSnapshot document : mapResource.getData()) {
                        buildEvent(document.getData());
                        happeningNow.add(buildEvent(document.getData()));
                    }
                    initializeRecyclerView(happeningNow);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                case ERROR:
                    Log.i(TAG, mapResource.getError().getMessage());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                default:
                    Log.d(TAG, "Created");
                    break;
            }
        });

        viewModel.fetchEventsHappeningSoonResponse.observe(this, listResource -> {
            switch (listResource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.eventsRv.setVisibility(View.INVISIBLE);
                    break;
                case SUCCESS:
                    happeningSoon.clear();
                    for (DocumentSnapshot document : listResource.getData()) {
                        buildEvent(document.getData());
                        happeningSoon.add(buildEvent(document.getData()));
                    }
                    eventsAdapter.setData(happeningSoon);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                case ERROR:
                    Log.i(TAG, listResource.getError().getMessage());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                default:
                    Log.d(TAG, "Created");
                    break;
            }
        });
    }

    private Event buildEvent(Map<String, Object> data) {
        Map<String, Object> m = data;
        String name = (String) m.get("name");
        Long dateStart = (Long) m.get("dateStart");
        Long dateEnd = (Long) m.get("dateEnd");
        String timeStart = (String) m.get("timeStart");
        String city = (String) m.get("city");
        String timeEnd = (String) m.get("timeEnd");
        String description = (String) m.get("description");
        String location = (String) m.get("location");
        String timeZone = (String) m.get("timeZone");
        //Integer checkIns = (Integer) m.get("checkIns");
        List<String> causes = (List<String>) m.get("causes");
        Map<String, Boolean> accessibilities = (Map<String, Boolean>) m.get("accessibilities");
        Integer eventID = (Integer) m.get("eventID");
        Event event = new Event(name, dateStart, dateEnd, timeStart, timeEnd, description, location, timeZone,
                40000, causes, accessibilities, eventID, city);
        return event;
    }

    private void setUpEvents() {
        binding.includeNowSoon.happeningNowBtn.setOnClickListener(view -> {
            binding.includeNowSoon.happeningNowBtn.setEnabled(false);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(true);

            if (happeningNow.size() == 0) {
                viewModel.fetchEventsHappeningNow(System.currentTimeMillis(), inputLocation);
            } else {
                eventsAdapter.setData(happeningNow);
            }
        });

        binding.includeNowSoon.happeningSoonBtn.setOnClickListener(view -> {

            binding.includeNowSoon.happeningNowBtn.setEnabled(true);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(false);

            if (happeningSoon.size() == 0) {
                viewModel.fetchEventsHappeningSoon(System.currentTimeMillis(), inputLocation);
            } else {
                eventsAdapter.setData(happeningSoon);
            }
        });


        binding.filterBtn.setOnClickListener(view -> {
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

    private void initializeRecyclerView(ArrayList<Event> events) {
        /*
        HashMap<String, Boolean> accessibilities = new HashMap<>();
        accessibilities.put("Curb cuts for wheelchair", true);
        accessibilities.put("Medic station with supplies", true);
        accessibilities.put("Medic station with trained staff", false);
        accessibilities.put("Easy access to seating", false);
        ArrayList<Event> events = new ArrayList<>();
        String date1 = "11/22/2020 21:14:00";
        String date2 = "11/23/2020 21:14:00";
        Long dateStart = getTimeStampFromDate(date1);
        Long dateEnd = getTimeStampFromDate(date2);
        Event event1 = new Event("#SchoolStrike4Climate", dateStart, dateEnd, "8:00AM", "4:00PM",
                "This is an event", "2520 Sproul Hall Plaza Berkeley, CA", "PST", 40000, null, accessibilities, 123);

        events.add(event1);
        events.add(event1);
        events.add(event1);
        events.add(event1);
        events.add(event1);*/
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
