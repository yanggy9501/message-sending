package com.freeing.message.sending.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "message.sending.template")
public class MessageTemplateProperties {

    private LocalTemplateProperties local;

    private ClassPathTemplateProperties classpath;

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
}
