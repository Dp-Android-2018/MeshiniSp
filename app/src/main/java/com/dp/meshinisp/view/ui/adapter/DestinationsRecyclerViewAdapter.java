package com.dp.meshinisp.view.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ItemDestinationRvLayoutBinding;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.utility.utils.firebase.classes.ActiveTripFirebase;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;
import com.dp.meshinisp.view.ui.viewholder.DestinationsViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class DestinationsRecyclerViewAdapter extends RecyclerView.Adapter<DestinationsViewHolder> {
    private ArrayList<StartTripResponseModel> data;
    private OnItemClickListener mListener;
    private ActiveTripFirebase activeTripFirebase;

    public DestinationsRecyclerViewAdapter(ArrayList<StartTripResponseModel> startTripResponseModels, ActiveTripFirebase activeTripFirebase) {
        this.data = startTripResponseModels;
        this.activeTripFirebase=activeTripFirebase;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public DestinationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         ItemDestinationRvLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_destination_rv_layout, parent, false);
        return new DestinationsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationsViewHolder holder, int position) {
        holder.bindClass(data.get(position), mListener,activeTripFirebase);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }
}
