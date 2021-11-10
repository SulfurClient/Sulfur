package gg.sulfur.client.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.SliderUnit;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.BlockCollisionEvent;
import gg.sulfur.client.impl.events.MovementEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.events.enums.PacketDirection;
import gg.sulfur.client.impl.notification.type.NotificationType;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LongJump extends Module {

    private NumberValue speed = new NumberValue("Speed", this, 5.0, 3.0, 15, SliderUnit.BPT);
    private EnumValue<Modes> mode = new EnumValue<>("Modes", this, Modes.values());
    private BooleanValue doShoot = new BooleanValue("Shoot Automatically", this, true);
    private ArrayList<Packet> packets = new ArrayList<>();
    private ArrayList<Vector3d> locations = new ArrayList<>();
    private Vector3d startVector;
    private Stopwatch testStopwatch = new Stopwatch();
    private boolean shooting;

    long lastBoosted;
    boolean canBoost;
    boolean boosted;

    boolean funnying = false;

    double speedy;

    public LongJump(ModuleData moduleData) {
        super(moduleData);
        register(speed, mode, doShoot);
    }

    public void onEnable() {
        testStopwatch.resetTime();
        if (mode.getValue().equals(Modes.TEST)) {
            if (getArrowCount() == 0 && doShoot.getValue() && mode.getValue() == Modes.TEST) {
                ChatUtil.displayChatMessage("You do not have an arrow in your inventory. Arrows are required to use this mode.");
                this.toggle();
                return;
            }
        }

        if (mode.getValue() == Modes.VERUS && mc.thePlayer.onGround) {
            mc.timer.timerSpeed = 0.3f;
            MotionUtils.setMotion(0);
            if (mode.getValue() == Modes.VERUS) {
                canBoost = true;
                if (mc.thePlayer.onGround) {
                    MotionUtils.damageVerus();
                    funnying = true;
                } else {
                    toggle();
                }
            }
        }
    }

    public void onDisable() {
        //if (System.currentTimeMillis() >= lastBoosted && boosted) lastBoosted = System.currentTimeMillis() + 15000;
        testStopwatch.resetTime();
        MotionUtils.setMotion(0);
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) mc.gameSettings.keyBindForward.pressed = true;
        boosted = false;
    }

    @Subscribe
    public void onCollide(BlockCollisionEvent event) {
        switch (mode.getValue()) {
            case DAMAGE: {
                break;
            }
        }
    }

    @Subscribe
    public void onMove(MovementEvent event) {
        switch (mode.getValue()) {
            case VANILLA: {
                if (mc.thePlayer.onGround) {
                    event.setMotionY(mc.thePlayer.motionY = 0.4);
                }

                MotionUtils.setMotion(event, speed.getValue());
                break;
            }
            case TEST: {

                if (mc.thePlayer.isMoving()) {
                    MotionUtils.setMotion(event, speed.getValue());
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }
                }
                break;
            }
            case DAMAGE:
            case VERUS: {
                if (speedy != -1 && !mc.thePlayer.onGround) MotionUtils.setMotion(event, speedy);
                break;
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValue()) {
            case VERUS: {
                if (mc.thePlayer.hurtTime >= 1 && canBoost) {
                    if (funnying) {
                        mc.thePlayer.motionY = 0.8f;
                        funnying = false;
                    }
                    if (!boosted) {
                        speedy = speed.getValue();
                        boosted = true;
                    }
                } else {
                    speedy = -1f;
                }
                break;
            }
            case GHOSTLY: {
                double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                double x = -Math.sin(yaw) * 2.8;
                double z = Math.cos(yaw) * 2.8;

                if (!mc.thePlayer.isMoving()) return;

                if (mc.thePlayer.ticksExisted % 5 == 0) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                } else {
                    MotionUtils.setMotion(0);
                }
                break;
            }
            case DAMAGE: {
                if (mc.thePlayer.hurtTime >= 1) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        speedy = speed.getValue();
                    }
                } else {
                    speedy = -1f;
                }
                break;
            }
            case TEST: {
                if (mc.thePlayer.hurtTime >= 1) {
                    for (Packet packet : packets) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
                    }
                    packets.clear();
                    toggle();
                }
                break;
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.thePlayer == null) {
            return;
        }
        if (mode.getValue() == Modes.TEST) {
            if (event.getPacketDirection() != PacketDirection.OUTBOUND) {
                event.setCancelled(true);
                packets.add(event.getPacket());
            }
        }
    }

    private int getArrowCount() {
        int arrowCount = 0;
        for (int i = 0; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();

                if ((is.getItem() == Items.arrow)) {
                    arrowCount += is.stackSize;
                }
            }
        }
        return arrowCount;
    }

    public enum Modes implements INameable {
        VANILLA("Vanilla"), TEST("MMC"), VERUS("Verus"), DAMAGE("On-Damage"), GHOSTLY("Ghostly");

        private final String name;

        Modes(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    private boolean hasMoved() {
        return mc.thePlayer.posX != mc.thePlayer.prevPosX || mc.thePlayer.posY != mc.thePlayer.prevPosY || mc.thePlayer.posZ != mc.thePlayer.prevPosZ;
    }
}
