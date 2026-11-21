package com.freeing.message.sending.core.template;

import com.freeing.message.sending.core.BaseMessageTemplate;

public interface MessageTemplateLoader<T extends BaseMessageTemplate, I> {

    T loadTemplate(I templateParam);
}
