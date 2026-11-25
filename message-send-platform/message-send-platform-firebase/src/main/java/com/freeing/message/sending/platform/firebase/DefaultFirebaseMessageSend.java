package com.freeing.message.sending.platform.firebase;

import com.freeing.message.sending.core.MessageSend;
import com.freeing.message.sending.core.PushMessage;
import com.freeing.message.sending.core.constants.SendChannel;
import com.freeing.message.sending.core.exception.SendMessageException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class DefaultFirebaseMessageSend implements MessageSend<PushMessage> {
    private final String platform;
    private final String group;
    private final String projectId;
    private final FirebaseConfig config;
    private FirebaseMessaging instance;

    public DefaultFirebaseMessageSend(FirebaseConfig config) {
        this.platform = config.getPlatform();
        this.group = config.getGroup();
        this.projectId = config.getProjectId();
        this.config = config;
        init();
    }

    private void init() {
        String privateKey = this.config.getPrivateKey();
        if (privateKey == null) {
            throw new SendMessageException("Firebase privateKey can't be empty");
        }
        if (privateKey.startsWith(FirebaseConfig.PREFIX_CLASSPATH)) {
            String classpath = privateKey.substring(FirebaseConfig.PREFIX_CLASSPATH.length());
            try (InputStream in = this.getClass().getResourceAsStream(classpath)) {
                if (in == null) {
                    throw new SendMessageException("Firebase classpath resource not found: " + classpath);
                }
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(in))
                        .build();
                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, this.projectId);
                this.instance = FirebaseMessaging.getInstance(firebaseApp);
            } catch (Exception e) {
                throw new SendMessageException(e);
            }
        } else if (privateKey.startsWith(FirebaseConfig.PREFIX_FILE)) {
            String filePath = privateKey.substring(FirebaseConfig.PREFIX_FILE.length());
            try (InputStream in = new FileInputStream(filePath)) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(in))
                        .build();
                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, this.projectId);
                this.instance = FirebaseMessaging.getInstance(firebaseApp);
            } catch (Exception e) {
                throw new SendMessageException(e);
            }
        } else {
            try (InputStream in = new ByteArrayInputStream(config.getPrivateKey().getBytes(StandardCharsets.UTF_8))) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(in))
                        .build();
                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, this.projectId);
                this.instance = FirebaseMessaging.getInstance(firebaseApp);
            } catch (Exception e) {
                throw new SendMessageException(e);
            }
        }
    }

    @Override
    public SendChannel getSendChannel() {
        return SendChannel.PUSH;
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
    public void send(PushMessage sendMessage) {
        MulticastMessage message = buildMessage(sendMessage);
        try {
            instance.sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new SendMessageException(e);
        }
    }

    private MulticastMessage buildMessage(PushMessage pushMessage) {
        List<String> tokens = pushMessage.getSendTo();
        String title = pushMessage.getMsgName();
        String body = pushMessage.getMsgContent();
        String imageUrl = pushMessage.getImageUrl();
        String clickAction = pushMessage.getClickAction();
        Map<String, String> data = pushMessage.getData();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setClickAction(clickAction)
                        .setImage(imageUrl)
                        .build())
                .build();

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setBadge(0)
                        .setCategory(clickAction)
                        .setMutableContent(true)
                        .setAlert(ApsAlert.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .build())
                .putHeader("apns-priority", "10")
                .build();

        MulticastMessage.Builder messageBuilder = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig);

        if (data != null && !data.isEmpty()) {
            messageBuilder.putAllData(data);
        }
        return messageBuilder.build();
    }
}
