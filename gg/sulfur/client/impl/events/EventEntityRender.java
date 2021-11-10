package gg.sulfur.client.impl.events;


import gg.sulfur.client.api.event.Event;

public class EventEntityRender extends Event {
    private final float partialTicks;

    public EventEntityRender(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}