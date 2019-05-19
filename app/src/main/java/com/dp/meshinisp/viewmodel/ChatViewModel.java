package com.dp.meshinisp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dp.meshinisp.service.model.request.NotificationRequest;
import com.dp.meshinisp.service.repository.remotes.ChatRepository;

import kotlin.Lazy;
import retrofit2.Response;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class ChatViewModel extends AndroidViewModel {

    private Lazy<ChatRepository> chatRepositoryLazy = inject(ChatRepository.class);

    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response<Void>> sendNotification(NotificationRequest notificationRequest) {
        return chatRepositoryLazy.getValue().sendNotification(notificationRequest);
    }


}
