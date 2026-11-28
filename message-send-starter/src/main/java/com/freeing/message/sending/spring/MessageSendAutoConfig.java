package com.freeing.message.sending.spring;

import com.freeing.message.loader.local.ClasspathTemplateLoader;
import com.freeing.message.loader.local.LocalTemplateLoader;
import com.freeing.message.render.placeholder.PlaceholderRender;
import com.freeing.message.render.thymeleaf.ThymeleafRender;
import com.freeing.message.sending.core.*;
import com.freeing.message.sending.core.aspect.MessageSendAspect;
import com.freeing.message.sending.core.exception.SendMessageException;
import com.freeing.message.sending.core.loadbalancer.LoadBalancer;
import com.freeing.message.sending.core.loadbalancer.RandomLoadBalancer;
import com.freeing.message.sending.core.manager.MessageSendManager;
import com.freeing.message.sending.core.util.CollectionUtil;
import com.freeing.message.sending.platform.firebase.DefaultFirebaseMessageSend;
import com.freeing.message.sending.platform.firebase.FirebaseConfig;
import com.freeing.message.sending.platform.mail.DefaultMailMessageSend;
import com.freeing.message.sending.platform.mail.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.*;

@Configuration
@ConditionalOnMissingBean(MessageSendService.class)
@EnableConfigurationProperties({SendMessageProperties.class, MessageTemplateProperties.class})
public class MessageSendAutoConfig {

    @Autowired
    private SendMessageProperties sendMessageProperties;

    @Autowired
    private MessageTemplateProperties messageTemplateProperties;

    public SendMessageProperties getSendMessageProperties() {
        return sendMessageProperties;
    }

    public void setSendMessageProperties(SendMessageProperties sendMessageProperties) {
        this.sendMessageProperties = sendMessageProperties;
    }

    public MessageTemplateProperties getMessageTemplateProperties() {
        return messageTemplateProperties;
    }

    public void setMessageTemplateProperties(MessageTemplateProperties messageTemplateProperties) {
        this.messageTemplateProperties = messageTemplateProperties;
    }

    @Bean
    @ConditionalOnMissingBean(LoadBalancer.class)
    public LoadBalancer loadBalancer() {
        return new RandomLoadBalancer();
    }

    @Bean
    @ConditionalOnMissingBean(PlaceholderRender.class)
    public PlaceholderRender placeholderRender() {
        MessageTemplateProperties.PlaceholderProperties properties = messageTemplateProperties.getPlaceholder();
        if (properties != null) {
            return new PlaceholderRender(
                    properties.getSupportContentType(),
                    properties.getOpenToken(),
                    properties.getCloseToken());
        }
        return new PlaceholderRender();
    }

    @Bean
    @ConditionalOnMissingBean(ThymeleafRender.class)
    public ThymeleafRender thymeleafRender() {
        MessageTemplateProperties.ThymeleafProperties properties = messageTemplateProperties.getThymeleaf();
        if (properties != null) {
            return new ThymeleafRender(properties.getSupportContentType());
        }
        return new ThymeleafRender();
    }

    @Bean
    @ConditionalOnMissingBean(MessageTemplateLoader.class)
    public MessageTemplateLoader<BaseMessageTemplate, String> messageTemplateLoader() {
        if (messageTemplateProperties.getLocal() != null && messageTemplateProperties.getLocal().isEnable()) {
            return new LocalTemplateLoader(messageTemplateProperties.getLocal().getPath());
        }
        if (messageTemplateProperties.getClasspath() != null && messageTemplateProperties.getClasspath().isEnable()) {
            return new ClasspathTemplateLoader(messageTemplateProperties.getClasspath().getPath());
        }
        throw new SendMessageException("Please config the bean MessageTemplateLoader " +
                "or the properties MessageTemplateProperties");
    }

    @Bean("com.freeing.message.sending.messageSendThreadPool")
    @ConditionalOnMissingBean(name = "messageSendThreadPool")
    public ExecutorService messageSendThreadPool() {
        return new ThreadPoolExecutor(
                4,
                10,
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(1000),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @Bean
    public MessageSendService messageSendService(
            @Autowired(required = false) List<MessageSend<?>> messageSends,
            @Autowired(required = false) List<MessageTemplateRender> messageTemplateRenders,
            @Autowired(required = false) List<MessageSendAspect> aspectList,
            @Autowired(required = false) MessageTemplateLoader<?, ?> messageTemplateLoader,
            @Autowired(required = false) LoadBalancer loadBalancer,
            @Autowired @Qualifier("com.freeing.message.sending.messageSendThreadPool") ExecutorService threadPool) {

        MessageSendManager messageSendManager = new MessageSendManager();
        if (CollectionUtil.isNotEmpty(messageSends)) {
            messageSends.forEach(messageSendManager::register);
        }
        List<? extends SendMessageProperties.FirebaseConfig> firebaseList = sendMessageProperties.getFirebase();
        if (CollectionUtil.isNotEmpty(firebaseList)) {
            for (SendMessageProperties.FirebaseConfig config : firebaseList) {
                FirebaseConfig firebaseConfig = new FirebaseConfig();
                firebaseConfig.setPlatform(config.getPlatform())
                        .setGroup(config.getGroup())
                        .setProjectId(config.getProjectId())
                        .setPrivateKey(config.getPrivateKey());
                DefaultFirebaseMessageSend firebaseMessageSend = new DefaultFirebaseMessageSend(firebaseConfig);
                messageSendManager.register(firebaseMessageSend);
            }
        }

        List<? extends SendMessageProperties.MailConfig> mail = sendMessageProperties.getMail();
        if (CollectionUtil.isNotEmpty(mail)) {
            for (SendMessageProperties.MailConfig config : mail) {
                MailConfig mailConfig = new MailConfig();
                mailConfig.setPlatform(config.getPlatform())
                        .setGroup(config.getGroup())
                        .setHost(config.getHost())
                        .setPort(config.getPort())
                        .setUsername(config.getUsername())
                        .setPassword(config.getPassword())
                        .setFromAddress(config.getFromAddress())
                        .setFromPersonal(config.getFromPersonal())
                        .setProperties(config.getProperties());
                DefaultMailMessageSend mailMessageSend = new DefaultMailMessageSend(mailConfig);
                messageSendManager.register(mailMessageSend);
            }
        }

        return new MessageSendService(
                messageSendManager,
                messageTemplateRenders,
                messageTemplateLoader,
                loadBalancer,
                aspectList,
                threadPool);
    }
}
