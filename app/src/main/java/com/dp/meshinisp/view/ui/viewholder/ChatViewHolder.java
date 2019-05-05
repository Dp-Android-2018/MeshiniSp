package com.dp.meshinisp.view.ui.viewholder;

import android.text.format.DateFormat;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ItemListChatBinding;
import com.dp.meshinisp.service.model.global.Message;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;

import java.util.Calendar;
import java.util.Locale;

import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class ChatViewHolder extends RecyclerView.ViewHolder {
    private final ItemListChatBinding binding;
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private Message message;
    private OnItemClickListener listener;
    private int serviceProviderId;
    private int userId;
    private Calendar cal;

    public ChatViewHolder(ItemListChatBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindClass(Message message, final OnItemClickListener listener, int serviceProviderId, int userId) {
        this.message = message;
        this.listener = listener;
        this.serviceProviderId = serviceProviderId;
        this.userId = userId;
        if (customUtilsLazy.getValue().getSavedLanguageType().equals(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC)) {
            Locale locale = new Locale("ar", "EG");
            cal = Calendar.getInstance(locale);
            System.out.println("language : arabic");
            cal.setTimeInMillis(message.getTimestamp() * 1000L);
//        "dd-MM-yyyy hh:mm:ss"
            String date = DateFormat.format("EEE, MMM d, hh:mm:ss", cal).toString();
            binding.tvMessageTime.setText(date);
        } else {
            cal = Calendar.getInstance(Locale.ENGLISH);
            System.out.println("language : english");
            cal.setTimeInMillis(message.getTimestamp() * 1000L);
//        "dd-MM-yyyy hh:mm:ss"
            String date = DateFormat.format("EEE, MMM d, hh:mm:ss", cal).toString();
            binding.tvMessageTime.setText(date);
        }
        binding.tvMessageContent.setText(message.getContent());

        if (!message.getFromID().equals("user-" + userId)) {
            binding.baseView.setBackground(binding.getRoot().getResources().getDrawable(R.drawable.chat_item_background_black));
            binding.tvMessageContent.setTextColor(binding.getRoot().getResources().getColor(R.color.white_90));
            binding.tvMessageTime.setTextColor(binding.getRoot().getResources().getColor(R.color.white_90));
        } else {
            binding.baseView.setBackground(binding.getRoot().getResources().getDrawable(R.drawable.chat_item_background_white));
            binding.tvMessageContent.setTextColor(binding.getRoot().getResources().getColor(R.color.text_color_black));
            binding.tvMessageTime.setTextColor(binding.getRoot().getResources().getColor(R.color.text_color_black));
        }
//        binding.tvName.setText(this.message.getName());
//        binding.tvCountry.setText(this.message.getCountry());
//        binding.tvDate.setText(this.message.getDate());
//        binding.tvOffer.setText(String.valueOf(this.message.getOffer()));
//        makeActionToCancelOffer();
//        readMoreClickListener();
    }

   /* private void makeActionToCancelOffer() {
        binding.ivCancelOffer.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(position);
                }
            }
        });
    }

    private void readMoreClickListener() {
        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        });
    }*/

}
