package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.InvalidEmailException;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.StateLiveData;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signup.SignUpRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.events_view.EventsViewRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.CheckIfEmailIsTakenUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.CreateUserWithEmailAndPasswordUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.InitializeUserUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.SetUserDisplayNameUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.SignUpWithGoogleUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningNowUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningNowWithFilterUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningSoonUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningSoonWithFilterUseCase;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class EventsViewViewModel extends ViewModel {
    public StateLiveData<List<Event>> fetchEventsHappeningNowResponse = new StateLiveData<>();
    public StateLiveData<List<Event>> fetchEventsHappeningSoonResponse = new StateLiveData<>();
    public StateLiveData<List<Event>> fetchEventsHappeningNowWithFilterResponse = new StateLiveData<>();
    public StateLiveData<List<Event>> fetchEventsHappeningSoonWithFilterResponse = new StateLiveData<>();

    private FetchEventsHappeningNowUseCase fetchEventsHappeningNowUseCase =
            new FetchEventsHappeningNowUseCase(EventsViewRepositoryImpl.newInstance());

    private FetchEventsHappeningSoonUseCase fetchEventsHappeningSoonUseCase =
            new FetchEventsHappeningSoonUseCase(EventsViewRepositoryImpl.newInstance());

    private FetchEventsHappeningNowWithFilterUseCase fetchEventsHappeningNowWithFilterUseCase =
            new FetchEventsHappeningNowWithFilterUseCase(EventsViewRepositoryImpl.newInstance());

    private FetchEventsHappeningSoonWithFilterUseCase fetchEventsHappeningSoonWithFilterUseCase =
            new FetchEventsHappeningSoonWithFilterUseCase(EventsViewRepositoryImpl.newInstance());

    @Override
    protected void onCleared() {
        super.onCleared();
        fetchEventsHappeningNowUseCase.dispose();
    }

    public void fetchEventsHappeningNow(Long timeStamp, String city) {
        fetchEventsHappeningNowResponse.postLoading();

        DisposableObserver<List<Event>> disposableObserver = new DisposableObserver<List<Event>>() {
            @Override
            public void onNext(@NonNull List<Event> events) {
                fetchEventsHappeningNowResponse.postSuccess(events);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fetchEventsHappeningNowResponse.postError(e);
            }

            @Override
            public void onComplete() {

            }
        };

        fetchEventsHappeningNowUseCase.execute(disposableObserver, FetchEventsHappeningNowUseCase.Params.fetchEventsHappeningNow(timeStamp, city));
    }

    public void fetchEventsHappeningNowWithFilter(Long timeStamp, String city, ArrayList<String> causes) {
        fetchEventsHappeningNowWithFilterResponse.postLoading();

        DisposableObserver<List<Event>> disposableObserver = new DisposableObserver<List<Event>>() {
            @Override
            public void onNext(@NonNull List<Event> events) {
                fetchEventsHappeningNowWithFilterResponse.postSuccess(events);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fetchEventsHappeningNowWithFilterResponse.postError(e);
            }

            @Override
            public void onComplete() {

            }
        };

        fetchEventsHappeningNowWithFilterUseCase.execute(disposableObserver, FetchEventsHappeningNowWithFilterUseCase.Params.fetchEventsHappeningNow(timeStamp, city, causes));
    }

    public void fetchEventsHappeningSoon(Long timeStamp, String city) {
        fetchEventsHappeningSoonResponse.postLoading();

        DisposableObserver<List<Event>> disposableObserver = new DisposableObserver<List<Event>>() {
            @Override
            public void onNext(@NonNull List<Event> events) {
                fetchEventsHappeningSoonResponse.postSuccess(events);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fetchEventsHappeningSoonResponse.postError(e);
            }

            @Override
            public void onComplete() {

            }
        };

        fetchEventsHappeningSoonUseCase.execute(disposableObserver, FetchEventsHappeningSoonUseCase.Params.fetchEventsHappeningSoon(timeStamp, city));
    }

    public void fetchEventsHappeningSoonWithFilter(Long timeStamp, String city, ArrayList<String> causes) {
        fetchEventsHappeningSoonWithFilterResponse.postLoading();

        DisposableObserver<List<Event>> disposableObserver = new DisposableObserver<List<Event>>() {
            @Override
            public void onNext(@NonNull List<Event> events) {
                fetchEventsHappeningSoonWithFilterResponse.postSuccess(events);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fetchEventsHappeningSoonWithFilterResponse.postError(e);
            }

            @Override
            public void onComplete() {

            }
        };

        fetchEventsHappeningSoonWithFilterUseCase.execute(disposableObserver, FetchEventsHappeningSoonWithFilterUseCase.Params.fetchEventsHappeningSoon(timeStamp, city, causes));
    }
}
