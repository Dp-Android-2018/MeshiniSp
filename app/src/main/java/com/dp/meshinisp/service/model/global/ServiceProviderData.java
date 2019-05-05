package com.dp.meshinisp.service.model.global;

import java.io.Serializable;

public class ServiceProviderData implements Serializable {
    private String ChatPath;
    private String sender_name;
    private String device_token;

    public ServiceProviderData() {
    }

    public ServiceProviderData(String ChatPath, String sender_name, String device_token) {

        this.ChatPath = ChatPath;
        this.sender_name = sender_name;
        this.device_token = device_token;
    }

    public String getChatPath() {
        return ChatPath;
    }

    public void setChatPath(String chatPath) {
        ChatPath = chatPath;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_token() {
        return device_token;
    }
}
