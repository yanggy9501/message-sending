package com.freeing.message.sending.test.direct;

import com.freeing.message.loader.local.LocalTemplateLoader;
import com.freeing.message.render.placeholder.PlaceholderRender;
import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.InAppTarget;
import com.freeing.message.sending.core.MailTarget;
import com.freeing.message.sending.core.MessageSendService;
import com.freeing.message.sending.core.aspect.MessageSendAspect;
import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.loadbalancer.LoadBalancer;
import com.freeing.message.sending.core.loadbalancer.RandomLoadBalancer;
import com.freeing.message.sending.core.manager.MessageSendManager;
import com.freeing.message.sending.core.MessageTemplateLoader;
import com.freeing.message.sending.core.MessageTemplateRender;
import com.freeing.message.sending.platform.mail.DefaultMailMessageSend;
import com.freeing.message.sending.platform.mail.MailConfig;
import com.freeing.message.sending.test.aspect.LogIntercptor;
import com.freeing.message.sending.test.inapp.InAppMessageSend;
import com.freeing.message.sending.test.inapp.bean.TransInfo;
import jakarta.activation.FileDataSource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class MessageSendServiceTest {

    @Test
    public void testInAppSend1() {
        MessageSendManager messageSendManager = new MessageSendManager();
        messageSendManager.register(new InAppMessageSend());
        List<MessageTemplateRender> messageTemplateRenders = new ArrayList<>();
        MessageTemplateLoader<? extends BaseMessageTemplate, ?> messageTemplateLoader = new LocalTemplateLoader("~/tmp/msg");
        LoadBalancer loadBalancer = new RandomLoadBalancer();
        List<MessageSendAspect> aspectList = new ArrayList<>();
        MessageSendService sendService = new MessageSendService(messageSendManager,
                messageTemplateRenders,
                messageTemplateLoader,
                loadBalancer,
                aspectList,
                Executors.newFixedThreadPool(10));

        sendService.of("hello test-inapp", "this is test inapp content: transNo=#{transNo}, amount=#{amount}")
                .channel(SendChannel.IN_APP)
                .addData(SendChannel.IN_APP, new TransInfo("1", "FX2026", "10.00"))
                .addTarget(new InAppTarget()
                        .setPublisher("p-user1")
                        .addReceiver("r-user1")
                        .addReceiver("r-user2")
                )
                .variables(Map.of("transNo", "FX2026", "amount", "10.00"))
                .putAttr("k1", "v1")
                .putAttr("k2", "v2")
                .syncSend();

    }

    @Test
    public void testInAppSend2() {
        MessageSendManager messageSendManager = new MessageSendManager();
        messageSendManager.register(new InAppMessageSend());
        List<MessageTemplateRender> messageTemplateRenders = new ArrayList<>();
        messageTemplateRenders.add(new PlaceholderRender(new String[]{"text"}));
        MessageTemplateLoader<? extends BaseMessageTemplate, ?> messageTemplateLoader =
                new LocalTemplateLoader("src/test/local-template");
        LoadBalancer loadBalancer = new RandomLoadBalancer();
        List<MessageSendAspect> aspectList = new ArrayList<>();
        aspectList.add(new LogIntercptor());
        MessageSendService sendService = new MessageSendService(messageSendManager,
                messageTemplateRenders,
                messageTemplateLoader,
                loadBalancer,
                aspectList,
                Executors.newFixedThreadPool(10));

        sendService.of("trans_issue")
                .channel(SendChannel.IN_APP)
                .addData(SendChannel.IN_APP, new TransInfo("1", "FX2026", "10.00"))
                .addTarget(new InAppTarget()
                        .setPublisher("p-user1")
                        .addReceiver("r-user1")
                        .addReceiver("r-user2")
                )
                .variables(Map.of("transType", "ISSUE", "amount", "10.00", "status", "success"))
                .putAttr("k1", "v1")
                .putAttr("k2", "v2")
                .syncSend();
    }

    public static void main(String[] args) {
        new MessageSendServiceTest().testMailSend1();
    }

    private void testMailSend1() {
        MessageSendManager messageSendManager = new MessageSendManager();
        messageSendManager.register(new InAppMessageSend());
        // mail
        HashMap<String, Object> map = new HashMap<>();
        map.put("mail.smtp.ssl.enable", "true");
        map.put("mail.smtp.protocol", "smtps");
        MailConfig mailConfig = new MailConfig();
        mailConfig.setHost("smtp.qq.com")
                .setPort(465)
                // todo
                .setUsername("your-mail@qq.com")
                .setPassword("password")
                .setProperties(map)
                .setPlatform("qq-server1");

        DefaultMailMessageSend mailMessageSend = new DefaultMailMessageSend(mailConfig);
        messageSendManager.register(mailMessageSend);

        List<MessageTemplateRender> messageTemplateRenders = new ArrayList<>();
        messageTemplateRenders.add(new PlaceholderRender(new String[]{"text"}));
        MessageTemplateLoader<? extends BaseMessageTemplate, ?> messageTemplateLoader =
                new LocalTemplateLoader("message-send-test/src/test/local-template");
        LoadBalancer loadBalancer = new RandomLoadBalancer();
        List<MessageSendAspect> aspectList = new ArrayList<>();
        MessageSendService sendService = new MessageSendService(messageSendManager,
                messageTemplateRenders,
                messageTemplateLoader,
                loadBalancer,
                aspectList,
                Executors.newFixedThreadPool(10));

        sendService.of("trans_issue")
                .channel(SendChannel.IN_APP, SendChannel.MAIL)
                // data数据不兼容：in-app,push等透彻的不一样
                .addData(SendChannel.IN_APP, new TransInfo("1", "FX2026", "10.00"))
                .addTarget(new InAppTarget()
                        .setPublisher("p-user1")
                        .addReceiver("r-user1")
                        .addReceiver("r-user2")
                )
                .addTarget(new MailTarget()
                        .setFrom("your-mail@qq.com")
                        .setTo(List.of("your-mail@qq.com"))
                        .setCc(List.of("your-mail@qq.com"))
                        .addAttachment(new FileDataSource("message-send-test/src/test/local-template/trans_issue_template.txt"))
                )
                .variables(Map.of("transType", "ISSUE", "amount", "10.00", "status", "success"))
                .putAttr("k1", "v1")
                .putAttr("k2", "v2")
                .asyncSend();
    }

}
