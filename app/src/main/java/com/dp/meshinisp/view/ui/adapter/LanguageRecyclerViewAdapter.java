package com.dp.meshinisp.view.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.LanguageSpinnerListItemBinding;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.view.ui.callback.OnLanguageItemClickListener;
import com.dp.meshinisp.view.ui.viewholder.LanguageViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class LanguageRecyclerViewAdapter extends RecyclerView.Adapter<LanguageViewHolder> {
    private List<CountryCityResponseModel> data;
    private OnLanguageItemClickListener mListener;

    public LanguageRecyclerViewAdapter(List<CountryCityResponseModel> startTripResponseModels) {
        this.data = startTripResponseModels;
    }

    public void setOnItemClickListener(OnLanguageItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LanguageSpinnerListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.language_spinner_list_item, parent, false);
        return new LanguageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.bindClass(data.get(position), mListener,position);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }
}
