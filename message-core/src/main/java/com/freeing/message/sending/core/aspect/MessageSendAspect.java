package com.freeing.message.sending.core.aspect;

import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.SendMessage;
import com.freeing.message.sending.core.compose.SendMessagePretreatment;

public interface MessageSendAspect {

    default void sendAround(
            MessageSeedAspectChain chain,
            SendMessage sendMessage,
            BaseMessageTemplate template,
            SendMessagePretreatment pre) {
        chain.next(sendMessage, template, pre);
    }
}
