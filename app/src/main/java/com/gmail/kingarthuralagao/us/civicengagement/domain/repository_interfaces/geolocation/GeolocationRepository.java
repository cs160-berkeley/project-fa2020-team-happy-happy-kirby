package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.geolocation;

import com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark.adapter.LandmarkResultsAdapter;

import io.reactivex.rxjava3.core.Observable;


public interface GeolocationRepository {

    Observable<LandmarkResultsAdapter.LandmarkEntity> getGeolocation(String latlng, String key);

}
