package com.freeing.message.sending.platform.firebase;

import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.properties.BaseConfig;

public class FirebaseConfig extends BaseConfig<FirebaseConfig> {

    private String projectId;

    private String privateKey;

    @Override
    public SendChannel getChannel() {
        return SendChannel.PUSH;
    }

    @Override
    public FirebaseConfig getSelf() {
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public FirebaseConfig setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public FirebaseConfig setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }
}

