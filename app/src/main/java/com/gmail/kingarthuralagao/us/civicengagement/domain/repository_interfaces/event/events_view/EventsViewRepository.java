package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.events_view;

import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.model.user.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;


public interface EventsViewRepository {

    Observable<List<DocumentSnapshot>> fetchEventsHappeningNow(Long timeStamp, String city);

    Observable<List<DocumentSnapshot>> fetchEventsHappeningSoon(Long timeStamp, String city);
}
