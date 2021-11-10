package gg.sulfur.client.impl.events;

import gg.sulfur.client.api.event.Event;

public class ChatEvent extends Event {

    private String rawMessage;

    public ChatEvent(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }
}
