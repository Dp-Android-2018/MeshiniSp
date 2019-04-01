package com.dp.meshinisp.view.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.TripListItemBinding;
import com.dp.meshinisp.service.model.global.TripsResponseModel;
import com.dp.meshinisp.view.ui.viewholder.TripsViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class TripsRecyclerViewAdapter extends RecyclerView.Adapter<TripsViewHolder> {
    private ArrayList<TripsResponseModel> requestsResponseModels;
    private String tripsType;

    public TripsRecyclerViewAdapter(ArrayList<TripsResponseModel> requestsResponseModels, String tripsType) {
        this.requestsResponseModels=requestsResponseModels;
        this.tripsType=tripsType;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TripListItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.trip_list_item,parent,false);
        return new TripsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {
        holder.bindClass(requestsResponseModels.get(position),tripsType);
    }

    @Override
    public int getItemCount() {
        if (requestsResponseModels !=null){
            return requestsResponseModels.size();
        }else {
            return 0;
        }
    }
}
