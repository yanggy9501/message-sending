package com.freeing.message.sending.core.compose;

import com.freeing.message.sending.core.MessageSendService;
import com.freeing.message.sending.core.Target;
import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.exception.SendMessageException;

import java.util.*;

public class SendMessagePretreatment {

    final MessageSendService sendService;

    /**
     * channel 分类 指定channel则使用（channel，group=null，platform=null）的MessageSend发消息 指定channel +
     * group则使用（channel，group，platform=null）的MessageSend发消息 指定platform
     * 则使用（channel=null，group=null，platform）的MessageSend发消息
     */
    private final Map<SendChannel, Map.Entry<String /* group */, String /* platform */>> channels =
            new HashMap<>();

    private boolean msgSetFlag = false;

    private Object template;

    private String msgName;

    private String msgContent;

    private Map<String, Object> variables;

    private Map<String, Object> attr;

    private List<Target> targets;

    public SendMessagePretreatment(MessageSendService sendService) {
        this.sendService = sendService;
    }

    protected MessageSendService getSendService() {
        return sendService;
    }

    public Map<SendChannel, Map.Entry<String /* group */, String /* platform */>> getChannels() {
        return channels;
    }

    public boolean isMsgSetFlag() {
        return msgSetFlag;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTemplate() {
        return (T) template;
    }

    public String getMsgName() {
        return msgName;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public SendMessagePretreatment channel(SendChannel... channels) {
        if (channels == null || channels.length == 0) {
            throw new SendMessageException("SendChannel is null or empty");
        }
        for (SendChannel channel : channels) {
            this.channels.put(channel, new AbstractMap.SimpleEntry<>(null, null));
        }
        return this;
    }

    public SendMessagePretreatment channel(SendChannel channel, String group) {
        this.channels.compute(
                channel,
                (g, p) -> {
                    if (p == null) {
                        return new AbstractMap.SimpleEntry<>(group, null);
                    }
                    return p;
                });
        return this;
    }

    public SendMessagePretreatment platform(String... platforms) {
        if (platforms == null || platforms.length == 0) {
            throw new SendMessageException("Platform is null or empty");
        }
        for (String platform : platforms) {
            SendChannel channel =
                    this.sendService
                            .getMessageSendManager()
                            .getByPlatform(platform)
                            .getSendChannel();
            this.channels.compute(
                    channel,
                    (g, p) -> {
                        if (p == null) {
                            return new AbstractMap.SimpleEntry<>(null, platform);
                        }
                        return p;
                    });
        }
        return this;
    }

    public SendMessagePretreatment of(String msgName, String msgContent) {
        if (msgSetFlag) {
            throw new SendMessageException("Message already set");
        }
        this.msgSetFlag = true;
        this.msgName = msgName;
        this.msgContent = msgContent;
        return this;
    }

    public <T> SendMessagePretreatment of(T template) {
        if (msgSetFlag) {
            throw new SendMessageException("Message already set");
        }
        this.msgSetFlag = true;
        this.template = template;
        return this;
    }

    public SendMessagePretreatment variables(Map<String, Object> variables) {
        this.variables = variables;
        return this;
    }

    public SendMessagePretreatment putAttr(String key, Object value) {
        if (attr == null) {
            attr = new HashMap<>();
        }
        attr.put(key, value);
        return this;
    }

    public SendMessagePretreatment addTarget(Target target) {
        if (targets == null) {
            targets = new ArrayList<>();
        }
        targets.add(target);
        return this;
    }

    public void syncSend() {
        new SendMessageActuator(this).execute(false);
    }

    public void asyncSend() {
        new SendMessageActuator(this).execute(true);
    }
}
