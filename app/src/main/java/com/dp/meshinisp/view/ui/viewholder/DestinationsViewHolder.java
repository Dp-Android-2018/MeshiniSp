package com.dp.meshinisp.view.ui.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ItemDestinationRvLayoutBinding;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.utility.utils.firebase.classes.ActiveTripFirebase;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;


public class DestinationsViewHolder extends RecyclerView.ViewHolder {
    private final ItemDestinationRvLayoutBinding binding;
    private StartTripResponseModel startTripResponseModel;
    private OnItemClickListener listener;

    public DestinationsViewHolder(ItemDestinationRvLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindClass(StartTripResponseModel startTripResponseModel, final OnItemClickListener listener, ActiveTripFirebase activeTripFirebase) {
        this.startTripResponseModel = startTripResponseModel;
        this.listener = listener;
        binding.tvDestination.setText(startTripResponseModel.getName());
        initializeDestinations(activeTripFirebase);
        readMoreClickListener();
    }

    private void initializeDestinations(ActiveTripFirebase activeTripFirebase) {
        if (activeTripFirebase != null) {
            if (startTripResponseModel.getId() == activeTripFirebase.getNext_destination()) {
                binding.btStartDestination.setVisibility(View.GONE);
                binding.btDoneDestination.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i <activeTripFirebase.getPlaces().size(); i++) {
                if (activeTripFirebase.getPlaces().get(i).getId() == startTripResponseModel.getId()) {
                    if (activeTripFirebase.getPlaces().get(i).isDone()) {
                        binding.getRoot().setBackgroundColor(binding.getRoot().getResources().getColor(R.color.slide_to_finish_color));
                        binding.ivDone.setVisibility(View.VISIBLE);
                        binding.btStartDestination.setVisibility(View.GONE);
                        binding.btDoneDestination.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void readMoreClickListener() {
        binding.btStartDestination.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onStartClick(position, binding);
                }
            }
        });

        binding.btDoneDestination.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEndClick(position, binding);
                }
            }
        });
    }

}
