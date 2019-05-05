package com.dp.meshinisp.service.model.global;

public class Message {
    private String content;
    private String fromID;
    private String toID;
    private boolean isRead;
    private String type;
    private Long timestamp;

    public Message(){};
    public Message(String content, String fromID, String toID, boolean isRead, String type, Long timestamp ) {
        this.content = content;
        this.fromID = fromID;
        this.toID = toID;
        this.isRead = isRead;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
