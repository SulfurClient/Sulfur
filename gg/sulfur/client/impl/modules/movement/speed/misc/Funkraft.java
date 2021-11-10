package gg.sulfur.client.impl.modules.movement.speed.misc;

import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Funkraft extends SpeedMode {

    public Funkraft() {
        super("Funcraft");
    }

    public Funkraft(@NotNull String modeName) {
        super(modeName);
    }

    @Override
    public void onUpdate(@Nullable UpdateEvent event) {
        /*if (mc.thePlayer.fallDistance > 0.04) {
            mc.thePlayer.motionY -= 0.5f;
        }*/
    }

    @Override
    public void onMove(@Nullable MovementEvent event) {
        if (mc.thePlayer.isMovingOnGround()) {
            event.setMotionY(mc.thePlayer.motionY = MotionUtils.getMotion(0.42F));
        }
        double spedd2 = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.35 : 0.300;
        //mc.timer.timerSpeed = 1.2f;
        if (mc.thePlayer.ticksExisted % 9 == 0) {
            spedd2 = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.39 : 0.33;
        }
        MotionUtils.setMotion(event, spedd2);
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
