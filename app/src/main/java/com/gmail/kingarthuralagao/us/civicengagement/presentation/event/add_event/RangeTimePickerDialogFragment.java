package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogFilterBinding;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogTimePickerWithRangeBinding;

import static com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils.getScreenWidth;

public class RangeTimePickerDialogFragment extends DialogFragment {

    public interface ITimePickerListener {
        void onTimeRangeSet(int startHour, int startMinute, int EndHour, int endMinute);
    }

    public static RangeTimePickerDialogFragment newInstance() {
        return new RangeTimePickerDialogFragment();
    }

    private DialogTimePickerWithRangeBinding binding;
    private ITimePickerListener iTimePickerListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogTimePickerWithRangeBinding.inflate(getLayoutInflater());

        try {
            iTimePickerListener = (ITimePickerListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new RuntimeException("Calling fragment must implement");
        }
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

    private void setUpEvents() {
        binding.toolbar.setNavigationOnClickListener(view -> dismiss());

        binding.saveBtn.setOnClickListener(view -> {
            iTimePickerListener.onTimeRangeSet(binding.startTimePicker.getHour(),
                    binding.startTimePicker.getMinute(),
                    binding.endTimePicker.getHour(),
                    binding.endTimePicker.getMinute());
            dismiss();
        });

        binding.startTimePicker.setOnTimeChangedListener((timePicker, hour, minute) -> binding.saveBtn.setEnabled(true));

        binding.endTimePicker.setOnTimeChangedListener((timePicker, hour, minute) -> binding.saveBtn.setEnabled(true));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iTimePickerListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = getScreenWidth();
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


}
