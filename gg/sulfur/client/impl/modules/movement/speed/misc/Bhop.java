package gg.sulfur.client.impl.modules.movement.speed.misc;

import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.Speed;
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kansio
 * @created 3:20 AM
 * @project Client
 */
public class Bhop extends SpeedMode {

    public Bhop() {
        super("Bhop");
    }

    @Override
    public void onUpdate(@Nullable UpdateEvent event) {
        if (!mc.thePlayer.isMoving()) {
            return;
        }

        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }

        MotionUtils.setMotion(getSpeed().getSpeed().getValue());
    }
}
