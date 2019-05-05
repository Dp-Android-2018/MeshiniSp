package com.dp.meshinisp.utility.utils.firebase.classes;

import androidx.annotation.NonNull;

import com.dp.meshinisp.view.ui.callback.ActiveTripCallback;
import com.dp.meshinisp.view.ui.callback.ActiveTripDataCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDataBase {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ActiveTripFirebase activeTripFirebase;
    private int userId;
    private ActiveTripCallback activeTripCallback;
    private ActiveTripDataCallback activeTripDataCallback;

    public FirebaseDataBase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("active_trips_service_provider");
        activeTripFirebase = new ActiveTripFirebase();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(String.valueOf(userId)).getValue() != null) {
                    activeTripCallback.hasActiveTrip(true);
                    activeTripFirebase = dataSnapshot.child(String.valueOf(userId)).getValue(ActiveTripFirebase.class);
                    if(activeTripDataCallback!=null) {
                        activeTripDataCallback.ActiveTripData(activeTripFirebase);
                    }
                } else {
                    activeTripCallback.hasActiveTrip(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void setUserId(int userId, ActiveTripCallback activeTripCallback) {
        this.userId = userId;
        this.activeTripCallback = activeTripCallback;
//        System.out.println("user id in firebase : " + userId);
    }

    public void setActiveTripDataCallback(ActiveTripDataCallback activeTripDataCallback) {
        this.activeTripDataCallback = activeTripDataCallback;
    }
}