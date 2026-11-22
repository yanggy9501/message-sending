package com.freeing.message.sending.platform.firebase;

import com.freeing.message.sending.core.MessageSend;
import com.freeing.message.sending.core.PushMessage;
import com.freeing.message.sending.core.constants.SendChannel;

public class DefaultFirebaseMessageSend implements MessageSend<PushMessage> {
    private final String platform;
    private final String group;

    public DefaultFirebaseMessageSend(FirebaseConfig config) {
        this.platform = config.getPlatform();
        this.group = config.getGroup();
    }

    @Override
    public SendChannel getSendChannel() {
        return SendChannel.PUSH;
    }

    @Override
    public String getPlatform() {
        return platform;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public void send(PushMessage sendMessage) {

    }

}
