package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogFilterBinding;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils.getScreenWidth;

public class FilterDialogFragment extends DialogFragment {

    public interface IFilterClickListener {
        void onFilterApply(List<String> causes);
    }

    public static FilterDialogFragment newInstance() {
        return new FilterDialogFragment();
    }

    private DialogFilterBinding binding;
    private IFilterClickListener iFilterClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogFilterBinding.inflate(getLayoutInflater());

        try {
            iFilterClickListener = (IFilterClickListener) getParentFragment();
        } catch (ClassCastException e) {
            throw e;
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
        setUpViews();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iFilterClickListener = null;
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

    private void setUpViews() {
        binding.closeBtn.setOnClickListener(view -> dismiss());

        binding.applyBtn.setOnClickListener(view -> {
            iFilterClickListener.onFilterApply(getCausesList());
            dismiss();
        });
    }

    private List<String> getCausesList() {
        List<String> causes = new ArrayList<>();

        if (binding.includeCausesFilter.climateCheckbox.isChecked()) {
            causes.add(getResources().getString(R.string.climate));
        }

        if (binding.includeCausesFilter.gunViolenceCheckbox.isChecked()) {
            causes.add(getResources().getString(R.string.gun_violence));
        }

        if (binding.includeCausesFilter.healthcareCheckbox.isChecked()) {
            causes.add(getResources().getString(R.string.health_care));
        }

        if (binding.includeCausesFilter.immigrationCheckbox.isChecked()) {
            causes.add(getResources().getString(R.string.immigration));
        }

        if (binding.includeCausesFilter.lgbtqCheckbox.isChecked()) {
            causes.add(getResources().getString(R.string.lgbtq));
        }

        if (binding.includeCausesFilter.povertyCheckbox.isChecked()) {
            causes.add(getResources().getString(R.string.poverty));
        }

        if (binding.includeCausesFilter.racialJusticeCheckbox.isChecked()) {
            causes.add(getResources().getString(R.string.racial_justice));
        }

        if (binding.includeCausesFilter.womxnsRightsCheckbox.isChecked()) {
            causes.add(getResources().getString(R.string.womxns_right));
        }

        return causes;
    }
}
