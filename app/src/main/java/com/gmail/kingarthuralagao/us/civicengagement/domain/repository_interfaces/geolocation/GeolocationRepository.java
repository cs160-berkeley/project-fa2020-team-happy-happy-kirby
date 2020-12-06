package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.geolocation;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.location.GeolocationResult;

import io.reactivex.rxjava3.core.Observable;


public interface GeolocationRepository {

    Observable<GeolocationResult> getGeolocation(String latlng, String key);

}
