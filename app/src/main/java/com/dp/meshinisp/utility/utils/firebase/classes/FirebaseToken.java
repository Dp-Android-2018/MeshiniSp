package com.dp.meshinisp.utility.utils.firebase.classes;

import com.dp.meshinisp.utility.utils.SharedUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FirebaseToken {

    private static FirebaseToken instance;

    private FirebaseToken() {
    }

    public static FirebaseToken getInstance() {
        if (instance == null) {
            instance = new FirebaseToken();
        }
        return instance;
    }

    public LiveData<String> getFirebaseToken() {
        MutableLiveData<String> deviceToken = new MutableLiveData<>();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener((Task<InstanceIdResult> task) -> {
                    if (!task.isSuccessful()) {
                        System.out.println("device Token 1: Failed");
                        return;
                    }
                    // Get new Instance ID token
                    deviceToken.setValue(task.getResult().getToken());
                    System.out.println("device Token 1:" + deviceToken.getValue());
                });
        return deviceToken;
    }

}
