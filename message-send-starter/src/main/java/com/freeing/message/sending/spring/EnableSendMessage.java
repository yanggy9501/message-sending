package com.freeing.message.sending.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({MessageTemplateProperties.class, SendMessageProperties.class})
public @interface EnableSendMessage {

}
