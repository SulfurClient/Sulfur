package gg.sulfur.client.impl.modules.player;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.notification.type.NotificationType;
import gg.sulfur.client.impl.utils.inventory.InventoryUtils;
import gg.sulfur.client.impl.utils.networking.PacketUtil;
import gg.sulfur.client.impl.utils.pathfinding.DortPathFinder;
import gg.sulfur.client.impl.utils.pathfinding.Vec3;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.util.ArrayList;

public class ClickTP extends Module {

    private EnumValue<Mode> modes = new EnumValue<>("Modes", this, Mode.values());
    boolean shooting = false;
    private Stopwatch stopwatch = new Stopwatch();
    private boolean teleported = false;

    public ClickTP(ModuleData moduleData) {
        super(moduleData);
        register(modes);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (modes.getValue()) {
            case LUNAR: {
                if (mc.thePlayer.ticksExisted % 20 == 0) {
                    if (Mouse.isButtonDown(1)) {
                        MovingObjectPosition rayTrace = mc.thePlayer.rayTrace(200, mc.timer.renderPartialTicks);
                        BlockPos blockPos = rayTrace.func_178782_a();
                        if (blockPos == null) {
                            return;
                        }

                        ArrayList<Vec3> path;
                        path = DortPathFinder.computePath(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5));

                        int i = 0;
                        for (Vec3 vector : path) {
                            i++;
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vector.getX(), vector.getY(), vector.getZ(), true));
                            mc.thePlayer.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                        }
                    }
                }
                break;
            }
            case MMC: {

                if (Mouse.isButtonDown(1)) {
                    if (!shooting) {
                        if (!doShoot()) {
                            Sulfur.getInstance().getNotificationManager().postNotification("failed", NotificationType.INFO);
                            this.toggle();
                        } else {
                            Sulfur.getInstance().getNotificationManager().postNotification("!failed", NotificationType.INFO);
                        }
                    }
                }

                if (mc.thePlayer.ticksExisted % 20 == 0) {
                    if (Mouse.isButtonDown(1)) {
                        MovingObjectPosition rayTrace = mc.thePlayer.rayTrace(200, mc.timer.renderPartialTicks);
                        BlockPos blockPos = rayTrace.func_178782_a();
                        if (blockPos == null) {
                            return;
                        }

                        ArrayList<Vec3> path;
                        path = DortPathFinder.computePath(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5));


                        if (mc.thePlayer.hurtResistantTime > 1) {
                            int i = 0;
                            for (Vec3 vector : path) {
                                i++;
                                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vector.getX(), vector.getY(), vector.getZ(), true));
                                mc.thePlayer.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                            }
                            ChatUtil.displayChatMessage(String.format("Teleported to %s, %s, %s in %s teleports.", blockPos.getX(), blockPos.getY(), blockPos.getZ(), path.size()));
                            Sulfur.getInstance().getNotificationManager().postNotification("Disabling for now", NotificationType.INFO);
                            toggle();
                        }
                    }
                }
                break;
            }
            case MATRIX: {
                if (!mc.thePlayer.isInWeb) return;

                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + r(-0.1, 0.1), mc.thePlayer.posY, mc.thePlayer.posZ + r(-0.1, 0.1), true));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ, true));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 100, mc.thePlayer.posZ, true));
                break;
            }
        }

    }

    public void onEnable() {
        if (modes.getValue() == Mode.MMC) {
            ChatUtil.displayChatMessage("This mode is in development atm");
            if (getArrowCount() == 0) {
                ChatUtil.displayChatMessage("Get an arrow atleast");
                this.toggle();
                return;
            }
        }
    }

    public void onDisable() {
        shooting = false;
    }

    private double r(double min, double max) {
        return Math.floor(Math.random()*(max-min+1)+min);
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

    // I'm reusing code here
    private boolean doShoot() {
        //ChatUtl.displayChatMessage("called");
        boolean threw = false;
        shooting = true;

        if (getArrowCount() == 0) {
            return false;
        }

        for (short i = 0; i < 45; i++) {

            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();

                // how the fuck do i do this me confused
                // FIXME: Retard can't code
                if (is.getItem() instanceof ItemBow) {
                    threw = true;

                    int heldItemBeforeThrow = mc.thePlayer.inventory.currentItem;
                    if (i - 36 < 0) {

                        InventoryUtils.swap(i, 8);


                        mc.thePlayer.motionX = 0.0;
                        PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(8));


                    }else {

                        PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(i - 36));

                    }

                    Vector2f rotation = new Vector2f();
                    rotation.x = mc.thePlayer.rotationYaw;
                    rotation.y = -90;
                    mc.thePlayer.renderPitchHead = rotation.y;
                    mc.thePlayer.renderYawOffset = rotation.x;
                    mc.thePlayer.renderYawHead = rotation.x;

                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

                    for (int J = 0; J < 5; J++) {
                        //ChatUtil.displayChatMessage("called " +  J  + " times");
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, -90, true));
                    }
                    Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));

                    mc.thePlayer.inventory.currentItem = heldItemBeforeThrow;
                    PacketUtil.sendPacketNoEvent(new C09PacketHeldItemChange(heldItemBeforeThrow));

                    return true;

                }

            }

        }

        return false;
    }

    public enum Mode implements INameable {
        LUNAR("Lunar"), MATRIX("Matrix"), MMC("MMC");
        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}
