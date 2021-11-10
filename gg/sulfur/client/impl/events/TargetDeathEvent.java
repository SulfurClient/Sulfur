package gg.sulfur.client.impl.events;

import gg.sulfur.client.api.event.Event;
import net.minecraft.entity.Entity;

/**
 * @author Kansio
 * @created 3:58 PM
 * @project Client
 */
public class TargetDeathEvent extends Event {

    private final Entity entity;

    public TargetDeathEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}