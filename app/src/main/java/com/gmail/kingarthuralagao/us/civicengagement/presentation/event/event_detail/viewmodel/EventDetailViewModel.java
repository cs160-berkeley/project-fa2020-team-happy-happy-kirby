package com.gmail.kingarthuralagao.us.civicengagement.presentation.event.event_detail.viewmodel;

import androidx.lifecycle.ViewModel;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.StateLiveData;
import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.event_detail.EventDetailRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.events_view.EventsViewRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.add_event.PostEventUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.event_detail.PostUserCheckInUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningNowUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view.FetchEventsHappeningSoonUseCase;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class EventDetailViewModel extends ViewModel {
    public StateLiveData<Status> postUserCheckInResponse = new StateLiveData<>();

    private PostUserCheckInUseCase postUserCheckInUseCase =
            new PostUserCheckInUseCase(EventDetailRepositoryImpl.newInstance());

    @Override
    protected void onCleared() {
        super.onCleared();
        postUserCheckInUseCase.dispose();
    }

    public void postUserCheckIn(String eventID, String userID) {
        postUserCheckInResponse.postLoading();

        DisposableObserver<Status> disposableObserver = new DisposableObserver<Status>() {
            @Override
            public void onNext(@NonNull Status status) {
                postUserCheckInResponse.postSuccess(status);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                postUserCheckInResponse.postError(e);
            }

            @Override
            public void onComplete() {}
        };

        postUserCheckInUseCase.execute(disposableObserver, PostUserCheckInUseCase.Params.postUserCheckIn(eventID, userID));
    }
}
