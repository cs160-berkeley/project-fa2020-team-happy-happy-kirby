package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.add_event;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone.TimeZone;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.add_event.AddNewEventRepository;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.events_view.EventsViewRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

public class GetTimezoneUseCase extends BaseUseCase<TimeZone, GetTimezoneUseCase.Params> {

    private AddNewEventRepository addNewEventRepository;

    public GetTimezoneUseCase(AddNewEventRepository addNewEventRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.addNewEventRepository = addNewEventRepository;
    }

    @Override
    protected Observable<TimeZone> createObservableUseCase(Params params) {
        return addNewEventRepository.getTimeZone(params.location, params.timeStamp, params.key);
    }

    public static final class Params {

        private String location;
        private final Long timeStamp;
        private String key;

        private Params(String location, Long timeStamp, String key) {
            this.location = location;
            this.timeStamp = timeStamp;
            this.key = key;
        }

        public static Params getTimeZone(String location, Long timeStamp, String city) {
            return new Params(location, timeStamp, city);
        }
    }
}
