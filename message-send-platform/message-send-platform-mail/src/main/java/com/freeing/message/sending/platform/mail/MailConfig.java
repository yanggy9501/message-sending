package com.freeing.message.sending.platform.mail;

import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.properties.BaseConfig;

import java.util.Map;

public class MailConfig extends BaseConfig<MailConfig> {
    /** 服务器地址 */
    private String host;

    /** 端口 */
    private int port;

    /** 邮件服务用户凭证 */
    private String username;

    /** 邮件服务密码凭证 */
    private String password;

    /** 可选 默认发件人邮箱 */
    private String fromAddress;

    /** 可选 默认发件人名称 */
    private String fromPersonal;

    /** 属性配置 */
    private Map<String, Object> properties;

    @Override
    public SendChannel getChannel() {
        return SendChannel.MAIL;
    }

    @Override
    public MailConfig getSelf() {
        return this;
    }

    public String getHost() {
        return host;
    }

    public MailConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public MailConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MailConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MailConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public MailConfig setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public String getFromPersonal() {
        return fromPersonal;
    }

    public MailConfig setFromPersonal(String fromPersonal) {
        this.fromPersonal = fromPersonal;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public MailConfig setProperties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }
}
