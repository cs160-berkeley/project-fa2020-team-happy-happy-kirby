package com.gmail.kingarthuralagao.us.civicengagement.data.repository.geolocation;

import com.gmail.kingarthuralagao.us.civicengagement.data.api.geolocation.IGeolocation;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.location.GeolocationResult;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.geolocation.GeolocationRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeolocationRepositoryImpl implements GeolocationRepository {

    public static synchronized GeolocationRepositoryImpl newInstance() {
        if (instance == null) {
            instance = new GeolocationRepositoryImpl();
        }
        return instance;
    }

    private static GeolocationRepositoryImpl instance;
    private RetrofitClient retrofitClient;
    private final String TAG = getClass().getSimpleName();

    public GeolocationRepositoryImpl() {
        retrofitClient = new GeolocationRepositoryImpl.RetrofitClient();
    }
    @Override
    public Observable<GeolocationResult> getGeolocation(String latlng, String key) {
        return retrofitClient.getGeolocation().getResults(latlng, key).map(results -> {
            if (results.getGeolocationResults().size() > 0) {
                GeolocationResult geolocationResult = results.getGeolocationResults().get(0);
                return geolocationResult;
            }
            return null;
        });
    }

    public static class RetrofitClient {
        private String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/";
        private Gson gson;
        private Retrofit retrofitClient;
        IGeolocation geolocation;
        HttpLoggingInterceptor httpLoggingInterceptor;

        public RetrofitClient() {
            httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            gson = new GsonBuilder().setLenient().create();
            retrofitClient =
                    new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .build();
            geolocation= retrofitClient.create(IGeolocation.class);
        }

        public IGeolocation getGeolocation() {
            return geolocation;
        }

    }
}
