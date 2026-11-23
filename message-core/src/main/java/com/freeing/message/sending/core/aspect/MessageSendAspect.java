package com.freeing.message.sending.core.aspect;

import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.SendMessage;

import java.util.Map;

public interface MessageSendAspect {

    default void sendAround(
            MessageSeedAspectChain chain,
            SendMessage sendMessage,
            BaseMessageTemplate template,
            Map<String, Object> chainContext) {
        chain.next(sendMessage, template, chainContext);
    }
}
