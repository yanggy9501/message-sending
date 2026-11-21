package com.freeing.message.sending.core.template;

import java.util.Map;

/**
 * 模版渲染
 */
public interface MessageTemplateRender {

    /**
     * 判断是否支持渲染
     *
     * @param contentType 模版类型
     * @return bool
     */
    boolean isSupport(String contentType);

    /**
     * 渲染文本
     *
     * @param text HTML/Text
     * @param variables 参数变量
     * @return 使用 variables 渲染文本后的内容
     */
    String render(String text, Map<String, Object> variables);
}
