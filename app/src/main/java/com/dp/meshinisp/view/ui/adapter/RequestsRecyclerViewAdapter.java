package com.dp.meshinisp.view.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ItemListOfferBinding;
import com.dp.meshinisp.service.model.global.RequestsResponseModel;
import com.dp.meshinisp.view.ui.viewholder.RequestsViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsViewHolder> {
    private ArrayList<RequestsResponseModel> requestsResponseModels;

    public RequestsRecyclerViewAdapter(ArrayList<RequestsResponseModel> requestsResponseModels) {
        this.requestsResponseModels=requestsResponseModels;
    }

    @NonNull
    @Override
    public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListOfferBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_offer,parent,false);
        return new RequestsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsViewHolder holder, int position) {
        holder.bindClass(requestsResponseModels.get(position));
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
