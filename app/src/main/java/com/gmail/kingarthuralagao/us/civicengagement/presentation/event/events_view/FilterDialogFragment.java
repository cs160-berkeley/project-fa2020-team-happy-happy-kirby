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

import com.gmail.kingarthuralagao.us.civilengagement.databinding.DialogFilterBinding;

import static com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils.getScreenWidth;

public class FilterDialogFragment extends DialogFragment {

    public interface IFilterClickListener {
        void onApplyClick();
        void onCloseClick();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IFilterClickListener) {
            iFilterClickListener = (IFilterClickListener) context;
        } else {
            throw new RuntimeException("Must implement listener");
        }
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
}
