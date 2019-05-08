package com.dp.meshinisp.view.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ActivityChatBinding;
import com.dp.meshinisp.service.model.global.ConversationHistory;
import com.dp.meshinisp.service.model.global.LastMessage;
import com.dp.meshinisp.service.model.global.Message;
import com.dp.meshinisp.service.model.global.RequestClientModel;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.utility.utils.firebase.classes.FirebaseToken;
import com.dp.meshinisp.view.ui.adapter.ChatRecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;

public class ChatActivity extends BaseActivity {

    ActivityChatBinding binding;
    private String deviceToken;
    public static boolean active = false;
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private DatabaseReference reference;
    public ObservableList<Message> messages;
    private List<Message> allMessages;
    public ObservableField<String> message;
    private ConversationHistory history;
    private int userId;
    private int tripId;
    private RequestClientModel clientData;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    LastMessage lastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        clientData = new Gson().fromJson(getIntent().getStringExtra(ConfigurationFile.Constants.USER_DATA), RequestClientModel.class);
        userId = clientData.getId();
        tripId = getIntent().getIntExtra(ConfigurationFile.Constants.TRIP_ID, 0);
        messages = new ObservableArrayList<>();
        allMessages = new ArrayList<>();
        message = new ObservableField<String>("");
        initializeUiData();
        getUserConversation();
        initializeChatRecyclerView();
        getLastMessage();
        FirebaseToken.getInstance().getFirebaseToken().observe(this, s -> deviceToken = s);
        setupToolbar();
        makeActionOnClickOnBtnSend();
    }

    private void initializeUiData() {
        binding.tvUserName.setText(clientData.getClientName());
        ImageView userImageView = binding.ivUser;
        Picasso.get().load(clientData.getProfilePictureUrl()).into(userImageView);
    }

    private void makeActionOnClickOnBtnSend() {
        binding.ivSendMessage.setOnClickListener(v -> {
            if (!binding.etMessage.getText().toString().equals("")) {
                addMessage(binding.etMessage.getText().toString(), "text");
                binding.etMessage.setText("");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    private void setupToolbar() {
        binding.chatToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        binding.chatToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void addMessage(String content, String type) {
        reference = FirebaseDatabase.getInstance().getReference("conversations/Meshini"
                + customUtilsLazy.getValue().getSavedMemberData().getUserId() + "-" + userId + "-" + tripId);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final Message newMessage = new Message(content, "serviceProvider-" + customUtilsLazy.getValue().getSavedMemberData().getUserId(), "user-" + userId, false, type, (timestamp.getTime()) / 1000);
        reference.push().setValue(newMessage).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Snackbar.make(binding.getRoot(), "Error Sending message", Snackbar.LENGTH_SHORT).show();
            } else {
//                binding.etMessage.setText("");
//                sendNotification();
            }
        });

        reference.child("last_message").setValue(newMessage);
    }


    public void getUserConversation() {
        reference = FirebaseDatabase.getInstance().getReference("conversations");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getRef().getKey();
//                hasData.set(View.VISIBLE);
//                noData.set(View.GONE);
                if (key.equals("Meshini"
                        + customUtilsLazy.getValue().getSavedMemberData().getUserId() + "-" + userId + "-" + tripId)) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Message message = (Message) dataSnapshot1.getValue(Message.class);
                        allMessages.add(message);
                    }
                    setAdapterMessages(allMessages);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addChildEventListener(childEventListener);
    }

    private void initializeChatRecyclerView() {
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(customUtilsLazy.getValue().getSavedMemberData().getUserId(), userId);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvMessages.setLayoutManager(linearLayoutManager);
        binding.rvMessages.setAdapter(chatRecyclerViewAdapter);
        linearLayoutManager.scrollToPositionWithOffset(chatRecyclerViewAdapter.getLastPosition(), 20);
        binding.rvMessages.setItemAnimator(new DefaultItemAnimator());
    }

    private void setAdapterMessages(List<Message> allMessages) {
        if (allMessages.size() != 1) {
            allMessages.remove(allMessages.get(allMessages.size() - 1));
            chatRecyclerViewAdapter.setData(allMessages);
        }
    }

    private void getLastMessage() {
        reference = FirebaseDatabase.getInstance().getReference("conversations/Meshini"
                + customUtilsLazy.getValue().getSavedMemberData().getUserId() + "-" + userId + "-" + tripId);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getRef().getKey();
                System.out.println("Conversation kay :" + key);
//                hasData.set(View.VISIBLE);
//                noData.set(View.GONE);
                if (key.equals("last_message")) {

                    Message message1 = (Message) dataSnapshot.getValue(Message.class);
                    if (message1 != null) {
                        System.out.println("Conversation new message :" + message1.getContent());
                        chatRecyclerViewAdapter.addItem(message1);
                        linearLayoutManager.scrollToPositionWithOffset(chatRecyclerViewAdapter.getLastPosition(), 20);
                    }
//                    System.out.println("Conversation List :" + allMessages.get(allMessages.size() - 1).getContent());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addChildEventListener(childEventListener);

    }
}
