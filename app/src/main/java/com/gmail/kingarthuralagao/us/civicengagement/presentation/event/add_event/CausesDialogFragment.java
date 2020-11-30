package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogCausesBinding;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils.getScreenWidth;

public class CausesDialogFragment extends DialogFragment {

    public interface ICausesSetListener {
        void onCausesSet(List<String> causes);
    }

    public static CausesDialogFragment newInstance(ArrayList<String> causes) {
        CausesDialogFragment fragment = new CausesDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("Causes", causes);
        fragment.setArguments(args);
        return fragment;
    }

    private DialogCausesBinding binding;
    private static ICausesSetListener iCausesSetListener;
    private List<String> checkedCauses;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogCausesBinding.inflate(getLayoutInflater());

        checkedCauses = getArguments().getStringArrayList("Causes");
        try {
            iCausesSetListener = (ICausesSetListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new RuntimeException("Calling fragment must implement");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpViews();
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
        iCausesSetListener = null;
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
        for (String cause : checkedCauses) {
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
                default:
                    binding.includeCausesFilter.womxnsRightsCheckbox.setChecked(true);
            }
        }
    }

    private void setUpEvents() {
        binding.negativeBtn.setOnClickListener(view -> dismiss());

        binding.positiveBtn.setOnClickListener(view -> {
            getCheckedCheckBoxes();
            dismiss();
        });
    }

    private void getCheckedCheckBoxes() {
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

        iCausesSetListener.onCausesSet(causes);
    }
}
