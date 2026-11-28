package com.freeing.message.sending.core;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class PushMessage extends SendMessage {
    @Serial private static final long serialVersionUID = 1L;

    private String imageUrl;

    private String clickAction;

    private Map<String, String> data = new HashMap<>();

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
