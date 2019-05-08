package com.dp.meshinisp.utility.utils.firebase.classes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
                        return;
                    }
                    deviceToken.setValue(task.getResult().getToken());
                });
        return deviceToken;
    }

}
