package com.freeing.message.sending.core;

import java.io.Serial;
import java.io.Serializable;

public class BaseMessageTemplate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模版编码
     */
    private String templateCode;

    /**
     * 消息标题/签名/主题，占位符用 $(var)表示
     */
    private String msgName;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息内容的格式 html/text
     */
    private String contentType;

    /**
     * 模版支持的发送渠道，多个渠道用逗号(,)隔开
     */
    private String supportChannels;

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSupportChannels() {
        return supportChannels;
    }

    public void setSupportChannels(String supportChannels) {
        this.supportChannels = supportChannels;
    }

    @Override
    public String toString() {
        return "BaseMessageTemplate{" +
                "templateCode='" + templateCode + '\'' +
                ", msgName='" + msgName + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", contentType='" + contentType + '\'' +
                ", supportChannels='" + supportChannels + '\'' +
                '}';
    }
}
