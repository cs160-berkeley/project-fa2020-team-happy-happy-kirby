package com.gmail.kingarthuralagao.us.civicengagement.presentation.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

        /*
        firebaseAuth = ((CivicEngagementApp) this.getApplication()).getAuthInstance();

        String userDisplayName = firebaseAuth.getCurrentUser().getDisplayName();
        binding.nameTv.setText("Hello "
                + (userDisplayName == null ? firebaseAuth.getCurrentUser().getEmail() : userDisplayName));*/

        AddNewEventDialogFragment fragment = AddNewEventDialogFragment.newInstance();
        binding.addNewEventBtn.setOnClickListener(view -> {
            fragment.show(getSupportFragmentManager(), "");
        });

        binding.landingEditText.setOnClickListener(view -> {
            initializeLocationSearch();
        });

        binding.locationImage.setOnClickListener(view -> {
            Intent i = new Intent(this, EventsViewActivity.class);
            startActivity(i);
        });

        binding.logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            Intent i = new Intent(this, AuthenticationActivity.class);
            startActivity(i);
        });
    }

    private void initializeLocationSearch() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        /*
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            ((CivicEngagementApp) this.getApplication()).getAuthInstance().signOut();
            Intent i = new Intent(this, AuthenticationActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}