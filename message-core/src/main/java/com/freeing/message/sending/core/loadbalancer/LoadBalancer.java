package com.freeing.message.sending.core.loadbalancer;

import com.freeing.message.sending.core.MessageSend;
import com.freeing.message.sending.core.SendMessage;

import java.util.List;

public interface LoadBalancer {
    <T extends SendMessage> MessageSend<T> getServer(List<MessageSend<T>> servers);
}
