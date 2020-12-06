package com.gmail.kingarthuralagao.us.civicengagement.data.api.geolocation

import com.gmail.kingarthuralagao.us.civicengagement.data.model.location.Results
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IGeolocationAPI {
    @GET("json")
    suspend fun getResults(@Query("latlng") latlng: String?,
                           @Query("key") api_key: String?) : Response<Results>
}