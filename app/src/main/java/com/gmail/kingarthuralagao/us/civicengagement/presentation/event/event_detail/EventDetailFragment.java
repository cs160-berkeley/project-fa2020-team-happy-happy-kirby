package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.event_detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.LoadingDialog;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.accessibility.AccessibilityFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.AuthenticationActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.event_detail.viewmodel.EventDetailViewModel;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.virtual.EngageVirtuallyFragment;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentEventDetailBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class EventDetailFragment extends Fragment {


    public static EventDetailFragment newInstance(Event event) {
        EventDetailFragment fragment = new EventDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String TAG = "EventDetailFrag";
    private EventDetailViewModel viewModel;
    private Event event;
    private FragmentEventDetailBinding binding;
    private ArrayList<String> tabTitles;
    private PagerAdapter pagerAdapter;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(EventDetailViewModel.class);
        event = (Event) getArguments().getSerializable("event");

        tabTitles = new ArrayList<>();
        tabTitles.add("Description");
        tabTitles.add("Accessibility Info");
        tabTitles.add("Engage Virtually");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventDetailBinding.inflate(inflater, container, false);
        initializeViews();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscribeToLiveData();
        setViewPager();
        setUpEvents();
    }

    private void subscribeToLiveData() {
        viewModel.postUserCheckInResponse.observe(this, statusResource -> {
            switch (statusResource.getStatus()) {
                case SUCCESS:
                    CivicEngagementApp.getUser().getCheckIns().add(event.getID());
                    setCheckInButtonStatus();
                    Toasty.success(requireContext(),
                            "You have successfully checked in to this event",
                            Toasty.LENGTH_LONG,
                            true).show();
                    loadingDialog.dismiss();
                    break;
                case LOADING:
                    loadingDialog = new LoadingDialog();
                    loadingDialog.show(getChildFragmentManager(), "");
                    break;
                case ERROR:
                    loadingDialog.dismiss();
                    Log.i(TAG, statusResource.getError().getMessage());
                    Toasty.error(requireContext(),
                            "Error checking in",
                            Toast.LENGTH_SHORT,
                            true).show();
                    break;
                default:
            }
        });
    }

    private void initializeViews() {
        binding.eventName.setText(event.getName());
        String startDate = Utils.getDateFromTimeStamp(event.getDateStart());
        String endDate  = Utils.getDateFromTimeStamp(event.getDateEnd());
        binding.includeEventDetails.eventDateTv.setText(startDate + " - " + endDate);
        binding.includeEventDetails.eventLocationTv.setText(event.getLocation());
        binding.includeEventDetails.eventTimeTv.setText(event.getTimeStart() + " - " + event.getTimeEnd() + " " + event.getTimeZone());
        binding.includeEventDetails.eventCheckInsTv.setText("" + event.getCheckIns());

        List<String> causes = event.getCauses();
        binding.includeEventDetails.causesTv.setText(causes.toString().substring(1, causes.toString().length() - 1));

        setCheckInButtonStatus();
    }

    private void setUpEvents() {
        binding.backArrowImg.setOnClickListener(v -> {
            requireActivity().finish();
        });

        binding.checkInBtn.setOnClickListener(view -> {
            viewModel.postUserCheckIn(event.getID(), CivicEngagementApp.getUser().getUserID());
        });
    }

    private void setCheckInButtonStatus() {
        User userDoc = CivicEngagementApp.getUser();

        if (userDoc == null) {
            Toast.makeText(requireContext(), "Please log in to continue", Toast.LENGTH_LONG).show();
            Intent i = new Intent(requireContext(), AuthenticationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            FirebaseAuth.getInstance().signOut();
            startActivity(i);
        } else {
            if (userDoc.getCheckIns().contains(event.getID())) {
                Log.i(TAG, userDoc.getCheckIns().toString());
                binding.checkInBtn.setText("Checked In");
                binding.checkInBtn.setBackgroundColor(getResources().getColor(R.color.checked_in_green, null));
            }
        }
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
            } else if (position == 1) {
                fragment = AccessibilityFragment.newInstance(event.getAccessibilities());
            } else {
                fragment = EngageVirtuallyFragment.newInstance(event.getGoFundMeLink());
                //fragment = EngageVirtuallyFragment.newInstance("https://www.gofundme.com/f/20rjwcnws0");
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
