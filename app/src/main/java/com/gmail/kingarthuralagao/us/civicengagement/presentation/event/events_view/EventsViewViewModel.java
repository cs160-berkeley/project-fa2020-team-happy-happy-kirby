package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.events_view;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.InvalidEmailException;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.StateLiveData;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.Utils;
import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signup.SignUpRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.events_view.EventsViewRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.CheckIfEmailIsTakenUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.CreateUserWithEmailAndPasswordUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.InitializeUserUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.SetUserDisplayNameUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.authentication.signup.SignUpWithGoogleUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningNowUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningSoonUseCase;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class EventsViewViewModel extends ViewModel {
    public StateLiveData<List<DocumentSnapshot>> fetchEventsHappeningNowResponse = new StateLiveData<>();
    public StateLiveData<List<DocumentSnapshot>> fetchEventsHappeningSoonResponse = new StateLiveData<>();

    private FetchEventsHappeningNowUseCase fetchEventsHappeningNowUseCase =
            new FetchEventsHappeningNowUseCase(EventsViewRepositoryImpl.newInstance());

    private FetchEventsHappeningSoonUseCase fetchEventsHappeningSoonUseCase =
            new FetchEventsHappeningSoonUseCase(EventsViewRepositoryImpl.newInstance());

    @Override
    protected void onCleared() {
        super.onCleared();
        fetchEventsHappeningNowUseCase.dispose();
    }

    public void fetchEventsHappeningNow(Long timeStamp, String city) {
        fetchEventsHappeningNowResponse.postLoading();

        DisposableObserver<List<DocumentSnapshot>> disposableObserver = new DisposableObserver<List<DocumentSnapshot>>() {
            @Override
            public void onNext(@NonNull List<DocumentSnapshot> documentSnapshots) {
                fetchEventsHappeningNowResponse.postSuccess(documentSnapshots);
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

    public void fetchEventsHappeningSoon(Long timeStamp, String city) {
        fetchEventsHappeningSoonResponse.postLoading();

        DisposableObserver<List<DocumentSnapshot>> disposableObserver = new DisposableObserver<List<DocumentSnapshot>>() {
            @Override
            public void onNext(@NonNull List<DocumentSnapshot> documentSnapshots) {
                fetchEventsHappeningSoonResponse.postSuccess(documentSnapshots);
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
}
