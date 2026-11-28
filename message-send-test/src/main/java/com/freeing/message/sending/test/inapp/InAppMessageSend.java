package com.freeing.message.sending.test.inapp;

import com.freeing.message.sending.core.AbstractInAppMessageSend;
import com.freeing.message.sending.core.InAppMessage;
import com.freeing.message.sending.test.inapp.bean.TransInfo;
import org.springframework.stereotype.Component;

@Component
public class InAppMessageSend extends AbstractInAppMessageSend<TransInfo> {

    @Override
    public void send(InAppMessage<TransInfo> message) {
        System.out.println(message);
        TransInfo data = message.getData();
        System.out.println(data);
    }
}
