package com.freeing.message.sending.core.aspect;

import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.SendMessage;

public interface MessageSendAspect {

    default void sendAround(
            MessageSeedAspectChain chain,
            SendMessage sendMessage,
            BaseMessageTemplate template) {
        chain.next(sendMessage, template);
    }
}
