package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.geolocation;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.location.GeolocationResult;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.geolocation.GeolocationRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetGeolocationUseCase extends BaseUseCase<GeolocationResult, GetGeolocationUseCase.Params> {

    private GeolocationRepository geolocationRepository;

    public GetGeolocationUseCase(GeolocationRepository geolocationRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.geolocationRepository = geolocationRepository;
    }

    @Override
    protected Observable<GeolocationResult> createObservableUseCase(Params params) {
        return geolocationRepository.getGeolocation(params.location, params.key);
    }

    public static final class Params {

        private String location;
        private String key;

        private Params(String location, String key) {
            this.location = location;
            this.key = key;
        }

        public static Params getGeolocation(String location, String key) {
            return new Params(location, key);
        }
    }
}
