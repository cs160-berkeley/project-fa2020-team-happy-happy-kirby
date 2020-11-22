package com.gmail.kingarthuralagao.us.civicengagement.presentation.accessibility;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.accessibility.Accessibility;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.accessibility.adapter.AccessibilityAdapter;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentAccessibilityBinding;

import java.util.ArrayList;

public class AccessibilityFragment extends Fragment {

    public static AccessibilityFragment newInstance() {
        AccessibilityFragment fragment = new AccessibilityFragment();
        return fragment;
    }

    private FragmentAccessibilityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initializeRecyclerView();
        binding = FragmentAccessibilityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initializeRecyclerView() {
        ArrayList<Accessibility> accessibilities = new ArrayList<>();
        accessibilities.add(new Accessibility("Curb cuts for wheelchair", true));
        accessibilities.add(new Accessibility("Medic station with supplies", true));
        accessibilities.add(new Accessibility("Medic station with trained staff", false));
        accessibilities.add(new Accessibility("Easy access to seating", false));

        AccessibilityAdapter accessibilityAdapter = new AccessibilityAdapter(accessibilities);

        binding.recylerView.setAdapter(accessibilityAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider_gray, null));

        binding.recylerView.addItemDecoration(dividerItemDecoration);
        binding.recylerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}