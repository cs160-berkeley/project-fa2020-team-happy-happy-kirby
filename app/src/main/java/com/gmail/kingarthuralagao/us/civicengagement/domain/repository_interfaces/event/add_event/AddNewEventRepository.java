package com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.add_event;

import com.gmail.kingarthuralagao.us.civicengagement.data.model.timezone.TimeZone;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;


public interface AddNewEventRepository {

    Observable<TimeZone> getTimeZone(String location, Long timeStamp, String apiKey);

    //Observable<List<DocumentSnapshot>> fetchEventsHappeningSoon(Long timeStamp, String city);
}
