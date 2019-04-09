package com.dp.meshinisp.view.ui.viewholder;

import com.dp.meshinisp.databinding.ItemListDestinationBinding;
import com.dp.meshinisp.databinding.OfferListItemBinding;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;

import androidx.recyclerview.widget.RecyclerView;


public class DestinationsViewHolder extends RecyclerView.ViewHolder {
    private final ItemListDestinationBinding binding;
    private StartTripResponseModel startTripResponseModel;
    private OnItemClickListener listener;

    public DestinationsViewHolder(ItemListDestinationBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindClass(StartTripResponseModel startTripResponseModel, final OnItemClickListener listener) {
        this.startTripResponseModel = startTripResponseModel;
        this.listener = listener;
        binding.tvDestination.setText(startTripResponseModel.getName());
        readMoreClickListener();
    }

   /* private void makeActionToCancelOffer() {
        binding.ivCancelOffer.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(position);
                }
            }
        });
    }*/

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
