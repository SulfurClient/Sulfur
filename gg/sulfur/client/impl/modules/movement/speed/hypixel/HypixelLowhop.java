package gg.sulfur.client.impl.modules.movement.speed.hypixel;

import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HypixelLowhop extends SpeedMode {

    public HypixelLowhop() {
        super("Hypickle LowHop");
    }

    @Override
    public void onUpdate(@Nullable UpdateEvent event) {
        if (mc.thePlayer.isMoving() && !(mc.thePlayer.isInWater() || mc.thePlayer.isInLava())) {
            if (mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.pressed && mc.thePlayer.jumpTicks == 0) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY = 0.24;
                mc.thePlayer.jumpTicks = 5;
            } else if (mc.thePlayer.motionY < 0) {
                mc.thePlayer.motionY *= 1.0625;
            }
            MotionUtils.setMotion(MotionUtils.getBaseSpeed() * 1.06575F);
        }
    }

    @Override
    public void onMove(@Nullable MovementEvent event) {
        super.onMove(event);
    }

    @Override
    public void onPacket(@Nullable PacketEvent event) {
        super.onPacket(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
