package com.freeing.message.sending.test.spring;

import com.freeing.message.sending.core.InAppTarget;
import com.freeing.message.sending.core.MailTarget;
import com.freeing.message.sending.core.MessageSendService;
import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.manager.MessageSendManager;
import com.freeing.message.sending.test.inapp.bean.TransInfo;

import jakarta.activation.FileDataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestStarter {

    @Autowired private MessageSendService messageSendService;

    @Test
    public void testBean() {
        MessageSendManager messageSendManager = messageSendService.getMessageSendManager();
        System.out.println(messageSendManager);
        System.out.println(messageSendService.getMessageTemplateLoader());
        System.out.println(messageSendService.getAspectList());
        System.out.println(messageSendService.getThreadPool());
        System.out.println(messageSendService.getMessageTemplateRenders());
        System.out.println("-------");
    }

    @Test
    public void testInApp1() {
        messageSendService
                .of(
                        "hello test-inapp",
                        "this is test inapp content: transNo=#{transNo}, amount=#{amount}")
                .channel(SendChannel.IN_APP)
                .addTarget(
                        new InAppTarget()
                                .setPublisher("p-user1")
                                .addReceiver("r-user1")
                                .addReceiver("r-user2")
                                .setData(new TransInfo("1", "FX2026", "10.00")))
                .variables(Map.of("transNo", "FX2026", "amount", "10.00"))
                .putAttr("k1", "v1")
                .putAttr("k2", "v2")
                .asyncSend();
    }

    @Test
    public void testInApp2() {
        messageSendService
                .of("trans_issue")
                .channel(SendChannel.IN_APP)
                .addTarget(
                        new InAppTarget()
                                .setPublisher("p-user1")
                                .addReceiver("r-user1")
                                .addReceiver("r-user2")
                                .setData(new TransInfo("1", "FX2026", "10.00")))
                .variables(Map.of("transType", "ISSUE", "amount", "10.00", "status", "success"))
                .putAttr("k1", "v1")
                .putAttr("k2", "v2")
                .syncSend();
    }

    @Test
    public void testMail() {
        messageSendService
                .of("trans_issue")
                .channel(SendChannel.IN_APP)
                .addTarget(
                        new MailTarget()
                                .setFrom("your-mail@qq.com")
                                .setTo(List.of("your-mail@qq.com"))
                                .setCc(List.of("your-mail@qq.com"))
                                .addAttachment(
                                        new FileDataSource(
                                                "message-send-test/src/test/local-template/trans_issue_template.txt")))
                .variables(Map.of("transType", "ISSUE", "amount", "10.00", "status", "success"))
                .putAttr("k1", "v1")
                .putAttr("k2", "v2")
                .syncSend();
        System.out.println(messageSendService);
    }
}
