package com.freeing.message.sending.core;

import java.util.HashMap;
import java.util.Map;

public class PushTarget extends Target {
    private String imageUrl;
    
    private String clickAction;
    
    private Map<String, String> data = new HashMap<>();

    public String getImageUrl() {
        return imageUrl;
    }

    public PushTarget setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getClickAction() {
        return clickAction;
    }

    public PushTarget setClickAction(String clickAction) {
        this.clickAction = clickAction;
        return this;
    }

    public Map<String, String> getData() {
        return data;
    }

    public PushTarget setData(Map<String, String> data) {
        this.data = data;
        return this;
    }
}
