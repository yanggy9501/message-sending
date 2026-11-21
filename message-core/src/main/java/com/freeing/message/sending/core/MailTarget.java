package com.freeing.message.sending.core;

import jakarta.activation.DataSource;

import java.util.List;

public class MailTarget extends Target {
    /**
     * 发送人邮箱地址
     */
    private String from;

    /**
     * 接收送人邮箱地址
     */
    private List<String> to;

    /**
     * 抄送
     */
    private List<String> cc;

    /**
     * 秘抄
     */
    private List<String> bcc;

    /**
     * 附件
     */
    private List<DataSource> attachments;

    /**
     * 内嵌资源
     */
    private List<DataSource> inlineSources;

    public String getFrom() {
        return from;
    }

    public MailTarget setFrom(String from) {
        this.from = from;
        return this;
    }

    public List<String> getTo() {
        return to;
    }

    public MailTarget setTo(List<String> to) {
        this.to = to;
        return this;
    }

    public List<String> getCc() {
        return cc;
    }

    public MailTarget setCc(List<String> cc) {
        this.cc = cc;
        return this;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public MailTarget setBcc(List<String> bcc) {
        this.bcc = bcc;
        return this;
    }

    public List<DataSource> getAttachments() {
        return attachments;
    }

    public MailTarget setAttachments(List<DataSource> attachments) {
        this.attachments = attachments;
        return this;
    }

    public List<DataSource> getInlineSources() {
        return inlineSources;
    }

    public MailTarget setInlineSources(List<DataSource> inlineSources) {
        this.inlineSources = inlineSources;
        return this;
    }
}
