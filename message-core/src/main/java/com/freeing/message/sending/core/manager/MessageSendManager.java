package com.freeing.message.sending.core.manager;

import com.freeing.message.sending.core.MessageSend;
import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.exception.SendMessageException;
import com.freeing.message.sending.core.SendMessage;
import com.freeing.message.sending.core.util.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageSendManager {
    private final Map<String /* platform */, MessageSend<?>> platformMessageSendMap = new HashMap<>();
    private final Map<SendChannel, Map<String /* group */, List<MessageSend<?>>>> groupMessageSendMap = new HashMap<>();

    public synchronized void register(MessageSend<?> messageSend) {
        SendChannel sendChannel = messageSend.getSendChannel();
        String group = messageSend.getGroup();
        String platform = messageSend.getPlatform();

        if (platformMessageSendMap.containsKey(platform)) {
            throw new SendMessageException("The platform exist already: " + platform);
        }
        if (StrUtil.isNotEmpty(platform)) {
            platformMessageSendMap.put(platform, messageSend);
        }

        groupMessageSendMap.compute(sendChannel, (k, v) -> {
            if (v == null) {
                v = new HashMap<>();
            }
            if (!v.containsKey(group)) {
                v.put(group, new ArrayList<>());
            }
            v.get(group).add(messageSend);
            return v;
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends SendMessage> MessageSend<T> getByPlatform(String platform) {
        if (platformMessageSendMap.containsKey(platform)) {
            return (MessageSend<T>) platformMessageSendMap.get(platform);
        }
        throw new SendMessageException("The platform no exist: " + platform);
    }

    @SuppressWarnings("unchecked")
    public <T extends SendMessage> List<MessageSend<T>> getByGroup(SendChannel channel, String group) {
        if (groupMessageSendMap.containsKey(channel)) {
            if (groupMessageSendMap.get(channel).containsKey(group)) {
                List<?> rawList = groupMessageSendMap.get(channel).get(group);
                return rawList.stream()
                        .map(item -> (MessageSend<T>) item)
                        .collect(Collectors.toList());
            }
        }
        throw new SendMessageException("The group no exist: " + group);
    }

    public <T extends SendMessage> List<MessageSend<T>> getByChannel(SendChannel channel) {
        return getByGroup(channel, null);
    }
}
