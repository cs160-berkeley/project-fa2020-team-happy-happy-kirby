package com.gmail.kingarthuralagao.us.civicengagement.data.api.timezone;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone.TimeZone;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITimeZone {

    @GET("json")
    Observable<TimeZone> getTimeZone(@Query("location") String latlng,
                                              @Query("timestamp") Long timeStamp,
                                              @Query("key") String api_key);
}