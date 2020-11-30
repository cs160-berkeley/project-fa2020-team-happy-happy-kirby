package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.events_view;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.events_view.EventsViewRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FetchEventsHappeningSoonWithFilterUseCase extends BaseUseCase<List<Event>, FetchEventsHappeningSoonWithFilterUseCase.Params> {

    private EventsViewRepository eventsViewRepository;

    public FetchEventsHappeningSoonWithFilterUseCase(EventsViewRepository eventsViewRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.eventsViewRepository = eventsViewRepository;
    }

    @Override
    protected Observable<List<Event>> createObservableUseCase(Params params) {
        return eventsViewRepository
                .fetchEventsHappeningSoonWithFilter(params.timeStamp, params.city, params.causes)
                .map(snapshots -> { // Convert snapshots into Event objects
                    List<Event> events = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : snapshots) {
                        if (documentSnapshot.exists()) {
                            events.add(documentSnapshot.toObject(Event.class));
                        }
                    }
                    return events;
                });
    }

    public static final class Params {

        private final Long timeStamp;
        private String city;
        private ArrayList<String> causes;

        private Params(Long timeStamp, String city, ArrayList<String> c) {
            this.timeStamp = timeStamp;
            this.city = city;
            this.causes = c;
        }

        public static Params fetchEventsHappeningSoon(Long timeStamp, String city, ArrayList<String> causes) {
            return new Params(timeStamp, city, causes);
        }
    }
}
