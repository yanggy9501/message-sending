package com.freeing.message.sending.core.aspect;

import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.SendMessage;

import java.util.Iterator;
import java.util.Map;


public class MessageSeedAspectChain {

    private final MessageSendAspectChainCallback chainCallback;

    private final Iterator<MessageSendAspect> aspectIterator;

    public MessageSeedAspectChain(Iterator<MessageSendAspect> aspectIterator,
                                  MessageSendAspectChainCallback chainCallback) {
        this.chainCallback = chainCallback;
        this.aspectIterator = aspectIterator;
    }

    /**
     * 调用下一个切面
     */
    public void next(SendMessage sendMessage,
                     BaseMessageTemplate template,
                     Map<String, Object> chainContext) {
        if (aspectIterator.hasNext()) {
            aspectIterator.next().sendAround(this, sendMessage, template, chainContext);
        } else {
            chainCallback.run(sendMessage, template);
        }
    }
}
