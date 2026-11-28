package com.freeing.message.sending.core;

import com.freeing.message.sending.core.aspect.MessageSendAspect;
import com.freeing.message.sending.core.compose.SendMessagePretreatment;
import com.freeing.message.sending.core.loadbalancer.LoadBalancer;
import com.freeing.message.sending.core.manager.MessageSendManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MessageSendService {
    private final MessageSendManager messageSendManager;

    private final List<MessageTemplateRender> messageTemplateRenders;

    private final MessageTemplateLoader<? extends BaseMessageTemplate, ?> messageTemplateLoader;

    private final LoadBalancer loadBalancer;

    private final List<MessageSendAspect> aspectList;

    private final ExecutorService threadPool;

    public MessageSendService(
            MessageSendManager messageSendManager,
            List<MessageTemplateRender> messageTemplateRenders,
            MessageTemplateLoader<? extends BaseMessageTemplate, ?> messageTemplateLoader,
            LoadBalancer loadBalancer,
            List<MessageSendAspect> aspectList,
            ExecutorService threadPool) {
        this.messageSendManager = messageSendManager;
        this.messageTemplateRenders = messageTemplateRenders;
        this.messageTemplateLoader = messageTemplateLoader;
        this.loadBalancer = loadBalancer;
        this.aspectList = aspectList == null ? new ArrayList<>() : aspectList;
        this.threadPool = threadPool;
    }

    public MessageSendManager getMessageSendManager() {
        return messageSendManager;
    }

    public List<MessageTemplateRender> getMessageTemplateRenders() {
        return messageTemplateRenders;
    }

    public MessageTemplateLoader<? extends BaseMessageTemplate, ?> getMessageTemplateLoader() {
        return messageTemplateLoader;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public List<MessageSendAspect> getAspectList() {
        return aspectList;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public SendMessagePretreatment of(String msgName, String msgContent) {
        return new SendMessagePretreatment(this).of(msgName, msgContent);
    }

    public <T> SendMessagePretreatment of(T template) {
        return new SendMessagePretreatment(this).of(template);
    }
}
