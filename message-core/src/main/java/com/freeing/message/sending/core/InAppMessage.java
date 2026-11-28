package com.freeing.message.sending.core;

import java.io.Serial;
import java.io.Serializable;

public class InAppMessage<T> extends SendMessage implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    /** 透传数据 */
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
