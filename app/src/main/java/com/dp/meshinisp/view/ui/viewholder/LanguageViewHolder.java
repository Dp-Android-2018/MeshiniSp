package com.dp.meshinisp.view.ui.viewholder;

import android.view.View;
import android.widget.CompoundButton;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ItemDestinationRvLayoutBinding;
import com.dp.meshinisp.databinding.LanguageSpinnerListItemBinding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.service.model.global.StartTripResponseModel;
import com.dp.meshinisp.utility.utils.firebase.classes.ActiveTripFirebase;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;
import com.dp.meshinisp.view.ui.callback.OnLanguageItemClickListener;

import androidx.recyclerview.widget.RecyclerView;

public class LanguageViewHolder extends RecyclerView.ViewHolder {
    private final LanguageSpinnerListItemBinding binding;
    private CountryCityResponseModel startTripResponseModel;
    private OnLanguageItemClickListener listener;
    private int position;

    public LanguageViewHolder(LanguageSpinnerListItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindClass(CountryCityResponseModel startTripResponseModel, final OnLanguageItemClickListener listener, int position) {
        this.startTripResponseModel = startTripResponseModel;
        this.listener = listener;
        this.position = position;
        binding.tvCountryName.setText(startTripResponseModel.getName());
//        initializeDestinations();
        readMoreClickListener();
    }

   /* private void initializeDestinations() {
        if (activeTripFirebase != null) {
            if (startTripResponseModel.getId() == activeTripFirebase.getNext_destination()) {
                binding.btStartDestination.setVisibility(View.GONE);
                binding.btDoneDestination.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < activeTripFirebase.getPlaces().size(); i++) {
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
    }*/

    private void readMoreClickListener() {


        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                if (binding.languageCheckbox.isChecked()) {
                    binding.languageCheckbox.setChecked(false);
                    listener.onCheckboxUnChecked(position, binding);

                } else {
                    binding.languageCheckbox.setChecked(true);
                    listener.onCheckboxChecked(position, binding);
                }
            }
        });

        binding.languageCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listener.onCheckboxChecked(position, binding);
                } else if (!isChecked) {
                    listener.onCheckboxUnChecked(position, binding);
                }
            }
        });
    }

}
