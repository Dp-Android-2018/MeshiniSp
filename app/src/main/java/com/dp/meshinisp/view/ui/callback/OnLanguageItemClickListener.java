package com.dp.meshinisp.view.ui.callback;

import com.dp.meshinisp.databinding.LanguageSpinnerListItemBinding;

public interface OnLanguageItemClickListener {
    void onCheckboxChecked(int position, LanguageSpinnerListItemBinding convertView);
    void onCheckboxUnChecked(int position, LanguageSpinnerListItemBinding convertView);
}
