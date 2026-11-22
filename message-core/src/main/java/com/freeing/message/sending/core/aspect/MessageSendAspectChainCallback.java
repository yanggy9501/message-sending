package com.freeing.message.sending.core.aspect;

import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.SendMessage;
import com.freeing.message.sending.core.compose.SendMessagePretreatment;

public interface MessageSendAspectChainCallback {
    void run(SendMessage sendMessage, BaseMessageTemplate template);
}
