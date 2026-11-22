package com.freeing.message.sending.platform.mail;

import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.exception.SendMessageException;
import com.freeing.message.sending.core.MailMessage;
import com.freeing.message.sending.core.MessageSend;
import com.freeing.message.sending.core.util.CollectionUtil;
import com.freeing.message.sending.core.util.StrUtil;
import jakarta.activation.DataSource;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;
import java.util.Properties;

public class DefaultMailMessageSend implements MessageSend<MailMessage> {
    private static final String UTF8 = "UTF-8";
    private static final String HTML = "html";

    private final String platform;
    private final String group;
    private final JavaMailSender mailSender;
    private final MailConfig config;

    public DefaultMailMessageSend(MailConfig config) {
        this.platform = config.getPlatform();
        this.group = config.getGroup();
        this.config = config;
        // create mail client
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(config.getHost());
        mailSender.setPort(config.getPort());
        mailSender.setUsername(config.getUsername());
        mailSender.setPassword(config.getPassword());
        mailSender.setDefaultEncoding(UTF8);
        Properties props = new Properties();
        if (config.getProperties() != null) {
            props.putAll(config.getProperties());
        }
        mailSender.setJavaMailProperties(props);
        this.mailSender = mailSender;
    }

    @Override
    public SendChannel getSendChannel() {
        return SendChannel.MAIL;
    }

    @Override
    public String getPlatform() {
        return platform;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public void send(MailMessage sendMessage) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            boolean hasMultipart = CollectionUtil.isEmpty(sendMessage.getAttachments()) ||
                    CollectionUtil.isEmpty(sendMessage.getInlineSources());
            boolean isHtml = sendMessage.getContentType() != null &&
                    sendMessage.getContentType().toLowerCase().contains(HTML);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, hasMultipart, UTF8);

            // from
            String fromAddress = config.getFromAddress();
            String fromPersonal = config.getFromPersonal();
            // default from1
            if (StrUtil.isNotEmpty(fromAddress)) {
                helper.setFrom(fromAddress);
            }
            // override the default from1 if set personal
            if (StrUtil.isNotEmpty(fromAddress) && StrUtil.isNotEmpty(fromPersonal)) {
                helper.setFrom(fromAddress, fromPersonal);
            }
            // override the up if set sendFrom (the most priority)
            if (StrUtil.isNotEmpty(sendMessage.getSendFrom())) {
                helper.setFrom(sendMessage.getSendFrom());
            }

            // to
            if (CollectionUtil.isNotEmpty(sendMessage.getSendTo())) {
                helper.setTo(sendMessage.getSendTo().toArray(new String[0]));
            }
            // cc
            if (CollectionUtil.isNotEmpty(sendMessage.getCc())) {
                helper.setCc(sendMessage.getCc().toArray(new String[0]));
            }
            // bcc
            if (CollectionUtil.isNotEmpty(sendMessage.getBcc())) {
                helper.setBcc(sendMessage.getBcc().toArray(new String[0]));
            }
            // subject + content
            helper.setSubject(sendMessage.getMsgName());
            helper.setText(sendMessage.getMsgContent(), isHtml);
            // attachment
            List<DataSource> attachments = sendMessage.getAttachments();
            if (CollectionUtil.isNotEmpty(attachments)) {
                for (DataSource attachment : attachments) {
                    helper.addAttachment(attachment.getName(), attachment);
                }
            }
            // inline
            List<DataSource> inlineSources = sendMessage.getInlineSources();
            if (CollectionUtil.isNotEmpty(inlineSources)) {
                for (DataSource inlineSource : inlineSources) {
                    helper.addInline(inlineSource.getName(), inlineSource);
                }
            }
            mailSender.send(message);
        } catch (Exception e) {
            throw new SendMessageException(e);
        }
    }

}
