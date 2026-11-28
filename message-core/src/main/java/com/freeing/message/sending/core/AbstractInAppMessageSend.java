package com.freeing.message.sending.core;

import com.freeing.message.sending.core.constants.SendChannel;

/**
 * @param <T> 透传数据范型
 */
public abstract class AbstractInAppMessageSend<T> implements MessageSend<InAppMessage<T>> {

    public AbstractInAppMessageSend() {}

    @Override
    public SendChannel getSendChannel() {
        return SendChannel.IN_APP;
    }

    @Override
    public String getPlatform() {
        return null;
    }

    @Override
    public String getGroup() {
        return null;
    }
}
