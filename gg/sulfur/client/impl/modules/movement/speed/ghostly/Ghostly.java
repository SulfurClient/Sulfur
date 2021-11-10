package gg.sulfur.client.impl.modules.movement.speed.ghostly;

import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kansio
 * @created 3:21 AM
 * @project Client
 */
public class Ghostly extends SpeedMode {

    public Ghostly() {
        super("Ghostly");
    }

    @Override
    public void onMove(MovementEvent event) {
        if (mc.thePlayer.ticksExisted % 3 == 0) {
            MotionUtils.setMotion(event, getSpeed().getSpeed().getValue());
        } else {
            MotionUtils.setMotion(0.4);
        }
    }
}
