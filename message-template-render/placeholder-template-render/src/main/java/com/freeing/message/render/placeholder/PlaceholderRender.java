package com.freeing.message.render.placeholder;

import com.freeing.message.sending.core.constants.ContentType;
import com.freeing.message.sending.core.MessageTemplateRender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlaceholderRender implements MessageTemplateRender {
    private final Set<String> supportContentTypeSet;
    private String openToken = "#{";
    private String closeToken = "}";

    public PlaceholderRender(String[] supportContentTypes, String openToken, String closeToken) {
        this.supportContentTypeSet = new HashSet<>();
        if (supportContentTypes != null && supportContentTypes.length > 0) {
            this.supportContentTypeSet.addAll(Arrays.asList(supportContentTypes));
        }
        this.supportContentTypeSet.add(ContentType.TEXT_PLAIN);
        this.supportContentTypeSet.add(ContentType.PLAINTEXT);
        if (openToken != null && closeToken !=  null) {
            this.openToken = openToken;
            this.closeToken = closeToken;
        }
    }

    public PlaceholderRender(String[] supportContentTypes) {
        this(supportContentTypes, null, null);
    }

    public PlaceholderRender() {
        this(new String[0], null, null);
    }

    @Override
    public boolean isSupport(String contentType) {
        return supportContentTypeSet.contains(contentType);
    }

    @Override
    public String render(String text, final Map<String, Object> variables) {
        return new GenericTokenParser(this.openToken, this.closeToken, varName -> {
            if (variables.containsKey(varName)) {
                return variables.get(varName).toString();
            }
            return this.openToken + varName + this.closeToken;
        }).parse(text);
    }
}
