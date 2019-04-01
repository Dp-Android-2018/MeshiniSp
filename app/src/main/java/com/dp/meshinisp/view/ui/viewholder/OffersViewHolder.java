package com.dp.meshinisp.view.ui.viewholder;

import android.content.Intent;
import android.widget.ImageView;

import com.dp.meshinisp.databinding.ItemListOfferBinding;
import com.dp.meshinisp.service.model.global.OffersResponseModel;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.view.ui.activity.RequestDetailsActivity;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;


public class OffersViewHolder extends RecyclerView.ViewHolder {
    private final ItemListOfferBinding binding;
    private OffersResponseModel requestsResponseModel;


    public OffersViewHolder(ItemListOfferBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindClass(OffersResponseModel videosResponseModel) {
        this.requestsResponseModel = videosResponseModel;
        binding.tvName.setText(requestsResponseModel.getName());
        binding.tvRating.setText(String.valueOf(requestsResponseModel.getRating()));
        binding.tvNumTrips.setText(String.valueOf(requestsResponseModel.getTripsCount()));
        ImageView ivFeedPhoto = binding.roundedImageView;
        Picasso.get().load(requestsResponseModel.getProfilePictureUrl()).into(ivFeedPhoto);
//        readMoreClickListener();
    }

    private void readMoreClickListener() {
        binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RequestDetailsActivity.class);
            intent.putExtra(ConfigurationFile.Constants.REQUEST_ID, requestsResponseModel.getId());
            v.getContext().startActivity(intent);
        });
    }

}
