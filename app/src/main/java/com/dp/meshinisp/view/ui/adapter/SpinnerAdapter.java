package com.dp.meshinisp.view.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerAdapter extends ArrayAdapter<CountryCityResponseModel> {


    public SpinnerAdapter(Context context, List<CountryCityResponseModel> countryCityPojos){
        super(context,0,countryCityPojos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_list_item, parent, false);
        }
        TextView textViewName = convertView.findViewById(R.id.tv_country_name);

        CountryCityResponseModel currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
        }

        return convertView;
    }

}
