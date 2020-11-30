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

public class FetchEventsHappeningNowWithFilterUseCase extends BaseUseCase<List<Event>, FetchEventsHappeningNowWithFilterUseCase.Params> {

    private EventsViewRepository eventsViewRepository;

    public FetchEventsHappeningNowWithFilterUseCase(EventsViewRepository eventsViewRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.eventsViewRepository = eventsViewRepository;
    }

    @Override
    protected Observable<List<Event>> createObservableUseCase(Params params) {
        return eventsViewRepository
                .fetchEventsHappeningNowWithFilter(params.timeStamp, params.city, params.causes)
                .map(documentSnapshots -> { // Convert snapshots into Event objects
                    List<Event> events = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        if (documentSnapshot.exists()) {
                            Event e = documentSnapshot.toObject(Event.class);
                            if (e.getDateEnd() > params.timeStamp / 1000) {
                                events.add(e);
                            }
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

        public static Params fetchEventsHappeningNow(Long timeStamp, String city, ArrayList<String> causes) {
            return new Params(timeStamp, city, causes);
        }
    }
}
