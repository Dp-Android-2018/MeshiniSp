package com.dp.meshinisp.service.model.global;

import java.io.Serializable;

public class LastMessage implements Serializable {

    private String content;
    private String fromID;
    private String toID;
    private boolean isRead;
    private String type;
    private Long timestamp;

    public LastMessage() {
    }

    public LastMessage(String content, Long timestampNow) {
        this.content = content;
        this.timestamp = timestampNow;
    }

    public String getContent() {
        return content;
    }

    public Long getTimestamp() {
        return timestamp;
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

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
