package com.freeing.message.sending.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(MessageTemplateProperties.class)
@ConfigurationProperties(prefix = "message.sending.template")
public class MessageTemplateProperties {

    private LocalTemplateProperties local;

    private ClassPathTemplateProperties classpath;

    private PlaceholderProperties placeholder;

    private ThymeleafProperties thymeleaf;

    public LocalTemplateProperties getLocal() {
        return local;
    }

    public void setLocal(LocalTemplateProperties local) {
        this.local = local;
    }

    public ClassPathTemplateProperties getClasspath() {
        return classpath;
    }

    public void setClasspath(ClassPathTemplateProperties classpath) {
        this.classpath = classpath;
    }

    public PlaceholderProperties getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(PlaceholderProperties placeholder) {
        this.placeholder = placeholder;
    }

    public ThymeleafProperties getThymeleaf() {
        return thymeleaf;
    }

    public void setThymeleaf(ThymeleafProperties thymeleaf) {
        this.thymeleaf = thymeleaf;
    }

    public static class LocalTemplateProperties {
        private boolean enable;
        private String path;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class ClassPathTemplateProperties {
        private boolean enable;
        private String path;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class PlaceholderProperties {
        private String[] supportContentType;

        private String openToken = "#{";

        private String closeToken = "}";

        public String[] getSupportContentType() {
            return supportContentType;
        }

        public void setSupportContentType(String[] supportContentType) {
            this.supportContentType = supportContentType;
        }

        public String getOpenToken() {
            return openToken;
        }

        public void setOpenToken(String openToken) {
            this.openToken = openToken;
        }

        public String getCloseToken() {
            return closeToken;
        }

        public void setCloseToken(String closeToken) {
            this.closeToken = closeToken;
        }
    }

    public static class ThymeleafProperties {
        private String[] supportContentType;

        public String[] getSupportContentType() {
            return supportContentType;
        }

        public void setSupportContentType(String[] supportContentType) {
            this.supportContentType = supportContentType;
        }
    }
}
