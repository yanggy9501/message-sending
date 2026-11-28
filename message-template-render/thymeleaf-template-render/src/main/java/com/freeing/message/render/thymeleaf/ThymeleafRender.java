package com.freeing.message.render.thymeleaf;

import com.freeing.message.sending.core.MessageTemplateRender;
import com.freeing.message.sending.core.constants.ContentType;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThymeleafRender implements MessageTemplateRender {
    private final Set<String> supportContentTypeSet;
    private final TemplateEngine engine;

    public ThymeleafRender(String[] supportContentTypes) {
        this.supportContentTypeSet = new HashSet<>();
        if (supportContentTypes != null && supportContentTypes.length > 0) {
            this.supportContentTypeSet.addAll(Arrays.asList(supportContentTypes));
        }
        this.supportContentTypeSet.add(ContentType.TEXT_PLAIN_THYMELEAF);
        this.supportContentTypeSet.add(ContentType.THYMELEAF);
        this.engine = new TemplateEngine();
    }

    public ThymeleafRender() {
        this(new String[0]);
    }

    @Override
    public boolean isSupport(String contentType) {
        return supportContentTypeSet.contains(contentType);
    }

    @Override
    public String render(String html, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return this.engine.process(html, context);
    }
}
