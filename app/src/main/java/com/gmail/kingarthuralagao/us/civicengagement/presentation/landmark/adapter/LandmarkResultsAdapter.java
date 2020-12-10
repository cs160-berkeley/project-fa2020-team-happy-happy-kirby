package com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.kingarthuralagao.us.civilengagement.databinding.RowItemLandmarkResultBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LandmarkResultsAdapter extends RecyclerView.Adapter<LandmarkResultsAdapter.ViewHolder> {

    public interface IOnLandmarkClickListener {
        void onLandMarkClick(boolean selected);
    }

    private List<LandmarkEntity> landmarks;
    private int colorResSelected;
    private int colorResUnSelected;
    private LandmarkEntity selected;
    private LinearLayout selectedLay;
    private IOnLandmarkClickListener iOnLandmarkClickListener;

    public LandmarkResultsAdapter(ArrayList<LandmarkEntity> landmarks, int colorRes, int colorResU, Fragment f) {
        this.landmarks = landmarks;
        this.colorResSelected = colorRes;
        this.colorResUnSelected = colorResU;

        if (f instanceof IOnLandmarkClickListener) {
            iOnLandmarkClickListener = (IOnLandmarkClickListener) f;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowItemLandmarkResultBinding binding = RowItemLandmarkResultBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LandmarkEntity entity = landmarks.get(position);
        holder.landmarkNameTv.setText(entity.name);
        holder.landmarkLocationTv.setText(entity.address);
        holder.layout.setOnClickListener(view -> {
            Log.i("Landmarkres", "Clicked");
            if (selected == null || (holder.landmarkNameTv.getText() != selected.getName()
                    && holder.landmarkLocationTv.getText() != selected.getAddress())) {
                holder.layout.setBackgroundColor(colorResSelected);
                if (selected != null) {
                    selectedLay.setBackgroundColor(colorResUnSelected);
                }
                selectedLay = holder.layout;
                selected = new LandmarkEntity(holder.landmarkNameTv.getText().toString(),
                        holder.landmarkLocationTv.getText().toString(), entity.city, entity.latitude, entity.longitude);
            } else {
                holder.layout.setBackgroundColor(colorResUnSelected);
                selectedLay.setBackgroundColor(colorResUnSelected);
                selectedLay = null;
                selected = null;
            }

            iOnLandmarkClickListener.onLandMarkClick(selected == null ? false : true);
        });
    }

    @Override
    public int getItemCount() {
        Log.i(getClass().getSimpleName(), "Item count: " + landmarks.size());
        return landmarks.size();
    }

    public LandmarkEntity getLandmark(int position) {
        return landmarks.get(position);
    }

    public void setData(List<LandmarkEntity> landmarks) {
        this.landmarks.clear();
        this.landmarks.addAll(landmarks);
        notifyDataSetChanged();
    }

    public LandmarkEntity getSelectedLandmark() {
        return this.selected;
    }
    public static class LandmarkEntity implements Serializable {
        private String name;
        private String address;
        private String city;
        private Double latitude;
        private Double longitude;

        public LandmarkEntity(String n, String a, String c, Double lat, Double lng) {
            name = n;
            address = a;
            city = c;
            latitude = lat;
            longitude = lng;
        }

        public String getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView landmarkNameTv;
        private TextView landmarkLocationTv;
        private LinearLayout layout;

        public ViewHolder(@NonNull RowItemLandmarkResultBinding binding) {
            super(binding.getRoot());

            landmarkNameTv = binding.landmarkNameTv;
            landmarkLocationTv = binding.landmarkLocationTv;
            layout = binding.itemLay;
        }

        public TextView getLandmarkLocationTv() {
            return landmarkLocationTv;
        }

        public TextView getLandmarkNameTv() {
            return landmarkNameTv;
        }

        public LinearLayout getLayout() {
            return layout;
        }
    }
}
