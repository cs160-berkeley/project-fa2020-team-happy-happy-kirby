package com.gmail.kingarthuralagao.us.civicengagement.presentation.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.event_detail.EventDetailActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.adapter.EventsAdapter;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentSearchEventsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchEventsFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchEventsFragment() {
        // Required empty public constructor
    }

    FragmentSearchEventsBinding binding;
    ArrayList<Event> events = new ArrayList<>();
    SearchListAdapter adapter;
    public SearchListAdapter.IOnItemClickListener iOnItemClickListener = new SearchListAdapter.IOnItemClickListener() {
        @Override
        public void onItemCLickListener(Event e) {
            Intent i = new Intent(requireActivity(), EventDetailActivity.class);
            i.putExtra("event", e);
            requireActivity().startActivity(i);
        }
    };

    public static SearchEventsFragment newInstance() {
        SearchEventsFragment fragment = new SearchEventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSearchEventsBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpEvents();
        initializeRecyclerView(events);
    }

    private void initializeRecyclerView(ArrayList<Event> events) {
        adapter = new SearchListAdapter(events, iOnItemClickListener);

        binding.rv.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider_transparent, null));

        binding.rv.addItemDecoration(dividerItemDecoration);
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setUpEvents() {
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString();
                searchInFireStore(searchText);
            }
        });
    }

    private void searchInFireStore(String searchText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .whereArrayContains("keyWords", searchText)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Event> e = task.getResult().toObjects(Event.class);
                    adapter.setData((ArrayList) e);
                } else {
                    adapter.setData(new ArrayList<>());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("SearchEventsFragment", "Fail");
                adapter.setData(new ArrayList<>());
            }
        });
    }
}