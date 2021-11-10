package gg.sulfur.client.impl.modules.misc;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.BlockCollisionEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class NoFall extends Module {

    //Spoofs ground to prevent flags
    private boolean verusFunny;
    private boolean hasFallen, waitFlag;
    private float flagCheck;

    private final EnumValue<Mode> enumValue = new EnumValue<>("Mode", this, NoFall.Mode.values());

    public NoFall(ModuleData moduleData) {
        super(moduleData);
        register(enumValue);
    }

    @Subscribe
    public void onCollide(BlockCollisionEvent event) {
        if (enumValue.getValue() == Mode.VERUS && verusFunny && mc.thePlayer.ticksExisted % 26 == 0 && mc.thePlayer.fallDistance > 3) {
            if (event.getBlock() instanceof BlockAir) {
                double x = event.getX();
                double y = event.getY();
                double z = event.getZ();
                if (y < mc.thePlayer.posY) {
                    event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-5, -1, -5, 5, 1.0F, 5).offset(x, y, z));
                }
            }

        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() && mc.thePlayer.fallDistance > 2F) {
            switch (enumValue.getValue()) {
                case EDIT: {
                    event.setOnGround(true);
                }
                break;
                case HYPIXEL: {
                    event.setOnGround(mc.thePlayer.ticksExisted % 2 == 0);
                    break;
                }
                case OFFGROUND: {
                    event.setOnGround(false);
                    break;
                }
                case VERUS: {
                    if (mc.thePlayer.fallDistance > 1.25F || mc.thePlayer.ticksExisted % 26 != 0) {
                        mc.thePlayer.motionY = 0;
                        verusFunny = true;
                        event.setOnGround(true);
                        mc.thePlayer.fallDistance = 0;
                    } else {
                        verusFunny = false;
                    }
                    break;
                }
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (enumValue.getValue()) {
            case OFFGROUND: {
                if (event.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packetPlayer = event.getPacket();
                    packetPlayer.onGround = false;
                } else if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                    C03PacketPlayer.C04PacketPlayerPosition packetPlayer = event.getPacket();
                    packetPlayer.onGround = false;
                } else if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
                    C03PacketPlayer.C05PacketPlayerLook packetPlayer = event.getPacket();
                    packetPlayer.onGround = false;
                } else if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                    C03PacketPlayer.C06PacketPlayerPosLook packetPlayer = event.getPacket();
                    packetPlayer.onGround = false;
                }
                break;
            }
        }
    }

    public String getSuffix() {
        return " \2477" + enumValue.getValue().getDisplayName();
    }

    public enum Mode implements INameable {
        EDIT("Edit"), HYPIXEL("Ghostly"), VERUS("Verus"), MATRIX("Matrix"), OFFGROUND("OffGround");
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