package gg.sulfur.client.impl.events;

import gg.sulfur.client.api.event.Event;

public class KeyboardEvent extends Event {

    private final int key;

    public KeyboardEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
