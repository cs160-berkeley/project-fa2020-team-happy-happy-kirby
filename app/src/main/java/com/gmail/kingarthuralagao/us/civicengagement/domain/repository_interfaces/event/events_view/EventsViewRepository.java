package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.events_view;

import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;


public interface EventsViewRepository {

    Observable<Map<String, Object>> fetchEventsHappeningNow(Long timeStamp);

    Observable<Status> fetchEventsHappeningSoon(Long timeStamp);
}
