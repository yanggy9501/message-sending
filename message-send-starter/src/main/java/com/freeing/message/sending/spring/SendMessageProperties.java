package com.freeing.message.sending.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnMissingBean(SendMessageProperties.class)
@ConfigurationProperties(prefix = "message.sending.channel")
public class SendMessageProperties {
    private List<MailConfig> mail;
    private List<FirebaseConfig> firebase;
    private InAppConfig inApp;

    public List<? extends MailConfig> getMail() {
        return mail;
    }

    public void setMail(List<MailConfig> mail) {
        this.mail = mail;
    }

    public List<? extends FirebaseConfig> getFirebase() {
        return firebase;
    }

    public void setFirebase(List<FirebaseConfig> firebase) {
        this.firebase = firebase;
    }

    public InAppConfig getInApp() {
        return inApp;
    }

    public void setInApp(InAppConfig inApp) {
        this.inApp = inApp;
    }

    public static class BaseConfig {
        /**
         * 渠道平台
         */
        protected String platform;

        /**
         * 分组
         */
        protected String group;

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

    public static class InAppConfig extends BaseConfig {
        /**
         * 启用
         */
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class MailConfig extends BaseConfig {
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

    public static class FirebaseConfig extends BaseConfig {
        private String projectId;

        /**
         * firebase 密钥
         * 1. classpath:xxx/xxx-firebase.json
         * 2. file:/xxx/xxx-firebase.json
         * 3. json-string
         */
        private String privateKey;

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }
}
