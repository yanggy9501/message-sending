package com.freeing.message.sending.core;

import com.freeing.message.sending.core.constants.SendChannel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SendMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID（主键）
     */
    private String id;

    /**
     * 消息模版编码
     */
    private String templateCode;

    /**
     * 消息标题/邮件主题/短信签名
     */
    private String msgName;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息的内容格式 html/text
     */
    private String contentType;

    /**
     * 发送渠道
     */
    private SendChannel sendChannel;

    /**
     * 分组
     */
    private String group;

    /**
     * 消息的推送渠道
     */
    private String platform;

    /**
     * 消息发送者
     */
    private String sendFrom;

    /**
     * 消息的接收者
     */
    private List<String> sendTo;

    /**
     * 附加属性
     */
    private Map<String, Object> attr;

    /**
     * 创建时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public SendChannel getSendChannel() {
        return sendChannel;
    }

    public void setSendChannel(SendChannel sendChannel) {
        this.sendChannel = sendChannel;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public List<String> getSendTo() {
        return sendTo;
    }

    public void setSendTo(List<String> sendTo) {
        this.sendTo = sendTo;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "id='" + id + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", msgName='" + msgName + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", contentType='" + contentType + '\'' +
                ", sendChannel=" + sendChannel +
                ", group='" + group + '\'' +
                ", platform='" + platform + '\'' +
                ", sendFrom='" + sendFrom + '\'' +
                ", sendTo=" + sendTo +
                ", attr=" + attr +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
