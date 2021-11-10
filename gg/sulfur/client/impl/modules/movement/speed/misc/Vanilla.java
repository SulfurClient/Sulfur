package gg.sulfur.client.impl.modules.movement.speed.misc;

import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import gg.sulfur.client.impl.utils.player.ChatUtil;

/**
 * @author Kansio
 * @created 1:11 AM
 * @project Client
 */
public class Vanilla extends SpeedMode {

    public Vanilla() {
        super("Vanilla");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (!mc.thePlayer.isMoving()) {
            return;
        }

        MotionUtils.setMotion(getSpeed().getSpeed().getValue());
    }
}
