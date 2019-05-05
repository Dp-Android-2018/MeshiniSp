package com.dp.meshinisp.view.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ItemListChatBinding;
import com.dp.meshinisp.service.model.global.Message;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;
import com.dp.meshinisp.view.ui.viewholder.ChatViewHolder;

import java.util.ArrayList;
import java.util.List;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<Message> data = new ArrayList<>();
    private OnItemClickListener mListener;
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListChatBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_chat, parent, false);
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bindClass(data.get(position), mListener, serviceProviderId, userId);
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
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
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
