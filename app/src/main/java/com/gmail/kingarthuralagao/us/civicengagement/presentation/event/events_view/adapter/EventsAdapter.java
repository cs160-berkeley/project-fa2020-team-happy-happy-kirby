package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.RowItemEventDetailsBinding;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private ArrayList<Event> events;

    public EventsAdapter(ArrayList<Event> e) {
        events = e;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowItemEventDetailsBinding binding = RowItemEventDetailsBinding.inflate(inflater, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventNameTv.setText(event.getName());
        String startDate = Utils.getDateFromTimeStamp(event.getDateStart());
        String endDate  = Utils.getDateFromTimeStamp(event.getDateEnd());
        holder.eventDateTv.setText(startDate + " - " + endDate);
        holder.eventTimeTv.setText(event.getTimeStart() + " - " + event.getTimeEnd());
        holder.eventLocationTv.setText(event.getLocation());
        holder.eventCheckInsTv.setText(event.getCheckIns() + "");
    }

    @Override
    public int getItemCount() {
        Log.i(getClass().getSimpleName(), "Item count: " + events.size());
        return events.size();
    }

    public Event getEvent(int position) {
        return events.get(position);
    }

    public void setData(ArrayList<Event> eventList) {
        this.events = eventList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eventNameTv;
        private TextView eventLocationTv;
        private TextView eventDateTv;
        private TextView eventTimeTv;
        private TextView eventCheckInsTv;

        public ViewHolder(@NonNull RowItemEventDetailsBinding binding) {
            super(binding.getRoot());

            eventNameTv = binding.eventNameTv;
            eventLocationTv = binding.eventLocationTv;
            eventDateTv = binding.eventDateTv;
            eventTimeTv = binding.eventTimeTv;
            eventCheckInsTv = binding.checkInsTv;
        }

        public TextView getEventNameTv() {
            return eventNameTv;
        }

        public TextView getEventLocationTv() {
            return eventLocationTv;
        }

        public TextView getEventDateTv() {
            return eventDateTv;
        }

        public TextView getEventTimeTv() {
            return eventTimeTv;
        }

        public TextView getEventCheckInsTv() {
            return eventCheckInsTv;
        }
    }
}
