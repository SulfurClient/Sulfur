package gg.sulfur.client.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
/*import javafx.scene.transform.Rotate;*/
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.render.Rotations;
import gg.sulfur.client.impl.utils.combat.AimUtil;
import gg.sulfur.client.impl.utils.combat.FightUtil;
import gg.sulfur.client.impl.utils.combat.extras.Rotation;
import gg.sulfur.client.impl.utils.inventory.InventoryUtils;
import gg.sulfur.client.impl.utils.networking.PacketUtil;

import java.util.List;

public class FastBow extends Module {

    private final EnumValue<Mode> mode = new EnumValue<>("Mode", this, Mode.values());
    private final NumberValue packets = new NumberValue("Packets", this, 20, 5, 100, true);
    private final NumberValue range = new NumberValue("Range", this, 25, 1, 100);
    private final BooleanValue friends = new BooleanValue("Friends", this, false);
    private final BooleanValue teams = new BooleanValue("Teams", this, false);

    public FastBow(ModuleData moduleData) {
        super(moduleData);
        register(mode, packets, range, friends, teams);
    }

    @Subscribe
    public void onPacket2(PacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook playerPosLook = event.getPacket();
            event.setCancelled(mc.thePlayer.ticksExisted > 55 && Math.abs(playerPosLook.getX() - mc.thePlayer.posX) < 9 &&
                    Math.abs(playerPosLook.getY() - mc.thePlayer.posY) < 9 &&
                    Math.abs(playerPosLook.getZ() - mc.thePlayer.posZ) < 9);
        }
    }

    @Subscribe
    public void onMotionUpdate(UpdateEvent event) {
        if (event.isPre()) return;

        mc.timer.timerSpeed = 1.0F;
        final ItemStack heldItem = mc.thePlayer.getHeldItem();

        if (!mc.gameSettings.keyBindUseItem.getIsKeyPressed() || heldItem == null || !(heldItem.getItem() instanceof ItemBow) || (mode.getValue().equals(Mode.VERUS) && !mc.thePlayer.onGround)) return;

        List<EntityLivingBase> targets = FightUtil.getMultipleTargets(range.getValue(), true, false, false, false, true);

        if (!friends.getValue()) {
            targets.removeIf(e -> Sulfur.getInstance().getFriendManager().getObjects().contains(e.getName().toLowerCase()));
        }

        if (teams.getValue()) {
            targets.removeIf(FightUtil::isOnSameTeam);
        }

        switch (mode.getValue()) {
            case VANILLA:
                targets.forEach(entityLivingBase -> {
                    mc.thePlayer.getHeldItem().setItemDamage(0);

                    PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                    final Rotation rotation = AimUtil.getBowAngles(entityLivingBase);

                    if (Sulfur.getInstance().getModuleManager().get(Rotations.class).isToggled()) {
                        mc.thePlayer.renderPitchHead = rotation.getRotationPitch();
                        mc.thePlayer.renderYawOffset = rotation.getRotationYaw();
                        mc.thePlayer.renderYawHead = rotation.getRotationYaw();
                    }

                    for (int i = 0; i < packets.getCastedValue().intValue(); ++i) {
                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, rotation.getRotationYaw(), rotation.getRotationPitch(), true));
                    }

                    PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                });
                break;
            case VERUS:
                mc.thePlayer.getHeldItem().setItemDamage(0);
                for (double d = -1; d < 0; d += 0.5) {
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, true));
                }
                for (EntityLivingBase entityLivingBase : targets) {
                    PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));

                    for (int i = 0; i < (packets.getCastedValue().intValue()); i++) {
                        final Rotation rotation = AimUtil.getRotationsRandom(entityLivingBase);

                        if (Sulfur.getInstance().getModuleManager().get(Rotations.class).isToggled()) {
                            mc.thePlayer.renderPitchHead = rotation.getRotationPitch();
                            mc.thePlayer.renderYawOffset = rotation.getRotationYaw();
                            mc.thePlayer.renderYawHead = rotation.getRotationYaw();
                        }

                        PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, rotation.getRotationYaw(), rotation.getRotationPitch(), true));
                    }
                    InventoryUtils.swap(9, mc.thePlayer.inventory.currentItem);
                    InventoryUtils.swap(9, mc.thePlayer.inventory.currentItem);

                    PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                break;
            case GHOSTLY: {
                if (mc.thePlayer.onGround && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
                    if (mc.thePlayer.ticksExisted % 4 == 0) {
                        double d = mc.thePlayer.posX;
                        double d2 = mc.thePlayer.posY + 1.0E-9;
                        double d3 = mc.thePlayer.posZ;
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

                        for (int i = 0; i < 20; i++) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(d, d2, d3, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                        }
                        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                        mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(mc.thePlayer.inventory.getCurrentItem(), Minecraft.getMinecraft().theWorld, mc.thePlayer, 10);
                    }
                }
                break;
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mode.getValue().equals(Mode.VERUS) && event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packetPlayerPosLook = event.getPacket();
            event.forceCancel(Math.abs(packetPlayerPosLook.getX() - mc.thePlayer.posX) < 9 && Math.abs(packetPlayerPosLook.getY() - mc.thePlayer.posY) < 9 && Math.abs(packetPlayerPosLook.getZ() - mc.thePlayer.posZ) < 9);
        }
    }

    enum Mode implements INameable {
        VANILLA("Vanilla"), VERUS("Verus"), GHOSTLY("Ghostly");

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
