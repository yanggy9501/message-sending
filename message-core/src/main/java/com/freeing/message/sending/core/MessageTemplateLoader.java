package com.freeing.message.sending.core;

public interface MessageTemplateLoader<T extends BaseMessageTemplate, I> {

    T loadTemplate(I templateParam);
}
