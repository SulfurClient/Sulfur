package gg.sulfur.client.impl.modules.movement.speed.verus;

import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kansio
 * @created 3:31 AM
 * @project Client
 */
public class VerusGround extends SpeedMode {

    public VerusGround() {
        super("Verus Ground");
    }

    @Override
    public void onMove(MovementEvent event) {
        if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null) {
            if (mc.thePlayer.isMoving()) {
                mc.gameSettings.keyBindJump.pressed = false;
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = 0.0;
                    MotionUtils.strafe(0.61F);
                    event.setMotionY(0.41999998688698);
                }
                MotionUtils.strafe();
            }
        }
    }
}
