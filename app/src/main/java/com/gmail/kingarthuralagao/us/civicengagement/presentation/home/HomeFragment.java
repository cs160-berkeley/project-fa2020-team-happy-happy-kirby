package com.gmail.kingarthuralagao.us.civicengagement.presentation.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.AuthenticationActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.AddNewEventDialogFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.EventsViewActivity;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpEvents();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            ((CivicEngagementApp) requireActivity().getApplication()).getAuthInstance().signOut();
            Intent i = new Intent(requireActivity(), AuthenticationActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpEvents() {
        AddNewEventDialogFragment fragment = AddNewEventDialogFragment.newInstance();
        binding.addNewEventBtn.setOnClickListener(view -> {
            fragment.show(getChildFragmentManager(), "");
        });

        binding.landingEditText.setOnClickListener(view -> {
            initializeLocationSearch();
        });

        binding.locationImage.setOnClickListener(view -> {
            Intent i = new Intent(requireActivity(), EventsViewActivity.class);
            startActivityForResult(i, 200);
        });
    }

    private void initializeLocationSearch() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        /*
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);*/
    }
}
