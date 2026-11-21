package com.freeing.message.sending.test.inapp;

import com.freeing.message.sending.core.AbstractInAppMessageSend;
import com.freeing.message.sending.core.InAppSendMessage;
import com.freeing.message.sending.test.inapp.bean.TransInfo;

public class InAppMessageSend extends AbstractInAppMessageSend<TransInfo> {

    @Override
    public void send(InAppSendMessage<TransInfo> message) {
        System.out.println(message);
        TransInfo data = message.getData();
        System.out.println(data);
    }
}
