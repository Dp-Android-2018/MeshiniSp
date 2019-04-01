package com.dp.meshinisp.view.ui.viewholder;

import android.content.Intent;
import android.widget.ImageView;

import com.dp.meshinisp.databinding.TripListItemBinding;
import com.dp.meshinisp.service.model.global.TripsResponseModel;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.view.ui.activity.RequestDetailsActivity;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;


public class TripsViewHolder extends RecyclerView.ViewHolder {
    private final TripListItemBinding binding;
    private TripsResponseModel requestsResponseModel;
    private String tripsType;

    public TripsViewHolder(TripListItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindClass(TripsResponseModel videosResponseModel, String tripsType) {
        this.requestsResponseModel = videosResponseModel;
        this.tripsType=tripsType;
        binding.tvCountry.setText(requestsResponseModel.getCountry());
        binding.tvTripDate.setText(requestsResponseModel.getDate());
        binding.tvGuideName.setText(requestsResponseModel.getServiceProvider());
        binding.tvCashAmount.setText(requestsResponseModel.getOfferPrice());
        ImageView ivFeedPhoto = binding.ivOffer;
        Picasso.get().load(requestsResponseModel.getImageUrl()).into(ivFeedPhoto);
        readMoreClickListener();
    }

    private void readMoreClickListener() {
        binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RequestDetailsActivity.class);
            intent.putExtra(ConfigurationFile.Constants.REQUEST_Type, tripsType);
            intent.putExtra(ConfigurationFile.Constants.REQUEST_ID, requestsResponseModel.getId());
            intent.putExtra(ConfigurationFile.Constants.OFFER_PRICE, requestsResponseModel.getOfferPrice());
            v.getContext().startActivity(intent);
        });
    }

}
