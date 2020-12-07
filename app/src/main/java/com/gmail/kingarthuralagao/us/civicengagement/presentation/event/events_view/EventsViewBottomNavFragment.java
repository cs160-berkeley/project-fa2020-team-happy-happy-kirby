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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.event_detail.EventDetailActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.adapter.EventsAdapter;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.viewmodel.EventsViewViewModel;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEventsViewBinding;

import java.util.ArrayList;
import java.util.List;

public class EventsViewBottomNavFragment extends Fragment implements
        FilterDialogFragment.IFilterClickListener {

    interface IRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, Integer position);
    }

    public interface IOnBackArrowPressed {
        void onBackArrowPressed();
    }

    public static EventsViewBottomNavFragment newInstance(String inputLocation) {
        EventsViewBottomNavFragment fragment = new EventsViewBottomNavFragment();
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
    private ArrayList<Event> happeningNowFiltered = new ArrayList<>();
    private ArrayList<Event> happeningSoonFiltered = new ArrayList<>();
    private ArrayList<String> filters = new ArrayList<>();
    private FragmentEventsViewBinding binding;
    private EventsAdapter eventsAdapter;
    private EventsViewViewModel viewModel;
    private Boolean clickedBefore = false;
    private IOnBackArrowPressed onBackArrowPressed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputLocation = getArguments().getString(INPUT_LOCATION);
        viewModel = new ViewModelProvider(this).get(EventsViewViewModel.class);

        if (getParentFragment() instanceof IOnBackArrowPressed) {
            onBackArrowPressed = (IOnBackArrowPressed) getParentFragment();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventsViewBinding.inflate(inflater, container, false);
        binding.locationTv.setText("Events in " + inputLocation);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subscribeToLiveData();
        setUpEvents();
        viewModel.fetchEventsHappeningNow(System.currentTimeMillis(), inputLocation);
    }

    @Override
    public void onFilterApply(List<String> causes) {
        updateData(causes);
        filters.clear();
        filters.addAll(causes);
    }

    private void updateData(List<String> causes) {
        if (causes.isEmpty()) {
            if (binding.includeNowSoon.happeningNowBtn.isEnabled()) {
                happeningSoonFiltered.clear();
                happeningSoonFiltered.addAll(happeningSoon);
                eventsAdapter.setData(happeningSoon);
            } else {
                eventsAdapter.setData(happeningNow);
                happeningNowFiltered.clear();
                happeningNowFiltered.addAll(happeningNow);
            }
            return;
        }

        happeningNowFiltered.clear();
        happeningSoonFiltered.clear();

        List<Event> happeningNowDummy = new ArrayList<>();
        List<Event> happeningSoonDummy = new ArrayList<>();
        happeningNowDummy.addAll(happeningNow);
        happeningSoonDummy.addAll(happeningSoon);

        for (String cause : causes) {
            for (int i = 0 ; i < happeningNowDummy.size(); i++) {
                if (happeningNowDummy.get(i).getCauses().contains(cause)) {
                    happeningNowFiltered.add(happeningNowDummy.remove(i));
                }
            }

            for (int i = 0; i < happeningSoonDummy.size(); i++) {
                if (happeningSoonDummy.get(i).getCauses().contains(cause)) {
                    happeningSoonFiltered.add(happeningSoonDummy.remove(i));
                }
            }
        }

        if (binding.includeNowSoon.happeningNowBtn.isEnabled()) {
            eventsAdapter.setData(happeningSoonFiltered);
        } else {
            eventsAdapter.setData(happeningNowFiltered);
        }

    }

    private void subscribeToLiveData() {
        viewModel.fetchEventsHappeningNowResponse.observe(this, mapResource -> {
            switch (mapResource.getStatus()) {
                case LOADING:
                    binding.swipeRefreshLayout.setRefreshing(true);
                    binding.eventsRv.setVisibility(View.INVISIBLE);
                    break;
                case SUCCESS:
                    happeningNow.clear();
                    happeningNow = (ArrayList<Event>) mapResource.getData();
                    happeningNowFiltered.clear();
                    happeningNowFiltered.addAll(happeningNow);
                    if (eventsAdapter == null) {
                        initializeRecyclerView(happeningNow);
                    } else {
                        eventsAdapter.setData(happeningNow);
                    }
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                case ERROR:
                    Log.i(TAG, mapResource.getError().getMessage());
                    binding.swipeRefreshLayout.setRefreshing(false);
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
                    binding.swipeRefreshLayout.setRefreshing(true);
                    binding.eventsRv.setVisibility(View.INVISIBLE);
                    break;
                case SUCCESS:
                    happeningSoon.clear();
                    happeningSoon = (ArrayList<Event>) listResource.getData();
                    happeningSoonFiltered.clear();
                    happeningSoonFiltered.addAll(happeningSoon);
                    eventsAdapter.setData(happeningSoon);
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                case ERROR:
                    Log.i(TAG, listResource.getError().getMessage());
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                default:
                    Log.d(TAG, "Created");
                    break;
            }
        });

        viewModel.fetchEventsHappeningNowWithFilterResponse.observe(this, listResource -> {
            switch (listResource.getStatus()) {
                case LOADING:
                    binding.swipeRefreshLayout.setRefreshing(true);
                    binding.eventsRv.setVisibility(View.INVISIBLE);
                    break;
                case SUCCESS:
                    happeningNowFiltered.clear();
                    happeningNowFiltered.addAll(listResource.getData());
                    eventsAdapter.setData(happeningNowFiltered);
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                case ERROR:
                    Log.i(TAG, listResource.getError().getMessage());
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                default:
                    Log.d(TAG, "Created");
                    break;
            }
        });

        viewModel.fetchEventsHappeningSoonWithFilterResponse.observe(this, listResource -> {
            switch (listResource.getStatus()) {
                case LOADING:
                    binding.swipeRefreshLayout.setRefreshing(true);
                    binding.eventsRv.setVisibility(View.INVISIBLE);
                    break;
                case SUCCESS:
                    happeningSoonFiltered.clear();
                    happeningSoonFiltered.addAll(listResource.getData());
                    eventsAdapter.setData(happeningSoonFiltered);
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                case ERROR:
                    Log.i(TAG, listResource.getError().getMessage());
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.eventsRv.setVisibility(View.VISIBLE);
                    break;
                default:
                    Log.d(TAG, "Created");
                    break;
            }
        });
    }

    private void setUpEvents() {
        binding.includeNowSoon.happeningNowBtn.setOnClickListener(view -> {
            binding.includeNowSoon.happeningNowBtn.setEnabled(false);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(true);

            /*
            if (happeningNow.size() == 0) {
                viewModel.fetchEventsHappeningNow(System.currentTimeMillis(), inputLocation);
            } else {
                eventsAdapter.setData(happeningNowFiltered);
            }*/
            eventsAdapter.setData(happeningNowFiltered);
        });

        binding.includeNowSoon.happeningSoonBtn.setOnClickListener(view -> {

            binding.includeNowSoon.happeningNowBtn.setEnabled(true);
            binding.includeNowSoon.happeningSoonBtn.setEnabled(false);

            if (!clickedBefore) {
                if (filters.size() == 0) {
                    viewModel.fetchEventsHappeningSoon(System.currentTimeMillis(), inputLocation);
                } else {
                    viewModel.fetchEventsHappeningSoonWithFilter(System.currentTimeMillis(), inputLocation, filters);
                }
                clickedBefore = true;
            } else {
                eventsAdapter.setData(happeningSoonFiltered);
            }
        });


        binding.filterBtn.setOnClickListener(view -> {
            FilterDialogFragment dialog = FilterDialogFragment.newInstance(filters);
            dialog.show(getChildFragmentManager(), "");
        });

        binding.eventsRv.addOnItemTouchListener(new RecyclerTouchListener(requireContext(), new IRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View view, Integer position) {
                Intent i = new Intent(requireActivity(), EventDetailActivity.class);
                i.putExtra("event", eventsAdapter.getEvent(position));
                startActivity(i);
                requireActivity().overridePendingTransition(R.anim.pull_up_from_bottom, R.anim.do_nothing);
                /*
                i.putExtra("event", eventsAdapter.getEvent(position));
                startActivity(i);*/
            }
        }));

        binding.swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                    if (binding.includeNowSoon.happeningNowBtn.isEnabled()) {
                        if (filters.size() == 0) {
                            viewModel.fetchEventsHappeningSoon(System.currentTimeMillis(), inputLocation);
                        } else {
                            viewModel.fetchEventsHappeningSoonWithFilter(System.currentTimeMillis(), inputLocation, filters);
                        }
                    } else {
                        if (filters.size() == 0) {
                            viewModel.fetchEventsHappeningNow(System.currentTimeMillis(), inputLocation);
                        } else {
                            viewModel.fetchEventsHappeningNowWithFilter(System.currentTimeMillis(), inputLocation, filters);
                        }
                    }
                }
        );

        binding.backArrow.setOnClickListener(view -> {
            //requireActivity().finish();
            onBackArrowPressed.onBackArrowPressed();
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
}
