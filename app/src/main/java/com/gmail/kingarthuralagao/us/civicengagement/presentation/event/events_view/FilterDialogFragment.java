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

    public static FilterDialogFragment newInstance(ArrayList<String> checkedFilters) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(CHECKED_FILTERS, checkedFilters);
        frag.setArguments(args);
        return frag;
    }

    public static String CHECKED_FILTERS = "checkedFilters";
    private DialogFilterBinding binding;
    private IFilterClickListener iFilterClickListener;
    private ArrayList<String> checkedFilters;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogFilterBinding.inflate(getLayoutInflater());

        try {
            iFilterClickListener = (IFilterClickListener) getParentFragment();
        } catch (ClassCastException e) {
            throw e;
        }
        checkedFilters = getArguments().getStringArrayList(CHECKED_FILTERS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setupViews();
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

    private void setUpEvents() {
        binding.closeBtn.setOnClickListener(view -> dismiss());

        binding.applyBtn.setOnClickListener(view -> {
            iFilterClickListener.onFilterApply(getCausesList());
            dismiss();
        });
    }

    private void setupViews() {
        for (String cause : checkedFilters) {
            switch (cause) {
                case "Climate":
                    binding.includeCausesFilter.climateCheckbox.setChecked(true);
                    break;
                case "Gun Violence":
                    binding.includeCausesFilter.gunViolenceCheckbox.setChecked(true);
                    break;
                case "Health Care":
                    binding.includeCausesFilter.healthcareCheckbox.setChecked(true);
                    break;
                case "Immigration":
                    binding.includeCausesFilter.immigrationCheckbox.setChecked(true);
                    break;
                case "LGBTQ+":
                    binding.includeCausesFilter.lgbtqCheckbox.setChecked(true);
                    break;
                case "Poverty":
                    binding.includeCausesFilter.povertyCheckbox.setChecked(true);
                    break;
                case "Racial Justice":
                    binding.includeCausesFilter.racialJusticeCheckbox.setChecked(true);
                    break;
                case "Womxn's Rights":
                    binding.includeCausesFilter.womxnsRightsCheckbox.setChecked(true);
                    break;
                default:
            }
        }
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
