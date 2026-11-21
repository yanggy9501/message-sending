package com.freeing.message.sending.core.constants;

public enum SendChannel {
    MAIL,
    IN_APP,
    SMS,
    PUSH,
    UNKNOWN;

    public static SendChannel getByName(String name) {
        for (SendChannel sendChannel : SendChannel.values()) {
            if (sendChannel.name().equalsIgnoreCase(name)) {
                return sendChannel;
            }
        }
        return UNKNOWN;
    }
}
