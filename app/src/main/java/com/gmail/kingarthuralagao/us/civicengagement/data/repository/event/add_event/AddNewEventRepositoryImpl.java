package com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.add_event;

import android.util.Log;

import com.gmail.kingarthuralagao.us.civicengagement.data.api.timezone.ITimeZone;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone.TimeZone;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.events_view.EventsViewRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.add_event.AddNewEventRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNewEventRepositoryImpl implements AddNewEventRepository {

    public static synchronized AddNewEventRepositoryImpl newInstance() {
        if (instance == null) {
            instance = new AddNewEventRepositoryImpl();
        }
        return instance;
    }

    private static AddNewEventRepositoryImpl instance;
    private final String TAG = getClass().getSimpleName();
    private RetrofitClient retrofitClient;

    public AddNewEventRepositoryImpl() {
        retrofitClient = new RetrofitClient();
    }

    @Override
    public Observable<TimeZone> getTimeZone(String location, Long timeStamp, String apiKey) {
        return retrofitClient.getTimeZoneAPI().getTimeZone(location, timeStamp, apiKey);
    }

    public static class RetrofitClient {
        private String BASE_URL = "https://maps.googleapis.com/maps/api/timezone/";
        private Gson gson;
        private Retrofit retrofitClient;
        ITimeZone timeZoneAPI;
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
            timeZoneAPI = retrofitClient.create(ITimeZone.class);
        }

        public ITimeZone getTimeZoneAPI() {
            return timeZoneAPI;
        }
    }
}
