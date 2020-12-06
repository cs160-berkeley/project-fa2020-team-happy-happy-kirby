package com.gmail.kingarthuralagao.us.civicengagement.presentation.home.viewmodel;

import androidx.lifecycle.ViewModel;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.StateLiveData;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.location.GeolocationResult;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.geolocation.GeolocationRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.geolocation.GetGeolocationUseCase;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class HomeFragmentViewModel extends ViewModel {
    public StateLiveData<GeolocationResult> getGeolocationResponse = new StateLiveData<>();

    private GetGeolocationUseCase getGeolocationUseCase =
            new GetGeolocationUseCase(GeolocationRepositoryImpl.newInstance());

    @Override
    protected void onCleared() {
        super.onCleared();
        getGeolocationUseCase.dispose();
    }

    public void getGeolocation(String latlng, String key) {
        getGeolocationResponse.postLoading();

        DisposableObserver<GeolocationResult> disposableObserver = new DisposableObserver<GeolocationResult>() {
            @Override
            public void onNext(@NonNull GeolocationResult geolocationResult) {
                getGeolocationResponse.postSuccess(geolocationResult);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getGeolocationResponse.postError(e);
            }

            @Override
            public void onComplete() {
            }
        };

        getGeolocationUseCase.execute(disposableObserver, GetGeolocationUseCase.Params.getGeolocation(latlng, key));

    }
}
