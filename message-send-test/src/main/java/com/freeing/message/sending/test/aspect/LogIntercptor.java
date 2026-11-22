package com.freeing.message.sending.test.aspect;

import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.SendMessage;
import com.freeing.message.sending.core.aspect.MessageSeedAspectChain;
import com.freeing.message.sending.core.aspect.MessageSendAspect;

public class LogIntercptor implements MessageSendAspect {
    @Override
    public void sendAround(MessageSeedAspectChain chain,
                           SendMessage sendMessage,
                           BaseMessageTemplate template) {
        System.out.println("开始发送消息...");
        chain.next(sendMessage, template);
        System.out.println("发送消息完成...");
    }
}
