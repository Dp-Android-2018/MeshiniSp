package com.dp.meshinisp.view.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.service.model.global.CountryCityResponseModel;
import com.dp.meshinisp.view.ui.callback.OnLanguageItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LanguageSpinnerAdapter extends ArrayAdapter<CountryCityResponseModel> {
    private OnLanguageItemClickListener mListener;
    private TextView textViewName;
    private CheckBox languageCheckBox;

    public LanguageSpinnerAdapter(Context context, List<CountryCityResponseModel> countryCityPojos) {
        super(context, 0, countryCityPojos);
    }

    public void setOnItemClickListener(OnLanguageItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent, mListener);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent, mListener);
    }

    private View initView(int position, View convertView, ViewGroup parent, final OnLanguageItemClickListener listener) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.language_spinner_list_item, parent, false);
        }
        textViewName = convertView.findViewById(R.id.tv_country_name);
        languageCheckBox = convertView.findViewById(R.id.language_checkbox);

        CountryCityResponseModel currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
        }

        return convertView;
    }
}
