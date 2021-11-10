package gg.sulfur.client.impl.events;

import gg.sulfur.client.api.event.Event;

public class SprintEvent extends Event {
    public boolean sprinting;

    public SprintEvent(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }
}
