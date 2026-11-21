package com.freeing.message.sending.platform.mail;

import com.freeing.message.sending.core.properties.BaseConfig;
import com.freeing.message.sending.core.constants.SendChannel;

import java.util.Map;

public class MailConfig extends BaseConfig {
    /**
     * 服务器地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 邮件服务用户凭证
     */
    private String username;

    /**
     * 邮件服务密码凭证
     */
    private String password;

    /**
     * 可选 默认发件人邮箱
     */
    private String fromAddress;

    /**
     * 可选 默认发件人名称
     */
    private String fromPersonal;

    /**
     * 属性配置
     */
    private Map<String, Object> properties;

    @Override
    public SendChannel getChannel() {
        return SendChannel.MAIL;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromPersonal() {
        return fromPersonal;
    }

    public void setFromPersonal(String fromPersonal) {
        this.fromPersonal = fromPersonal;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
