package gg.sulfur.client.impl.modules.movement.flight.verus;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.events.BlockCollisionEvent;
import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.managers.NotificationManager;
import gg.sulfur.client.impl.modules.movement.flight.FlightMode;
import gg.sulfur.client.impl.notification.type.NotificationType;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import gg.sulfur.client.impl.utils.time.TimerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.management.Notification;
import java.text.DecimalFormat;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class VerusDamage extends FlightMode {

    private CopyOnWriteArrayList<Packet> savedPackets = new CopyOnWriteArrayList<Packet>();
    Stopwatch stopwatch = new Stopwatch();
    Stopwatch gameSpeedStopwatch = new Stopwatch();
    Stopwatch damageTicksThing = new Stopwatch();
    double speedy = 2.5;
    private boolean pearled = false;
    private TimerUtil timer = new TimerUtil();

    private long lastToggledTime;

    private boolean boosted = false;
    private boolean canBoost = false;

    public VerusDamage() {
        super("Verus Damage");
    }

    @Override
    public void onPacket(@Nullable PacketEvent event) {

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
        if (mc.thePlayer.hurtTime >= 1 && canBoost) {
            if (!boosted) {
                speedy = getFlight().getSpeed().getValue()/* * 4*/;
                boosted = true;
            }
        } else {
            speedy = 0.28f;
        }
    }

    @Override
    public void onMove(@Nullable MovementEvent event) {
        MotionUtils.setMotion(speedy);
    }

    @Override
    public void onEnable() {
        MotionUtils.setMotion(0);
        //if (System.currentTimeMillis() >= lastToggledTime) {
            canBoost = true;
            MotionUtils.damageVerus();
        /*} else {
            canBoost = false;
            if (TimeUnit.MILLISECONDS.toSeconds(lastToggledTime - System.currentTimeMillis()) >= 1)
                Sulfur.getInstance().getNotificationManager().postNotification("You can be boosted in " + TimeUnit.MILLISECONDS.toSeconds(lastToggledTime - System.currentTimeMillis()) + " seconds!", NotificationType.INFO);
        }*/
        speedy = 0.0f;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        boosted = false;
        pearled = false;
        MotionUtils.setMotion(0);
        stopwatch.resetTime();
        gameSpeedStopwatch.resetTime();
        if (System.currentTimeMillis() >= lastToggledTime) lastToggledTime = System.currentTimeMillis() + 15000;
    }

}
