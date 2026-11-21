package com.freeing.message.render.placeholder;

public interface TokenHandler {
    /**
     * 处理， 如：#{varName}
     *
     * @param varName
     * @return
     */
    String handleToken(String varName);
}
