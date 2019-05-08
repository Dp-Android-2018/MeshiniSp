package com.dp.meshinisp.view.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ItemListChatServiceProviderBinding;
import com.dp.meshinisp.databinding.ItemListChatUserBinding;
import com.dp.meshinisp.service.model.global.Message;
import com.dp.meshinisp.view.ui.viewholder.ChatViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.VIEW_TYPE_MESSAGE_RECEIVED;
import static com.dp.meshinisp.utility.utils.ConfigurationFile.Constants.VIEW_TYPE_MESSAGE_SENT;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<Message> data = new ArrayList<>();
    private int serviceProviderId;
    private int userId;

    public ChatRecyclerViewAdapter(int serviceProviderId, int userId) {
        this.serviceProviderId = serviceProviderId;
        this.userId = userId;
    }

    public void setData(List<Message> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getFromID().equals("serviceProvider-" + serviceProviderId)) {
            return VIEW_TYPE_MESSAGE_SENT;

        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            ItemListChatServiceProviderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_chat_service_provider, parent, false);
            return new ChatViewHolder(binding);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            ItemListChatUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_chat_user, parent, false);
            return new ChatViewHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        if (data.get(position).getFromID().equals("serviceProvider-" + serviceProviderId)) {
            holder.bindServiceProvider(data.get(position), serviceProviderId, userId);
        } else {
            holder.bindUser(data.get(position), serviceProviderId, userId);
        }

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void addItem(Message message) {
        data.add(message);
        notifyDataSetChanged();
    }

    public int getLastPosition() {
        if (data != null) {
            return data.size() - 1;
        } else {
            return 0;
        }
    }
}
