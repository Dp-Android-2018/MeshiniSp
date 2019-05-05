package com.dp.meshinisp.service.model.global;

public class ConversationHistory {
    private LastMessage lastMessage;

    private ServiceProviderData conversations;




    public ConversationHistory(LastMessage lastMessage, ServiceProviderData conversations) {
        this.lastMessage = lastMessage;
        this.conversations=conversations;
    }


    public LastMessage getLastMessage() {
        return lastMessage;
    }
    public ServiceProviderData getConversations() {
        return conversations;
    }
}
