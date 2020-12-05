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
    lateinit var coroutineScope : Job

    fun fetchLocations1(landmarks : List<LandmarkResultWrapper>) {
        landmarkList = landmarks

        var landmarkEntities : MutableList<LandmarkResultsAdapter.LandmarkEntity> = mutableListOf()
        coroutineScope = CoroutineScope(Dispatchers.IO).launch {
            result.postLoading()
            var deferredList : MutableList<Deferred<String>> = mutableListOf()
            for (landmark in landmarkList!!) {
                val latlng = "${landmark.lat},${landmark.lon}"
                var deferred = async { getLocationFromAPI(latlng)}
                deferredList.add(deferred)
            }

            for (i in 0 until deferredList.size) {
                try {
                    val address : String = deferredList[i].await()
                    val landmarkName = landmarkList!![i].landmarkName
                    landmarkEntities.add(LandmarkResultsAdapter.LandmarkEntity(landmarkName, address))
                    Log.i("Inside For Loop", address)
                } catch (e : Exception) {
                    e.printStackTrace()
                    landmarkEntities.add(LandmarkResultsAdapter.LandmarkEntity("", ""))
                }
            }

            result.postSuccess(landmarkEntities)
            //emit(landmarkEntities)
        }
    }

    private suspend fun getLocationFromAPI(latlng : String) : String {
        var result = ""

        val key = "AIzaSyCt5BNGo9os5a-45NEJE9A-hN8cITF-nLA"
        try {
            val response = RetrofitClient.geolocationApi.getResults(latlng, key)
            val body = response.body()

            Log.i("GeolocationVM", response.toString())
            if (body != null) {
                result = body.geolocationResults[0]?.formattedAddress.toString()
                Log.i("GeolocationVM", body.geolocationResults[0]?.formattedAddress.toString())
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
    /*public class UploadLandmarkImageViewModel extends ViewModel {
        public StateLiveData<LandmarkResultsAdapter.LandmarkEntity> getGeolocationResponse = new StateLiveData<>();

        private GetGeolocationUseCase getGeolocationUseCase =
                new GetGeolocationUseCase(GeolocationRepositoryImpl.newInstance());

        @Override
        protected void onCleared() {
            super.onCleared();
            getGeolocationUseCase.dispose();
        }

        public void getGeolocation(String latlng, String key) {
            getGeolocationResponse.postLoading();

            DisposableObserver<LandmarkResultsAdapter.LandmarkEntity> disposableObserver = new DisposableObserver<LandmarkResultsAdapter.LandmarkEntity>() {
                @Override
                public void onNext(LandmarkResultsAdapter.@NonNull LandmarkEntity landmarkEntity) {
                    getGeolocationResponse.postSuccess(landmarkEntity);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    getGeolocationResponse.postError(e);
                }

                @Override
                public void onComplete() {
                    Log.i("UploadLandmark", "Oncomplete");
                }
            };

            getGeolocationUseCase.execute(disposableObserver, GetGeolocationUseCase.Params.getGeolocation(latlng, key));

        }
    }*/
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

