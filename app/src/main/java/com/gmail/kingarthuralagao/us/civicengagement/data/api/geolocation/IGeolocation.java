package com.gmail.kingarthuralagao.us.civicengagement.data.api.geolocation;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.location.Results;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGeolocation {

    @GET("json")
    Observable<Results> getResults(@Query("latlng") String latlng,
                                   @Query("key") String api_key);
}