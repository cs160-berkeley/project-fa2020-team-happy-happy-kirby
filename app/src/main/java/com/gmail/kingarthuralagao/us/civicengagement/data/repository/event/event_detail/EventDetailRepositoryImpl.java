package com.gmail.kingarthuralagao.us.civicengagement.data.repository.event.event_detail;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gmail.kingarthuralagao.us.civicengagement.data.Status;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.event_detail.EventDetailRepository;
import com.gmail.kingarthuralagao.us.civicengagement.domain.repository_interfaces.event.events_view.EventsViewRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class EventDetailRepositoryImpl implements EventDetailRepository {

    public static synchronized EventDetailRepositoryImpl newInstance() {
        if (instance == null) {
            instance = new EventDetailRepositoryImpl();
        }
        return instance;
    }

    private static EventDetailRepositoryImpl instance;
    private final String TAG = getClass().getSimpleName();

    @Override
    public Observable<Status> postEventCheckIn(String eventID, String userID) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        WriteBatch batch = db.batch();

        DocumentReference eventDocRef = db.collection("events").document(eventID);
        batch.update(eventDocRef,"checkIns", FieldValue.increment(1));

        DocumentReference userDocRef = db.collection("Users").document(userID);
        batch.update(userDocRef, "checkIns", FieldValue.arrayUnion(eventID));

        Observable<Status> observable = Observable.create(emitter -> {
            batch.commit()
                    .addOnCompleteListener(task -> {
                        emitter.onNext(Status.SUCCESS);
                        emitter.onComplete();
                    })
                    .addOnFailureListener(e -> {
                        emitter.onError(e);
                        emitter.onComplete();
                    });
        });

        return observable;
    }
}
