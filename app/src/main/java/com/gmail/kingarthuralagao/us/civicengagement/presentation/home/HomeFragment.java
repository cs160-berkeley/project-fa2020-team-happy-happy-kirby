package com.gmail.kingarthuralagao.us.civicengagement.presentation.home;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.AuthenticationActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.AddNewEventDialogFragment;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.detail.EventDetailActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.EventsViewActivity;
import com.gmail.kingarthuralagao.us.civilengagement.BuildConfig;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentHomeBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private FragmentHomeBinding binding;
    private final String TAG = getClass().getSimpleName();
    private LocationManager mLocManager;
    private LocationListener mLocListener;
    private RequestQueue queue;
    private final int LOCATION_REQUEST_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);;
        mLocListener = location -> {
            //
        };
        queue = Volley.newRequestQueue(this.getActivity());

        Log.i("HomeFrag", (System.currentTimeMillis() / 1000) + "");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpEvents();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            ((CivicEngagementApp) requireActivity().getApplication()).getAuthInstance().signOut();
            Intent i = new Intent(requireActivity(), AuthenticationActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            Log.i(TAG, "Cancelled");
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String address = place.getAddress();
                int commaIndex = address.indexOf(",");
                String city = address.substring(0, commaIndex);
                Log.i(TAG, "Address comp: " + place.getAddressComponents());
                Intent i = new Intent(requireActivity(), EventsViewActivity.class);
                i.putExtra("Address", city);
                startActivity(i);
                //Log.i(TAG, "Place: " + place.getAddress() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "Cancelled");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpEvents() {
        binding.addNewEventBtn.setOnClickListener(view -> {
            AddNewEventDialogFragment fragment = AddNewEventDialogFragment.newInstance();
            fragment.show(getChildFragmentManager(), "");
        });

        binding.searchForLocationTv.setOnClickListener(view -> {
            initializeLocationSearch();
        });

        binding.useCurrentLocationTv.setOnClickListener(view -> {
            Intent i = new Intent(requireActivity(), EventsViewActivity.class);
            Log.i(TAG, "In onclicklistener for location image");
            // Location permission granted.
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                // Update location.
                mLocManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
                Location location = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.i(TAG, "Location: " + location.toString());
                makeGeoRequest(location);
            } else {
                // Location permission not granted.
                Log.i(TAG, "Requesting location permission.");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PERMISSION_GRANTED) {

                // Permission was granted.
                Intent i = new Intent(requireActivity(), EventsViewActivity.class);
                mLocManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
                Location location = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.i(TAG, "Location: " + location.toString());
                makeGeoRequest(location);
            } else {
                // Permission denied.
                // Tell the user the action is cancelled.
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Location permissions must be enabled to use this feature.")
                        .setCancelable(false)
                        .setPositiveButton("OK", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void makeGeoRequest(Location location) {
        final String GEO_URL = "https://maps.googleapis.com/maps/api/geocode/json";
        if (location != null) {
            // Request a string response from the provided URL.
            String fullGeoUrl = GEO_URL + "?latlng=" + location.getLatitude() + ','
                    + location.getLongitude() + "&key=" + BuildConfig.API_KEY;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    fullGeoUrl,
                    null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i(TAG, "Response: " + response.toString());
                                JSONArray results = response.getJSONArray("results");
                                JSONObject firstResult = results.getJSONObject(0);
                                JSONArray addressComponents = firstResult.getJSONArray("address_components");

                                String city = "";
                                for (int i = 0; i < addressComponents.length(); i++) {
                                    JSONObject object = addressComponents.getJSONObject(i);

                                    JSONArray typesArray = object.getJSONArray("types");
                                    for (int j = 0; j < typesArray.length(); j++) {
                                        if (typesArray.get(j).equals("locality")) {
                                            city = object.getString("long_name");
                                        }
                                    }

                                }
                                //JSONObject cityInfo = addressComponents.getJSONObject(3);
                                //String formattedAddress = firstResult.getString("formatted_address");
                                Log.i(TAG, "Address: " + city);
                                Intent i = new Intent(requireActivity(), EventsViewActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                i.putExtra("Address", city);
                                startActivity(i);
                            } catch (Exception e) {
                                Log.e(TAG, "Error retrieving results from GeoCoding API: " + e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error getting response from GeoCoding API: " + error);
                }
            });
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        }
    }

    private void initializeLocationSearch() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.

        // Initialize the SDK
        Places.initialize(requireContext(), BuildConfig.API_KEY);
        Places.createClient(requireContext());

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setTypeFilter(TypeFilter.CITIES)
                .build(requireContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
}
