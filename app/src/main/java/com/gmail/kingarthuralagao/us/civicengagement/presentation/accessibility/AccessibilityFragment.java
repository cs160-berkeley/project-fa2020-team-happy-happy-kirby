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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccessibilityFragment extends Fragment {

    public static AccessibilityFragment newInstance(Map<String, Boolean> accessibilities) {
        AccessibilityFragment fragment = new AccessibilityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ACCESSIBILITIES, (Serializable) accessibilities);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentAccessibilityBinding binding;
    private static final String ACCESSIBILITIES = "accessibilities";
    private HashMap<String, Boolean> accessibilities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accessibilities = (HashMap) getArguments().getSerializable(ACCESSIBILITIES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccessibilityBinding.inflate(inflater, container, false);
        initializeRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initializeRecyclerView() {
        ArrayList<Map.Entry<String, Boolean>> accessibilitiesList = new ArrayList<>();
        accessibilitiesList.addAll(accessibilities.entrySet());

        AccessibilityAdapter accessibilityAdapter = new AccessibilityAdapter(accessibilitiesList);

        binding.recyclerView.setAdapter(accessibilityAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider_gray, null));

        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}