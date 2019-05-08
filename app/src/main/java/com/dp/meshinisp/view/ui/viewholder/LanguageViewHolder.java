package com.dp.meshinisp.view.ui.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.databinding.LanguageSpinnerListItemBinding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.view.ui.callback.OnLanguageItemClickListener;

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
        readMoreClickListener();
    }

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

        binding.languageCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                listener.onCheckboxChecked(position, binding);
            } else if (!isChecked) {
                listener.onCheckboxUnChecked(position, binding);
            }
        });
    }

}
