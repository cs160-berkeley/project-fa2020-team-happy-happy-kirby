package com.gmail.kingarthuralagao.us.civicengagement.domain.usecase.event.add_event;

import com.gmail.kingarthuralagao.us.civicengagement.core.base.BaseUseCase;
import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.event.Event;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone.TimeZone;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.add_event.AddNewEventRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PostEventUseCase extends BaseUseCase<Status, PostEventUseCase.Params> {

    private AddNewEventRepository addNewEventRepository;

    public PostEventUseCase(AddNewEventRepository addNewEventRepository) {
        super(Schedulers.io(), AndroidSchedulers.mainThread());
        this.addNewEventRepository = addNewEventRepository;
    }

    @Override
    protected Observable<Status> createObservableUseCase(Params params) {
        return addNewEventRepository.postEvent(params.event);
    }

    public static final class Params {

        private Event event;

        private Params(Event e) {
            event = e;
        }

        public static Params postEvent(Event e) {
            return new Params(e);
        }
    }
}
