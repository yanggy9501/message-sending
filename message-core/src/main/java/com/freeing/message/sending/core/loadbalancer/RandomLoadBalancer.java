package com.freeing.message.sending.core.loadbalancer;

import com.freeing.message.sending.core.MessageSend;
import com.freeing.message.sending.core.SendMessage;

import java.security.SecureRandom;
import java.util.List;

public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public <T extends SendMessage> MessageSend<T> getServer(List<MessageSend<T>> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        int idx = new SecureRandom().nextInt(servers.size());
        return servers.get(idx);
    }
}
