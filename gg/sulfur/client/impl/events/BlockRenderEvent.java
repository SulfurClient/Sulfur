package gg.sulfur.client.impl.events;

import gg.sulfur.client.api.event.Event;

public class BlockRenderEvent extends Event {

    private final double x;
    private final double y;
    private final double z;

    public BlockRenderEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
