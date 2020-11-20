package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.accessibility.Accessibility;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.RowItemCheckboxBinding;

import java.util.ArrayList;

public class AccessibilityCheckboxAdapter extends RecyclerView.Adapter<AccessibilityCheckboxAdapter.ViewHolder> {

    private final ArrayList<Accessibility> accessibilities;

    public AccessibilityCheckboxAdapter(ArrayList<Accessibility> accessibilityList) {
        accessibilities = accessibilityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowItemCheckboxBinding binding = RowItemCheckboxBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getResourceTv().setText(accessibilities.get(position).getResource());
    }

    @Override
    public int getItemCount() {
        Log.i(getClass().getSimpleName(), "Item count: " + accessibilities.size());
        return accessibilities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView resourceTv;
        private CheckBox availabilityCheckBox;

        public ViewHolder(@NonNull RowItemCheckboxBinding binding) {
            super(binding.getRoot());

            resourceTv = binding.accessibilityTv;
            availabilityCheckBox = binding.accesibilityCheckbox;
        }

        public TextView getResourceTv() {
            return resourceTv;
        }

        public CheckBox getAvailabilityIv() {
            return availabilityCheckBox;
        }
    }
}
