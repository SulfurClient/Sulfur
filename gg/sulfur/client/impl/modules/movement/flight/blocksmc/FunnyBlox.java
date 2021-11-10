package gg.sulfur.client.impl.modules.movement.flight.blocksmc;

import gg.sulfur.client.impl.events.BlockCollisionEvent;
import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.flight.FlightMode;
import gg.sulfur.client.impl.utils.combat.MathUtils;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.concurrent.CopyOnWriteArrayList;

public class FunnyBlox extends FlightMode {

    private CopyOnWriteArrayList<Packet> savedPackets = new CopyOnWriteArrayList<Packet>();
    Stopwatch stopwatch = new Stopwatch();
    double speedy = 2.5;
    boolean boosted = false;

    public FunnyBlox() {
        super("TextFly932545xd");
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
        float ticks = 0;
        if (boosted) {
            assert event != null;
            if (event.isPre()) {
                ticks++;
            }
        }
        /*if (mc.thePlayer != null) {
            double bps = mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ) * (Minecraft.getMinecraft().timer.ticksPerSecond * Minecraft.getMinecraft().timer.timerSpeed);
            ChatUtil.displayMessage("Current BPS: " + new DecimalFormat("0.##").format(bps));
        }*/
        if (mc.thePlayer.hurtResistantTime >= 1) {
            //ChatUtil.displayMessage("hurtTime x2: " +mc.thePlayer.hurtTime * 2 + "");
            //ChatUtil.displayMessage("hurtResistantTime: " + mc.thePlayer.hurtResistantTime);
            if (!boosted) {
                boosted = true;
            } else {
                ticks++;
                speedy = getFlight().getSpeed().getValue()/* * 4*/;
            }
        } else if (ticks == 25) {
            ChatUtil.displayMessage("test call");
            speedy = 0.22f;
        }

        if (mc.thePlayer.isCollidedHorizontally && ticks <= 24) {
            speedy = 0.22f;
        }

        if (speedy >= 0.42) {
            if (stopwatch.timeElapsed(250)) {
                //speedy -= 0.12;
                if (boosted) {
                    //speedy -= getFlight().getSpeed().getValue();
                } else {
                    speedy = 0.22f;
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
        ChatUtil.displayChatMessage(" " + boosted);
        boosted = false;
        speedy = 0.22f;
        //mc.thePlayer.performHurtAnimation();
        //mc.thePlayer.motionY = 0.2F;
        MotionUtils.setMotion(0);
        stopwatch.resetTime();
        MotionUtils.damageVerus();
    }

    @Override
    public void onDisable() {
        ChatUtil.displayChatMessage(" " + boosted);
        mc.timer.timerSpeed = 1f;
        boosted = false;
    }

}
