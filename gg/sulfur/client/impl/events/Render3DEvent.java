package gg.sulfur.client.impl.events;

import gg.sulfur.client.api.event.Event;

public class Render3DEvent extends Event {
    private final float ticks;

    public Render3DEvent(float ticks) {
        this.ticks = ticks;
    }

    public float getPartialTicks() {
        return ticks;
    }
}
