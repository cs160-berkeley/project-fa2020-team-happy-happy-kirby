package com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.events_view;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.data.repository.authentication.signin.SignInRepositoryImpl;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.events_view.EventsViewRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class EventsViewRepositoryImpl implements EventsViewRepository {

    public static synchronized EventsViewRepositoryImpl newInstance() {
        if (instance == null) {
            instance = new EventsViewRepositoryImpl();
        }
        return instance;
    }

    private static EventsViewRepositoryImpl instance;
    private final String TAG = getClass().getSimpleName();

    @Override
    public Observable<Map<String, Object>> fetchEventsHappeningNow(Long timeStamp, String city) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Called");

        Log.d(TAG, "City : " + city);
        Log.d(TAG, "TimeStamp : " + (timeStamp / 1000));
        Observable<Map<String, Object>> observable = Observable.create(emitter -> {
            db.collection("events")
                    .whereEqualTo("city", city)
                    .whereLessThan("dateStart", timeStamp / 1000)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    emitter.onNext(document.getData());
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                Log.d(TAG, task.getResult().getDocuments().toString());
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                emitter.onError(task.getException());
                            }
                            emitter.onComplete();
                        }
                    });
        });

        return observable;
    }

    @Override
    public Observable<Status> fetchEventsHappeningSoon(Long timeStamp) {
        return null;
    }
}
