package com.gmail.kingarthuralagao.us.civicengagement.presentation.bottomnav;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.my_events.MyEventsBottomNavFragment;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityBottomNavBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {

    private ActivityBottomNavBinding bottomNavBinding;
    private HomeFragmentContainer homeFragmentContainer;
    private MyEventsBottomNavFragment myEventFragment;
    private boolean isHomeVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavBinding = ActivityBottomNavBinding.inflate(getLayoutInflater());
        setContentView(bottomNavBinding.getRoot());

        homeFragmentContainer = HomeFragmentContainer.newInstance();
        //myEventFragmentContainer = MyEventFragmentContainer.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(bottomNavBinding.fragmentContainer.getId(), homeFragmentContainer, "")
                .commit();

        setUpEvents();
    }

    private void setUpEvents() {
        bottomNavBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_1) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(homeFragmentContainer)
                            .hide(myEventFragment)
                            .commit();
                    isHomeVisible = true;
                    return true;
                } else if (item.getItemId() == R.id.page_2) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if (myEventFragment == null) {
                        myEventFragment = MyEventsBottomNavFragment.newInstance();
                        transaction
                                .add(bottomNavBinding.fragmentContainer.getId(), myEventFragment, "")
                                .hide(homeFragmentContainer)
                                .commit();
                        isHomeVisible = false;
                        return true;
                    }
                    transaction
                            .show(myEventFragment)
                            .hide(homeFragmentContainer)
                            .commit();
                    isHomeVisible = false;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (homeFragmentContainer.getEventsViewBottomNavFragment() != null && isHomeVisible) {
            homeFragmentContainer.navigateBackToHome();
            return;
        }
        super.onBackPressed();
    }
}