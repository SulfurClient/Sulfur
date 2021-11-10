package gg.sulfur.client.impl.modules.movement.flight.mush;

import gg.sulfur.client.impl.events.BlockCollisionEvent;
import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.flight.FlightMode;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CopyOnWriteArrayList;

public class Mush extends FlightMode {

    private CopyOnWriteArrayList<Packet> savedPackets = new CopyOnWriteArrayList<Packet>();
    Stopwatch stopwatch = new Stopwatch();
    double speedy = 2.5;
    boolean blinking = false;

    public Mush() {
        super("Mush");
    }

    @Override
    public void onPacket(@Nullable PacketEvent event) {
        if (blinking) {
            if (event.getPacket() instanceof C03PacketPlayer) {
                savedPackets.add(event.getPacket());
                event.setCancelled(true);
            }
        }

    }

    @Override
    public void onCollide(@Nullable BlockCollisionEvent event) {
        if (event.getBlock() instanceof BlockAir) {
            if (mc.thePlayer.isSneaking())
                return;
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();
            if (y < mc.thePlayer.posY) {
                event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-5, -1, -5, 5, 1.0F, 5).offset(x, y, z));
            }
        }
    }

    @Override
    public void onUpdate(@Nullable UpdateEvent event) {
        if (mc.thePlayer.ticksExisted % 18 == 0) {
            stopBlink();
            //toggle();
        }

        if (speedy >= 0.42) {
            if (stopwatch.timeElapsed(250)) {
                //speedy -= 0.12;
                if (blinking) {
                    speedy -= getFlight().getSpeed().getValue() / 100;
                } else {
                    speedy -= getFlight().getSpeed().getValue() / 25;
                }
                stopwatch.resetTime();
            }
        }
    }

    @Override
    public void onMove(@Nullable MovementEvent event) {
        MotionUtils.setMotion(speedy);
    }

    @Override
    public void onEnable() {
        speedy = getFlight().getSpeed().getValue() / 2;
        blinking = true;
        mc.thePlayer.performHurtAnimation();
        mc.thePlayer.motionY = 0.2F;
        MotionUtils.setMotion(0);
        stopwatch.resetTime();

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        if (blinking) stopBlink();
    }

    public void stopBlink() {
        for (Packet packet : savedPackets) {
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        savedPackets.clear();
        //boolean wasBlinking = blinking;
        blinking = false;
    }

}
