package com.freeing.message.sending.core.properties;

import com.freeing.message.sending.core.constants.SendChannel;

/**
 * 发送平台基础配置
 */
public abstract class BaseConfig {
    /**
     * 渠道平台
     */
    protected String platform;

    /**
     * 分组
     */
    protected String group;

    /**
     * 发送渠道
     */
    public abstract SendChannel getChannel();

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

