package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.event_detail;

import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;


public interface EventDetailRepository {

    Observable<Status> postEventCheckIn(String eventID, String userID);
}
