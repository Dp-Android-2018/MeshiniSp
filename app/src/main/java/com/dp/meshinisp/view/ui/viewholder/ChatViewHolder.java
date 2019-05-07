package com.dp.meshinisp.view.ui.viewholder;

import android.text.format.DateFormat;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dp.meshinisp.R;
import com.dp.meshinisp.databinding.ItemListChatBinding;
import com.dp.meshinisp.databinding.ItemListChatServisProviderBinding;
import com.dp.meshinisp.databinding.ItemListChatUserBinding;
import com.dp.meshinisp.service.model.global.Message;
import com.dp.meshinisp.utility.utils.ConfigurationFile;
import com.dp.meshinisp.utility.utils.CustomUtils;
import com.dp.meshinisp.view.ui.callback.OnItemClickListener;

import java.util.Calendar;
import java.util.Locale;

import kotlin.Lazy;

import static org.koin.java.standalone.KoinJavaComponent.inject;


public class ChatViewHolder extends RecyclerView.ViewHolder {
    private ItemListChatServisProviderBinding servisProviderBinding;
    private ItemListChatUserBinding userBinding;
    Lazy<CustomUtils> customUtilsLazy = inject(CustomUtils.class);
    private Message message;
    private int serviceProviderId;
    private int userId;
    private Calendar cal;

    public ChatViewHolder(ItemListChatServisProviderBinding binding) {
        super(binding.getRoot());
        this.servisProviderBinding = binding;
    }

    public ChatViewHolder(ItemListChatUserBinding binding) {
        super(binding.getRoot());
        this.userBinding = binding;
    }

    public void bindUser(Message message, int serviceProviderId, int userId) {
        this.message = message;
        this.serviceProviderId = serviceProviderId;
        this.userId = userId;
        if (customUtilsLazy.getValue().getSavedLanguageType().equals(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC)) {
            Locale locale = new Locale("ar", "EG");
            cal = Calendar.getInstance(locale);
            System.out.println("language : arabic");
            cal.setTimeInMillis(message.getTimestamp() * 1000L);
//        "dd-MM-yyyy hh:mm:ss"
            String date = DateFormat.format("EEE, MMM d, hh:mm:ss", cal).toString();
            userBinding.tvMessageTime.setText(date);
        } else {
            cal = Calendar.getInstance(Locale.ENGLISH);
            System.out.println("language : english");
            cal.setTimeInMillis(message.getTimestamp() * 1000L);
//        "dd-MM-yyyy hh:mm:ss"
            String date = DateFormat.format("EEE, MMM d, hh:mm:ss", cal).toString();
            userBinding.tvMessageTime.setText(date);
        }
        userBinding.tvMessageContent.setText(message.getContent());
    }

    public void bindServiceProvider(Message message, int serviceProviderId, int userId) {
        this.message = message;
        this.serviceProviderId = serviceProviderId;
        this.userId = userId;
        if (customUtilsLazy.getValue().getSavedLanguageType().equals(ConfigurationFile.Constants.ACCEPT_LANGUAGE_ARABIC)) {
            Locale locale = new Locale("ar", "EG");
            cal = Calendar.getInstance(locale);
            System.out.println("language : arabic");
            cal.setTimeInMillis(message.getTimestamp() * 1000L);
//        "dd-MM-yyyy hh:mm:ss"
            String date = DateFormat.format("EEE, MMM d, hh:mm:ss", cal).toString();
            servisProviderBinding.tvMessageTime.setText(date);
        } else {
            cal = Calendar.getInstance(Locale.ENGLISH);
            System.out.println("language : english");
            cal.setTimeInMillis(message.getTimestamp() * 1000L);
//        "dd-MM-yyyy hh:mm:ss"
            String date = DateFormat.format("EEE, MMM d, hh:mm:ss", cal).toString();
            servisProviderBinding.tvMessageTime.setText(date);
        }
        servisProviderBinding.tvMessageContent.setText(message.getContent());

    }
//    item_list_chat_servis_provider

}
