package com.freeing.message.sending.core;

import java.util.ArrayList;
import java.util.List;

public class InAppTarget extends Target {
    /**
     * 发布人
     */
    private String publisher;

    /**
     * 目标
     */
    private List<String> receivers;

    public String getPublisher() {
        return publisher;
    }

    public InAppTarget setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public InAppTarget setReceivers(List<String> receivers) {
        this.receivers = receivers;
        return this;
    }

    public InAppTarget addReceiver(String receiver) {
        if (this.receivers == null) {
            this.receivers = new ArrayList<>();
        }
        this.receivers.add(receiver);
        return this;
    }
}
