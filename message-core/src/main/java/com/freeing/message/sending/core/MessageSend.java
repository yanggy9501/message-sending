package com.freeing.message.sending.core;

import com.freeing.message.sending.core.constants.SendChannel;

public interface MessageSend<T extends SendMessage> extends AutoCloseable {
    SendChannel getSendChannel();

    /**
     * 获取平台
     */
    String getPlatform();

    /**
     * 租户标识
     */
    String getGroup();

    /**
     * 发送消息
     *
     * @param message 消息
     */
    void send(T message);

    /**
     * 释放相关资源
     */
    default void close() {}
}
