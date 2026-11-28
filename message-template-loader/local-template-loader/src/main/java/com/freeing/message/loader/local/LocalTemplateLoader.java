package com.freeing.message.loader.local;

import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.MessageTemplateLoader;
import com.freeing.message.sending.core.exception.SendMessageException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class LocalTemplateLoader implements MessageTemplateLoader<BaseMessageTemplate, String> {
    private static final String TEMPLATE_SUFFIX_HTML = "_template.html";
    private static final String TEMPLATE_SUFFIX_TXT = "_template.txt";
    private static final String TEMPLATE_SUFFIX_NULL = "_template";
    private static final String CONFIG_SUFFIX = "_config.properties";

    private final String path;

    public LocalTemplateLoader(String path) {
        this.path = path;
    }

    @Override
    public BaseMessageTemplate loadTemplate(String templateCode) {
        Path configPath = Path.of(path, templateCode + CONFIG_SUFFIX);
        Properties configProp = new Properties();
        try (InputStream inputStream = new FileInputStream(configPath.toFile())) {
            configProp.load(inputStream);
        } catch (Exception e) {
            throw new SendMessageException(
                    "Error loading template config-file: " + configPath.toAbsolutePath(), e);
        }
        String contentType = configProp.getProperty("contentType");
        String supportChannels = configProp.getProperty("supportChannels");
        String msgName = configProp.getProperty("msgName");

        BaseMessageTemplate messageTemplate = new BaseMessageTemplate();
        messageTemplate.setTemplateCode(templateCode);
        messageTemplate.setContentType(contentType);
        messageTemplate.setSupportChannels(supportChannels);
        messageTemplate.setMsgName(msgName);

        boolean findTmeplateFile = false;
        List<String> templateSuffixList =
                Arrays.asList(TEMPLATE_SUFFIX_HTML, TEMPLATE_SUFFIX_TXT, TEMPLATE_SUFFIX_NULL);
        for (String suffix : templateSuffixList) {
            Path templatePath = Path.of(path, templateCode + suffix);
            if (Files.exists(templatePath)) {
                try {
                    String content = Files.readString(templatePath, StandardCharsets.UTF_8);
                    messageTemplate.setMsgContent(content);
                    findTmeplateFile = true;
                    break;
                } catch (Exception e) {
                    throw new SendMessageException(
                            "Error loading template-file: " + templatePath.toAbsolutePath(), e);
                }
            }
        }
        if (!findTmeplateFile) {
            throw new SendMessageException(
                    "No template file found for template-code: " + templateCode);
        }
        return messageTemplate;
    }
}
