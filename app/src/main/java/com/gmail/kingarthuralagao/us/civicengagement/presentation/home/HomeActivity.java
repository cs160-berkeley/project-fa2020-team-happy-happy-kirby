package com.gmail.kingarthuralagao.us.civicengagement.presentation.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.AddNewEventDialogFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.AuthenticationActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.EventsViewActivity;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

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
    }
}