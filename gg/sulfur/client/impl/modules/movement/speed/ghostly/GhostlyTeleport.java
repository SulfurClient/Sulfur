package gg.sulfur.client.impl.modules.movement.speed.ghostly;

import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kansio
 * @created 3:25 AM
 * @project Client
 */
public class GhostlyTeleport extends SpeedMode {

    public GhostlyTeleport() {
        super("Ghostly Teleport");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double x = -Math.sin(yaw) * 1.8;
        double z = Math.cos(yaw) * 1.8;

        if (!mc.thePlayer.isMoving()) return;

        if (mc.thePlayer.ticksExisted % 5 == 0) {
            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
        }
    }
}
