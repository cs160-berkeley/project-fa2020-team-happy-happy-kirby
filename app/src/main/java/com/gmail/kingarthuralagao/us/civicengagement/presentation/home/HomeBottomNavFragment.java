package com.gmail.kingarthuralagao.us.civicengagement.presentation.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gmail.kingarthuralagao.us.civicengagement.CivicEngagementApp;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.LoadingDialog;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.authentication.AuthenticationActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.AddEventActivity;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.home.viewmodel.HomeFragmentViewModel;
import com.gmail.kingarthuralagao.us.civilengagement.BuildConfig;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentHomeBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class HomeBottomNavFragment extends Fragment {

    public interface IOnNavigateToEventsView {
        void onNavigateToEventsView(String location);
    }

    public static HomeBottomNavFragment newInstance() {
        return new HomeBottomNavFragment();
    }

    private static final int AUTOCOMPLETE_REQUEST_CODE = 100;
    private final int LOCATION_REQUEST_CODE = 1;
    private final String TAG = getClass().getSimpleName();
    private static Geocoder geocoder;
    private FragmentHomeBinding binding;
    private LocationManager mLocManager;
    private LocationListener mLocListener;
    private RequestQueue queue;
    private HomeFragmentViewModel viewModel;
    private LoadingDialog loadingDialog;
    private IOnNavigateToEventsView navigateToEventsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        mLocManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        initializeLocationListener();
        queue = Volley.newRequestQueue(this.getActivity());

        if (getParentFragment() instanceof IOnNavigateToEventsView) {
            navigateToEventsView = (IOnNavigateToEventsView) getParentFragment();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_menu);
        binding.toolbar.setOverflowIcon(drawable);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpEvents();
        subscribeToLiveData();
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
        inflater.inflate(R.menu.home_menu_bottom_nav, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            ((CivicEngagementApp) requireActivity().getApplication()).getAuthInstance().signOut();
            Intent i = new Intent(requireActivity(), AuthenticationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                Log.i(TAG, "Address comp: " + place.getAddressComponents() + " " + place.toString());
                String address = place.getAddress();
                String city = getCityFromPlace(place);
                /*
                int commaIndex = address.indexOf(",");
                String city = address.substring(0, commaIndex);
                Log.i(TAG, "Address comp: " + place.getAddressComponents());*/
                navigateToEventsView(city);
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
            Intent i = new Intent(requireActivity(), AddEventActivity.class);
            startActivity(i);
            requireActivity().overridePendingTransition(R.anim.pull_up_from_bottom, R.anim.do_nothing);
            /*
            AddNewEventDialogFragment fragment = AddNewEventDialogFragment.newInstance();
            fragment.show(getChildFragmentManager(), "");*/
        });

        binding.searchForLocationTv.setOnClickListener(view -> {
            initializeLocationSearch();
        });

        binding.useCurrentLocationTv.setOnClickListener(view -> {
            Log.i(TAG, "In onclicklistener for location image");
            // Location permission granted.
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                // Update location.
                mLocManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
                loadingDialog = LoadingDialog.newInstance("");
                loadingDialog.setCancelable(false);
                loadingDialog.show(getChildFragmentManager(), "");
            } else {
                // Location permission not granted.
                Log.i(TAG, "Requesting location permission.");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        });
    }

    private void subscribeToLiveData() {
        /*
        viewModel.getGeolocationResponse.observe(this, geolocationResource -> {
            switch (geolocationResource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (geolocationResource.getData() != null) {
                        List<AddressComponent> addressComponents = geolocationResource.getData().getAddressComponents();
                        String city = "";
                        for (int i = 0; i < addressComponents.size(); i++) {
                            AddressComponent addressComponent = addressComponents.get(i);

                            List<String> types = addressComponent.getTypes();
                            for (int j = 0; j < types.size(); j++) {
                                if (types.get(j).equals("locality")) {
                                    city = addressComponent.getLongName();
                                }
                            }
                        }
                        navigateToEventsView(city);
                    } else {
                        Toasty.error(requireContext(), "Error getting location", Toasty.LENGTH_SHORT, true);
                    }
                    //loadingDialog.dismiss();
                    break;
                case ERROR:
                    Toasty.error(requireContext(), "Error getting location", Toasty.LENGTH_SHORT, true);
                    //loadingDialog.dismiss();
                    break;
                default:
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                loadingDialog = LoadingDialog.newInstance("");
                loadingDialog.setCancelable(false);
                loadingDialog.show(getChildFragmentManager(), "");
                mLocManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
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

    private void initializeLocationListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mLocListener = location -> {
                if (location != null) {
                    Log.i(TAG, "Location: " + location.toString());
                    //viewModel.getGeolocation(location.getLatitude() + "," + location.getLongitude(), BuildConfig.API_KEY);
                    makeGeoRequest(location);
                    mLocManager.removeUpdates(mLocListener);
                }
            };
        } else {
            mLocListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if (location != null) {
                        Log.i(TAG, "Location: " + location.toString());
                        //makeGeoRequest(location);
                        viewModel.getGeolocation(location.getLatitude() + "," + location.getLongitude(), BuildConfig.API_KEY);
                        mLocManager.removeUpdates(mLocListener);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(@NonNull String provider) {}
                @Override
                public void onProviderDisabled(@NonNull String provider) {}
            };
        }
    }

    private void navigateToEventsView(String city) {
        navigateToEventsView.onNavigateToEventsView(city);

        /*
        Intent i = new Intent(requireActivity(), EventsViewActivity.class);
        i.putExtra("Address", city);
        /*
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(i);
        requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
    }

    private void initializeLocationSearch() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.

        // Initialize the SDK
        Places.initialize(requireContext(), BuildConfig.API_KEY);
        Places.createClient(requireContext());

        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setTypeFilter(TypeFilter.CITIES)
                .build(requireContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private static String getCityFromPlace(Place place) {
        geocoder = new Geocoder(CivicEngagementApp.getContext(), Locale.getDefault());

        try {
            return getCityNameByCoordinates(place.getLatLng().latitude, place.getLatLng().longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getCityNameByCoordinates(double lat, double lon) throws IOException {

        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {
            Log.i("AddNewEventDialogFrag", addresses.toString());
            return addresses.get(0).getLocality() == null ? addresses.get(0).getAdminArea() : addresses.get(0).getLocality();
        }
        return "";
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
                        loadingDialog.dismiss();
                        navigateToEventsView(city);
                    } catch (Exception e) {
                        loadingDialog.dismiss();
                        Log.e(TAG, "Error retrieving results from GeoCoding API: " + e);
                    }
                }
            }, error -> {
                loadingDialog.dismiss();
                Log.e(TAG, "Error getting response from GeoCoding API: " + error);
            });
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        }
    }
}