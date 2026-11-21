package com.freeing.message.sending.test.direct;

import com.freeing.message.loader.local.LocalTemplateLoader;
import com.freeing.message.render.placeholder.PlaceholderRender;
import com.freeing.message.sending.core.BaseMessageTemplate;
import com.freeing.message.sending.core.InAppTarget;
import com.freeing.message.sending.core.SendService;
import com.freeing.message.sending.core.aspect.MessageSendAspect;
import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.loadbalancer.LoadBalancer;
import com.freeing.message.sending.core.loadbalancer.RandomLoadBalancer;
import com.freeing.message.sending.core.manager.MessageSendManager;
import com.freeing.message.sending.core.template.MessageTemplateLoader;
import com.freeing.message.sending.core.template.MessageTemplateRender;
import com.freeing.message.sending.test.inapp.InAppMessageSend;
import com.freeing.message.sending.test.inapp.bean.TransInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendServiceTest {

    @Test
    public void testSend1() {
        MessageSendManager messageSendManager = new MessageSendManager();
        messageSendManager.register(new InAppMessageSend());
        List<MessageTemplateRender> messageTemplateRenders = new ArrayList<>();
        MessageTemplateLoader<? extends BaseMessageTemplate, ?> messageTemplateLoader = new LocalTemplateLoader("~/tmp/msg");
        LoadBalancer loadBalancer = new RandomLoadBalancer();
        List<MessageSendAspect> aspectList = new ArrayList<>();
        SendService sendService = new SendService(messageSendManager, messageTemplateRenders, messageTemplateLoader, loadBalancer, aspectList);

        sendService.of("hello test-inapp", "this is test inapp content: transNo=#{transNo}, amount=#{amount}")
                .channel(SendChannel.IN_APP)
                .data(new TransInfo("1", "FX2026", "10.00"))
                .addTarget(new InAppTarget()
                        .setPublisher("p-user1")
                        .addReceiver("r-user1")
                        .addReceiver("r-user2")
                )
                .variables(Map.of("transNo", "FX2026", "amount", "10.00"))
                .putAttr("k1", "v1")
                .putAttr("k2", "v2")
                .syncSeed();

    }

    @Test
    public void testSend2() {
        MessageSendManager messageSendManager = new MessageSendManager();
        messageSendManager.register(new InAppMessageSend());
        List<MessageTemplateRender> messageTemplateRenders = new ArrayList<>();
        messageTemplateRenders.add(new PlaceholderRender(new String[]{"text"}));
        MessageTemplateLoader<? extends BaseMessageTemplate, ?> messageTemplateLoader =
                new LocalTemplateLoader("src/test/local-template");
        LoadBalancer loadBalancer = new RandomLoadBalancer();
        List<MessageSendAspect> aspectList = new ArrayList<>();
        SendService sendService = new SendService(messageSendManager, messageTemplateRenders, messageTemplateLoader, loadBalancer, aspectList);

        sendService.of("trans_issue")
                .channel(SendChannel.IN_APP)
                .data(new TransInfo("1", "FX2026", "10.00"))
                .addTarget(new InAppTarget()
                        .setPublisher("p-user1")
                        .addReceiver("r-user1")
                        .addReceiver("r-user2")
                )
                .variables(Map.of("transType", "ISSUE", "amount", "10.00", "status", "success"))
                .putAttr("k1", "v1")
                .putAttr("k2", "v2")
                .syncSeed();

    }
}
