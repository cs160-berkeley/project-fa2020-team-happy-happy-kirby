package com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.LandmarkResultWrapper
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.StateLiveData
import com.gmail.kingarthuralagao.us.civicengagement.data.api.geolocation.IGeolocationAPI
import com.gmail.kingarthuralagao.us.civicengagement.data.model.location.GeolocationResult
import com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark.adapter.LandmarkResultsAdapter
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class UploadLandMarkImageViewModel : ViewModel() {

    private var landmarkList : List<LandmarkResultWrapper>? = null
    val result = StateLiveData<MutableList<LandmarkResultsAdapter.LandmarkEntity>>()
    private var coroutineScope : Job? = null

    fun fetchLocations1(landmarks : List<LandmarkResultWrapper>) {
        landmarkList = landmarks

        var landmarkEntities : MutableList<LandmarkResultsAdapter.LandmarkEntity> = mutableListOf()
        coroutineScope = CoroutineScope(Dispatchers.IO).launch {
            result.postLoading()
            var deferredList : MutableList<Deferred<GeolocationResult?>> = mutableListOf()
            for (landmark in landmarkList!!) {
                val latlng = "${landmark.lat},${landmark.lon}"
                var deferred = async { getLocationFromAPI(latlng)}
                deferredList.add(deferred)
            }

            for (i in 0 until deferredList.size) {
                try {
                    val result = deferredList[i].await()
                    val address : String = result?.formattedAddress.toString()
                    val city = getCity(result)
                    val landmarkName = landmarkList!![i].landmarkName
                    val lat = result?.geometry?.location?.lat
                    val lng = result?.geometry?.location?.lng
                    landmarkEntities.add(LandmarkResultsAdapter.LandmarkEntity(landmarkName, address, city, lat, lng))
                    Log.i("Inside For Loop", address)
                } catch (e : Exception) {
                    e.printStackTrace()
                    landmarkEntities.add(LandmarkResultsAdapter.LandmarkEntity("", "", "", 0.0, 0.0))
                }
            }

            result.postSuccess(landmarkEntities)
            //emit(landmarkEntities)
        }
    }

    private fun getCity(result: GeolocationResult?): String {
        var city = ""
        if (result != null) {
            val addressComponents = result.addressComponents
            for (i in 0 until addressComponents.size) {
                val addressComponent = addressComponents[i]

                val types = addressComponent.types;
                for (j in 0 until types.size) {
                    if (types[j] == "locality") {
                        city = addressComponent.longName;
                    }
                }
            }
        }
        return city
    }

    suspend fun getLocationFromAPI(latlng : String) : GeolocationResult? {
        val key = "AIzaSyCt5BNGo9os5a-45NEJE9A-hN8cITF-nLA"
        try {
            val response = RetrofitClient.geolocationApi.getResults(latlng, key)
            val body = response.body()

            Log.i("GeolocationVM", response.toString())
            if (body != null) {
                return body.geolocationResults[0]
                Log.i("GeolocationVM", body.geolocationResults[0]?.formattedAddress.toString())
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope?.cancel()
    }

}

object RetrofitClient {
    private val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"
    val gson = GsonBuilder().setLenient().create()

    val geolocationApi : IGeolocationAPI by lazy{
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        retrofit.create(IGeolocationAPI::class.java)
    }
}

