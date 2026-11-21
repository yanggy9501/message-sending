package com.freeing.message.sending.platform.inapp;

import com.freeing.message.sending.core.properties.BaseConfig;
import com.freeing.message.sending.core.constants.SendChannel;

public class InAppConfig extends BaseConfig {
    private boolean enabled;

    @Override
    public SendChannel getChannel() {
        return SendChannel.IN_APP;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

