package gg.sulfur.client.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.BlockCollisionEvent;
import gg.sulfur.client.impl.events.JumpEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.utils.movement.MotionUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Jesus extends Module {

    private final EnumValue<Mode> mode = new EnumValue<>("Mode", this, Mode.values());

    private boolean wasWater;
    private boolean boosted;

    public Jesus(ModuleData moduleData) {
        super(moduleData);
        register(mode);
    }

    @Subscribe
    public void onCollide(BlockCollisionEvent event) {
        switch (mode.getValue()) {
            case VERUS:
            case COLLIDE: {
                if (event.getBlock() instanceof BlockLiquid) {
                    if (mc.thePlayer.isSneaking()) return;
                    double x = event.getX();
                    double y = event.getY();
                    double z = event.getZ();
                    if (y < mc.thePlayer.posY) {
                        event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 0.9999, 15).offset(x, y, z));
                    }
                }
                break;
            }
            case DOLPHIN: {
                if (!(event.getBlock() instanceof BlockLiquid) || isInLiquid() || mc.thePlayer.isSneaking() || (mc.thePlayer.isBurning() && isOnWater())) {
                    return;
                }
                event.setAxisAlignedBB(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).contract(0.0, 1.0E-6, 0.0).offset(event.getX(), event.getY(), event.getZ()));
                break;
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValue()) {
            case MATRIX: {
                if (mc.thePlayer.isInWater()) mc.thePlayer.jump();
                break;
            }
            case SHOTBOW: {
                if (mc.thePlayer.onGround && !mc.thePlayer.isInWater()) {
                    wasWater = false;
                    boosted = false;
                }

                if (mc.thePlayer.isInWater()) {
                    if (mc.thePlayer.isCollidedHorizontally) {
                        return;
                    }
                    if (!boosted) {
                        mc.thePlayer.jump();
                    }
                    wasWater = true;
                }

                if (wasWater && !mc.thePlayer.isInWater()) {
                    boosted = true;
                }

                //do bouncy
                if (mc.thePlayer.isInWater() && wasWater && boosted) {
                    if (mc.thePlayer.ticksExisted % 4 == 0)
                        mc.thePlayer.motionY = 0.05;
                    else
                        mc.thePlayer.motionY = 0;

                }
                break;
            }
            case VERUS:
            case DOLPHIN: {
                if (event.isPre()) {
                    if (mc.thePlayer.isSneaking()) {
                        return;
                    }
                    if (mc.thePlayer.onGround || mc.thePlayer.isOnLadder()) {
                        wasWater = false;
                    }
                    if (wasWater && mc.thePlayer.motionY > 0.0) {
                        if (mc.thePlayer.motionY < 0.03) {
                            mc.thePlayer.motionY *= 1.2;
                            mc.thePlayer.motionY += 0.067;
                        } else if (mc.thePlayer.motionY < 0.05) {
                            mc.thePlayer.motionY *= 1.2;
                            mc.thePlayer.motionY += 0.06;
                        } else if (mc.thePlayer.motionY < 0.07) {
                            mc.thePlayer.motionY *= 1.2;
                            mc.thePlayer.motionY += 0.057;
                        } else if (mc.thePlayer.motionY < 0.11) {
                            mc.thePlayer.motionY *= 1.2;
                            mc.thePlayer.motionY += 0.0534;
                        } else {
                            mc.thePlayer.motionY += 0.0517;
                        }
                    }
                    if (wasWater && mc.thePlayer.motionY < 0.0 && mc.thePlayer.motionY > -0.3) {
                        mc.thePlayer.motionY += 0.04;
                    }
                    if (!isOnLiquid()) {
                        return;
                    }
                    if (MathHelper.ceiling_double_int(mc.thePlayer.posY) != mc.thePlayer.posY + 1.0E-6) {
                        return;
                    }
                    mc.thePlayer.motionY = 0.5;
                    if (mc.thePlayer.fallDistance > 0.12) {
                        MotionUtils.setMotion(MotionUtils.getSpeed() * 1.35);
                        if (mode.getValue() != Mode.VERUS)
                            event.setOnGround(false);
                    } else {
                        MotionUtils.setMotion(0.0);
                        if (mode.getValue() != Mode.VERUS)
                            event.setOnGround(true);
                    }
                    wasWater = true;
                }
                break;
            }
        }
    }

    @Subscribe
    public void onJump(JumpEvent event) {
        event.forceCancel(MotionUtils.isOnWater());
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (mode.getValue()) {
            case COLLIDE: {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packetPlayer = event.getPacket();
                    if (MotionUtils.isOnWater() && mc.thePlayer.ticksExisted % 2 == 0) {
                        packetPlayer.y += 0.01D;
                        packetPlayer.onGround = false;
                    }
                }
                break;
            }
        }
    }

    private boolean isOnLiquid() {
        final double y = mc.thePlayer.posY - 0.015625;
        for (int x = MathHelper.floor_double(mc.thePlayer.posX); x < MathHelper.ceiling_double_int(mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(mc.thePlayer.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOnWater() {
        final double y = mc.thePlayer.posY - 0.03;
        for (int x = MathHelper.floor_double(mc.thePlayer.posX); x < MathHelper.ceiling_double_int(mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(mc.thePlayer.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor_double(y), z);
                if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid && mc.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.water) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInLiquid() {
        final double y = mc.thePlayer.posY + 0.01;
        for (int x = MathHelper.floor_double(mc.thePlayer.posX); x < MathHelper.ceiling_double_int(mc.thePlayer.posX); ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.posZ); z < MathHelper.ceiling_double_int(mc.thePlayer.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, (int) y, z);
                if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    public enum Mode implements INameable {
        COLLIDE("Collide"),
        VERUS("Verus"),
        SHOTBOW("Shotbow"),
        MATRIX("Matrix"),
        DOLPHIN("Dolphin");
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
