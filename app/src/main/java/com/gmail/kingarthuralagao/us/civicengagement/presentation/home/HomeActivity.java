package com.gmail.kingarthuralagao.us.civicengagement.presentation.home;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ActivityHomeBinding binding;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), HomeFragment.newInstance())
                .commit();

        fetchEventsFromFirebase();
        //CivicEngagementApp.fetchUserDocument();
    }

    private void fetchEventsFromFirebase() {
        List<String> eventIDs = CivicEngagementApp.getUser().getCheckIns();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("events").
            whereIn("id", eventIDs)
                .get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Event e = document.toObject(Event.class);
                    Log.d("In Home Activity", "Event name is: " + e.getName());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("In Home Act", e.getMessage());
            }
        });
    }
}