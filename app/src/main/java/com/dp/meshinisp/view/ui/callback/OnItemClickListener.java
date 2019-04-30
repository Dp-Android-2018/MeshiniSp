package com.dp.meshinisp.view.ui.callback;

import com.dp.meshinisp.databinding.ItemDestinationRvLayoutBinding;

public interface OnItemClickListener {
    void onItemClick(int position);

    void onDeleteClick(int position);

    void onStartClick(int position, ItemDestinationRvLayoutBinding binding);

    void onEndClick(int position, ItemDestinationRvLayoutBinding binding);


}
