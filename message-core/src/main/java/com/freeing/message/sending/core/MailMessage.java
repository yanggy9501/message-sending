package com.freeing.message.sending.core;

import jakarta.activation.DataSource;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class MailMessage extends SendMessage implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    /** 抄送 */
    private List<String> cc;

    /** 秘抄 */
    private List<String> bcc;

    /** 附件 */
    private List<DataSource> attachments;

    /** 内嵌资源 */
    private List<DataSource> inlineSources;

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public List<DataSource> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<DataSource> attachments) {
        this.attachments = attachments;
    }

    public List<DataSource> getInlineSources() {
        return inlineSources;
    }

    public void setInlineSources(List<DataSource> inlineSources) {
        this.inlineSources = inlineSources;
    }
}
