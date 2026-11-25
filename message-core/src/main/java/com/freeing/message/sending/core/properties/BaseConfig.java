package com.freeing.message.sending.core.properties;

import com.freeing.message.sending.core.constants.SendChannel;

/**
 * 发送平台基础配置
 */
public abstract class BaseConfig<T extends BaseConfig<?>> {
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

    public abstract T getSelf();

    public String getPlatform() {
        return platform;
    }

    public T setPlatform(String platform) {
        this.platform = platform;
        return getSelf();
    }

    public String getGroup() {
        return group;
    }

    public T setGroup(String group) {
        this.group = group;
        return getSelf();
    }
}

