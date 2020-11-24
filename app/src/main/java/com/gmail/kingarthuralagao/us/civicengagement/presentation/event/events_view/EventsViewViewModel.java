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
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class EventsViewViewModel extends ViewModel {
    public StateLiveData<Map<String, Object>> fetchEventsHappeningNowResponse = new StateLiveData<>();

    private FetchEventsHappeningNowUseCase fetchEventsHappeningNowUseCase =
            new FetchEventsHappeningNowUseCase(EventsViewRepositoryImpl.newInstance());

    @Override
    protected void onCleared() {
        super.onCleared();
        fetchEventsHappeningNowUseCase.dispose();
    }

    public void fetchEventsHappeningNow(Long timeStamp, String city) {
        fetchEventsHappeningNowResponse.postLoading();

        DisposableObserver<Map<String, Object>> disposableObserver = new DisposableObserver<Map<String, Object>>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Map<String, Object> stringObjectMap) {
                fetchEventsHappeningNowResponse.postSuccess(stringObjectMap);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                fetchEventsHappeningNowResponse.postError(e);
            }

            @Override
            public void onComplete() {}
        };

        fetchEventsHappeningNowUseCase.execute(disposableObserver, FetchEventsHappeningNowUseCase.Params.fetchEventsHappeningNow(timeStamp, city));
    }
}
