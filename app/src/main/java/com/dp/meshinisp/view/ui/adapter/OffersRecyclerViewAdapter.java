package com.dp.meshinisp.view.ui.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.OfferListItemBinding;
import com.dp.meshinisp.service.model.global.OffersResponseModel;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;
import com.dp.meshinisp.view.ui.viewholder.OffersViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class OffersRecyclerViewAdapter extends RecyclerView.Adapter<OffersViewHolder> {
    private ArrayList<OffersResponseModel> requestsResponseModels;
    private OnItemClickListener mListener;

    public OffersRecyclerViewAdapter(ArrayList<OffersResponseModel> requestsResponseModels) {
        this.requestsResponseModels = requestsResponseModels;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.offer_list_item, parent, false);
        return new OffersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {
        holder.bindClass(requestsResponseModels.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        if (requestsResponseModels != null) {
            return requestsResponseModels.size();
        } else {
            return 0;
        }
    }

    public void removeItem(int position) {
        requestsResponseModels.remove(position);
        notifyItemRemoved(position);
    }
}
