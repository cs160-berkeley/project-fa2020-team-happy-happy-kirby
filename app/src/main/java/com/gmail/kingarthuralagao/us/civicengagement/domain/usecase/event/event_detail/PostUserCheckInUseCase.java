package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.event_detail;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.event_detail.EventDetailRepository;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.events_view.EventsViewRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PostUserCheckInUseCase extends BaseUseCase<Status, PostUserCheckInUseCase.Params> {

    private EventDetailRepository eventDetailRepository;

    public PostUserCheckInUseCase(EventDetailRepository eventDetailRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.eventDetailRepository = eventDetailRepository;
    }

    @Override
    protected Observable<Status> createObservableUseCase(Params params) {
        return eventDetailRepository.postEventCheckIn(params.eventID, params.userID);
    }

    public static final class Params {

        private String eventID;
        private String userID;

        private Params(String eventID, String userID) {
            this.eventID = eventID;
            this.userID = userID;
        }

        public static Params postUserCheckIn(String eventID, String userID) {
            return new Params(eventID, userID);
        }
    }
}
