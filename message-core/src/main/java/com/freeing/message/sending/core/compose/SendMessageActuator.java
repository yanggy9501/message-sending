package com.freeing.message.sending.core.compose;

import com.freeing.message.sending.core.*;
import com.freeing.message.sending.core.aspect.MessageSeedAspectChain;
import com.freeing.message.sending.core.constants.ContentType;
import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.exception.SendMessageException;
import com.freeing.message.sending.core.template.MessageTemplateLoader;
import com.freeing.message.sending.core.template.MessageTemplateRender;
import com.freeing.message.sending.core.util.StrUtil;
import org.springframework.core.ResolvableType;

import java.util.*;

public class SendMessageActuator {
    private final SendMessagePretreatment pretreatment;
    private final SendService sendService;

    public SendMessageActuator(SendMessagePretreatment pretreatment) {
        this.pretreatment = pretreatment;
        this.sendService = pretreatment.getSendService();
    }

    public void execute(boolean isAsync) {
        final Date startDate = new Date();
        Map<SendChannel, Map.Entry<String, String>> channels = this.pretreatment.getChannels();
        for (Map.Entry<SendChannel, Map.Entry<String /*group*/, String /*platform*/>> channelEntry : channels.entrySet()) {
            if (isAsync) {
                this.sendService.getThreadPool().execute(() -> executeChannel(channelEntry, startDate));
            } else {
                executeChannel(channelEntry, startDate);
            }
        }
    }

    private void executeChannel(Map.Entry<SendChannel, Map.Entry<String, String>> entry, Date startDate) {
        final SendChannel channel = entry.getKey();
        if (channel == null) {
            throw new NullPointerException("SendChannel is null");
        }
        String group = null;
        String platform = null;
        Map.Entry<String, String> value = entry.getValue();
        if (value != null) {
            group = value.getKey();
            platform = value.getValue();
        }

        // Get MessageSend of channel
        List<MessageSend<SendMessage>> messageSends;
        if (StrUtil.isNotEmpty(platform)) {
            MessageSend<SendMessage> messageSend = this.sendService.getMessageSendManager().getByPlatform(platform);
            messageSends = Collections.singletonList(messageSend);
        } else {
            messageSends = this.sendService.getMessageSendManager().getByGroup(channel, group);
        }

        // Build SendMessage for send
        SendMessage sendMessage = buildSendMessage(channel);
        // Set message base part
        sendMessage.setStartTime(startDate);
        sendMessage.setSendChannel(channel);
        sendMessage.setAttr(this.pretreatment.getAttr());

        BaseMessageTemplate messageTemplate = null;
        if (Objects.isNull(this.pretreatment.getTemplate())) {
            sendMessage.setMsgName(this.pretreatment.getMsgName());
            sendMessage.setMsgContent(this.pretreatment.getMsgContent());
        } else {
            // when given templateCode
            messageTemplate = renderTemplate(channel, sendMessage);
        }
        // aspect + doSend
        doSendWithAspect(channel, messageSends, sendMessage, messageTemplate);
    }

    private void doSendWithAspect(SendChannel channel,
                                  List<MessageSend<SendMessage>> messageSends,
                                  SendMessage sendMessage,
                                  BaseMessageTemplate messageTemplate) {
        new MessageSeedAspectChain(this.sendService.getAspectList().iterator(),
                (_sendMessage, _messageTemplate, _pretreatment) -> {
                    MessageSend<SendMessage> finalMessageSend = null;
                    // IN-App has generic type, having to spec doing.
                    if (channel == SendChannel.IN_APP) {
                        // 查找类型匹配的站内信 MessageSend
                        boolean findFlag = false;
                        Object data = null;
                        for (MessageSend<SendMessage> messageSend : messageSends) {
                            data = ((InAppSendMessage<?>) _sendMessage).getData();
                            ResolvableType inputResolvableType = data != null ?
                                    ResolvableType.forInstance(data) :
                                    ResolvableType.forClass(Void.class);
                            ResolvableType specMessageSendResolvableType =
                                    ResolvableType.forClass(messageSend.getClass()).as(AbstractInAppMessageSend.class);
                            ResolvableType genericResolvableType = specMessageSendResolvableType.getGeneric(0);
                            if (genericResolvableType.isAssignableFrom(inputResolvableType)) {
                                finalMessageSend = messageSend;
                                findFlag = true;
                                break;
                            }
                        }
                        if (!findFlag) {
                            String errorMsg = "No such AbstractInAppMessageSend subclass with generic type=" +
                                    (data == null ? Void.TYPE.getName() : data.getClass().toString());
                            throw new SendMessageException(errorMsg);
                        }
                    } else {
                        finalMessageSend = this.sendService.getLoadBalancer().getServer(messageSends);
                    }
                    if (finalMessageSend == null) {
                        throw new SendMessageException("No found MessageSend");
                    }
                    _sendMessage.setPlatform(finalMessageSend.getPlatform());
                    _sendMessage.setGroup(finalMessageSend.getGroup());
                    // seed message
                    finalMessageSend.send(_sendMessage);
                    _sendMessage.setEndTime(new Date());
                    return _sendMessage;
                }
        ).next(sendMessage, messageTemplate, this.pretreatment);
    }

    private SendMessage buildSendMessage(SendChannel channel) {
        if (channel == SendChannel.MAIL) {
            return buildMailSendMessage();
        }
        else if (channel == SendChannel.IN_APP) {
            InAppSendMessage<Object> sendMessage = new InAppSendMessage<>();
            sendMessage.setData(pretreatment.getData(channel));
            List<Target> targets = pretreatment.getTargets();
            for (Target target : targets) {
                if (target instanceof InAppTarget inAppTarget) {
                    sendMessage.setSendFrom(inAppTarget.getPublisher());
                    sendMessage.setSendTo(inAppTarget.getReceivers());
                    break;
                }
            }
            return sendMessage;
        }
        throw new SendMessageException("unknown channel: " + pretreatment.getChannels());
    }

    private MailSendMessage buildMailSendMessage() {
        MailSendMessage sendMessage = new MailSendMessage();
        sendMessage.setData(this.pretreatment.getData(SendChannel.MAIL));
        // 发送者+接收者+资源
        List<Target> targets = this.pretreatment.getTargets();
        for (Target target : targets) {
            if (target instanceof MailTarget mailTarget) {
                sendMessage.setSendFrom(mailTarget.getFrom());
                sendMessage.setSendTo(mailTarget.getTo());
                sendMessage.setCc(mailTarget.getCc());
                sendMessage.setBcc(mailTarget.getBcc());
                sendMessage.setAttachments(mailTarget.getAttachments());
                sendMessage.setInlineSources(mailTarget.getInlineSources());
                break;
            }
        }
        return sendMessage;
    }

    private BaseMessageTemplate renderTemplate(SendChannel channel, SendMessage sendMessage) {
        // 获取模版
        BaseMessageTemplate messageTemplate = getMessageTemplate();
        sendMessage.setTemplateCode(messageTemplate.getTemplateCode());
        // 检查模版是否支持当前发送渠道
        checkTemplateIfSupportTheChannel(channel, messageTemplate);
        // 模版渲染
        renderTemplate(sendMessage, messageTemplate);
        return messageTemplate;
    }

    private void renderTemplate(SendMessage sendMessage, BaseMessageTemplate messageTemplate) {
        List<MessageTemplateRender> messageTemplateRenders = this.sendService.getMessageTemplateRenders();
        for (MessageTemplateRender templateRender : messageTemplateRenders) {
            // 渲染消息内容-根据content-type渲染
            String contentType = messageTemplate.getContentType();
            sendMessage.setContentType(contentType);
            if (templateRender.isSupport(contentType)) {
                String msgContent =
                        templateRender.render(messageTemplate.getMsgContent(), this.pretreatment.getVariables());
                sendMessage.setMsgContent(msgContent);
            }
            // 渲染消息主题-纯文本渲染
            if (templateRender.isSupport(ContentType.TEXT_PLAIN)) {
                String msgName = templateRender.render(messageTemplate.getMsgName(),
                        this.pretreatment.getVariables());
                sendMessage.setMsgName(msgName);
            }
        }
    }

    private void checkTemplateIfSupportTheChannel(SendChannel channel, BaseMessageTemplate messageTemplate) {
        String supportChannelStr = messageTemplate.getSupportChannels();
        if (StrUtil.isEmpty(supportChannelStr)) {
            throw new SendMessageException("Template supportChannels is null: " + this.pretreatment.getTemplate());
        }
        String[] supportChannelArr = supportChannelStr.split(",");
        boolean isSupport = false;
        for (String supportChannel : supportChannelArr) {
            if (StrUtil.isNotEmpty(supportChannel)) {
                if (SendChannel.getByName(supportChannel.trim()) == channel) {
                    isSupport = true;
                    break;
                }
            }
        }
        if (!isSupport) {
            throw new SendMessageException("The template [" + messageTemplate.getTemplateCode()
                    + "] no support the channel: " + channel.name());
        }
    }

    private BaseMessageTemplate getMessageTemplate() {
        @SuppressWarnings("unchecked")
        MessageTemplateLoader<? extends BaseMessageTemplate, Object> messageTemplateLoader =
                (MessageTemplateLoader<? extends BaseMessageTemplate, Object>) this.sendService.getMessageTemplateLoader();
        BaseMessageTemplate messageTemplate = messageTemplateLoader.loadTemplate(this.pretreatment.getTemplate());
        if (Objects.isNull(messageTemplate)) {
            throw new SendMessageException("Template no exist: " + this.pretreatment.getTemplate());
        }
        return messageTemplate;
    }
}
