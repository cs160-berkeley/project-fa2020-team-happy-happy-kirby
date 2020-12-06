package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogLocationOptionBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LocationOptionDialogFragment extends BottomSheetDialogFragment {

    public interface IChoiceSetListener {
        void onLocationChoiceSet(int choice);
    }

    public static LocationOptionDialogFragment newInstance() {
        return new LocationOptionDialogFragment();
    }

    private DialogLocationOptionBinding binding;
    private static IChoiceSetListener iChoiceSetListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogLocationOptionBinding.inflate(getLayoutInflater());

        try {
            iChoiceSetListener = (IChoiceSetListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new RuntimeException("Calling fragment must implement");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpEvents();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iChoiceSetListener = null;
    }

    private void setUpEvents() {
        binding.landmarkLay.setOnClickListener(view -> {
            iChoiceSetListener.onLocationChoiceSet(1);
            dismiss();
        });

        binding.searchLocationLay.setOnClickListener(view -> {
            iChoiceSetListener.onLocationChoiceSet(0);
            dismiss();
        });
    }
}
