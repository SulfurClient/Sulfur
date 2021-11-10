package gg.sulfur.client.impl.modules.movement.flight.misc;

import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.modules.movement.flight.FlightMode;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kansio
 * @created 2:54 AM
 * @project Client
 */
public class Motion extends FlightMode {

    public Motion() {
        super("Motion");
    }

    @Override
    public void onMove(@Nullable MovementEvent event) {
        event.setMotionY(mc.thePlayer.motionY = (mc.thePlayer.movementInput.jump ? getFlight().getSpeed().getValue() : mc.thePlayer.movementInput.sneak ? -getFlight().getSpeed().getValue() : 0) * 0.5D);
        MotionUtils.setMotion(event, getFlight().getSpeed().getValue());
    }
}
