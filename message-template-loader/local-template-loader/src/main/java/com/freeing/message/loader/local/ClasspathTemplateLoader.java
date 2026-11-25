package com.freeing.message.loader.local;

import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.MessageTemplateLoader;
import com.freeing.message.sending.core.exception.SendMessageException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ClasspathTemplateLoader implements MessageTemplateLoader<BaseMessageTemplate, String> {
    private final static String TEMPLATE_SUFFIX_HTML = "_template.html";
    private final static String TEMPLATE_SUFFIX_TXT = "_template.txt";
    private final static String TEMPLATE_SUFFIX_NULL = "_template";
    private final static String CONFIG_SUFFIX = "_config.properties";

    private final String path;

    public ClasspathTemplateLoader(String path) {
        if (!path.endsWith("/")) {
            path += "/";
        }
        this.path = path;
    }

    @Override
    public BaseMessageTemplate loadTemplate(String templateCode) {
        String configPath = path + templateCode + CONFIG_SUFFIX;
        Properties configProp = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configPath)) {
            configProp.load(inputStream);
        } catch (Exception e) {
            throw new SendMessageException("Error loading template config-file: " + configPath, e);
        }
        String contentType = configProp.getProperty("contentType");
        String supportChannels = configProp.getProperty("supportChannels");
        String msgName = configProp.getProperty("msgName");

        BaseMessageTemplate messageTemplate = new BaseMessageTemplate();
        messageTemplate.setTemplateCode(templateCode);
        messageTemplate.setContentType(contentType);
        messageTemplate.setSupportChannels(supportChannels);
        messageTemplate.setMsgName(msgName);

        List<String> templateSuffixList = Arrays.asList(TEMPLATE_SUFFIX_HTML, TEMPLATE_SUFFIX_TXT, TEMPLATE_SUFFIX_NULL);
        for (String suffix : templateSuffixList) {
            String templatePath = path + templateCode + suffix;
            if (Files.exists(Path.of(templatePath))) {
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
                    if (inputStream == null) {
                        throw new SendMessageException("Template file not found: " + templatePath);
                    }
                    String templateContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    messageTemplate.setMsgContent(templateContent);

                } catch (Exception e) {
                    throw new SendMessageException("Error loading template-file: " + templatePath, e);
                }
            }
        }
        return messageTemplate;
    }
}
