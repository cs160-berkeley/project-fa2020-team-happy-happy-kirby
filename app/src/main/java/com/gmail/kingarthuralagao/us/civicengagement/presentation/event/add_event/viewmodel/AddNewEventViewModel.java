package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.add_event.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.gmail.kingarthuralagao.us.civicengagement.core.networking.DisposableObserverWrapper;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.StateLiveData;
import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone.TimeZone;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.add_event.AddNewEventRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.events_view.EventsViewRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.add_event.GetTimezoneUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.add_event.PostEventUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningNowUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningSoonUseCase;
import com.google.gson.JsonElement;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import retrofit2.Response;

public class AddNewEventViewModel extends ViewModel {
    public StateLiveData<TimeZone> getTimeZoneResponse = new StateLiveData<>();
    public StateLiveData<Status> postEventResponse = new StateLiveData<>();

    private GetTimezoneUseCase getTimezoneUseCase =
            new GetTimezoneUseCase(AddNewEventRepositoryImpl.newInstance());

    private PostEventUseCase postEventUseCase =
            new PostEventUseCase(AddNewEventRepositoryImpl.newInstance());

    @Override
    protected void onCleared() {
        super.onCleared();
        getTimezoneUseCase.dispose();
        postEventUseCase.dispose();
    }

    public void getTimeZone(String location, Long timeStamp, String key) {
        getTimeZoneResponse.postLoading();

        DisposableObserver<TimeZone> disposableObserver = new DisposableObserver<TimeZone>() {
            @Override
            public void onNext(@NonNull TimeZone timeZone) {
                Log.i("AddNewEventViewModel", "Success + " + timeZone.getTimeZoneName() + " " + timeZone.getStatus());
                getTimeZoneResponse.postSuccess(timeZone);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("AddNewEventViewModel", "Fail: " + e.getMessage());
                getTimeZoneResponse.postError(e);
            }

            @Override
            public void onComplete() {
                Log.i("AddNewEventViewModel", "Complete: ");
            }
        };

        getTimezoneUseCase.execute(disposableObserver, GetTimezoneUseCase.Params.getTimeZone(location, timeStamp, key));
    }

    public void postEvent(Event e) {
        postEventResponse.postLoading();

        DisposableObserver<Status> disposableObserver = new DisposableObserver<Status>() {
            @Override
            public void onNext(@NonNull Status status) {
                postEventResponse.postSuccess(status);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                postEventResponse.postError(e);
            }

            @Override
            public void onComplete() {}
        };

        postEventUseCase.execute(disposableObserver, PostEventUseCase.Params.postEvent(e));
    }

}
