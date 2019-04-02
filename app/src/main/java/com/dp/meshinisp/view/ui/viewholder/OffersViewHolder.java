package com.dp.meshinisp.view.ui.viewholder;

import android.content.Intent;
import android.view.View;

import com.dp.meshinisp.databinding.OfferListItemBinding;
import com.dp.meshinisp.service.model.global.OffersResponseModel;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.view.ui.activity.RequestDetailsActivity;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class OffersViewHolder extends RecyclerView.ViewHolder {
    private final OfferListItemBinding binding;
    private OffersResponseModel requestsResponseModel;
    private OnItemClickListener listener;

    public OffersViewHolder(OfferListItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindClass(OffersResponseModel videosResponseModel, final OnItemClickListener listener) {
        this.requestsResponseModel = videosResponseModel;
        this.listener = listener;
        binding.tvName.setText(requestsResponseModel.getName());
        binding.tvCountry.setText(requestsResponseModel.getCountry());
        binding.tvDate.setText(requestsResponseModel.getDate());
        binding.tvOffer.setText(String.valueOf(requestsResponseModel.getOffer()));
        makeActionToCancelOffer();
        readMoreClickListener();
    }

    private void makeActionToCancelOffer() {
        binding.ivCancelOffer.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(position);
                }
            }
        });
    }

    private void readMoreClickListener() {
        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        });
    }

}
