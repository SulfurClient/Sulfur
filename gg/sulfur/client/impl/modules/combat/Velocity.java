package gg.sulfur.client.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.impl.events.PacketEvent;

public class Velocity extends Module {

    private final EnumValue<Mode> enumValue = new EnumValue<>("Mode", this, Velocity.Mode.values());

    public Velocity(ModuleData moduleData) {
        super(moduleData);
        register(enumValue);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        switch (enumValue.getValue()) {
            case PACKET:
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity packetEntityVelocity = event.getPacket();
                    event.forceCancel(mc.theWorld != null && mc.theWorld.getEntityByID(packetEntityVelocity.func_149412_c()) == mc.thePlayer);
                } else if (event.getPacket() instanceof S27PacketExplosion) {
                    event.setCancelled(true);
                }
                break;

            case PULL:
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity packetEntityVelocity = event.getPacket();
                    if (mc.theWorld != null && mc.theWorld.getEntityByID(packetEntityVelocity.func_149412_c()) == mc.thePlayer) {
                        packetEntityVelocity.field_149415_b = -packetEntityVelocity.field_149415_b;
                        packetEntityVelocity.field_149414_d = -packetEntityVelocity.field_149414_d;
                    }
                } else if (event.getPacket() instanceof S27PacketExplosion) {
                    final S27PacketExplosion packetExplosion = event.getPacket();
                    packetExplosion.field_149152_f = -packetExplosion.field_149152_f;
                    packetExplosion.field_149159_h = -packetExplosion.field_149159_h;
                }
                break;

            case GROUND:
                if (mc.thePlayer.hurtTime >= 5 && !mc.thePlayer.onGround) {
                    mc.thePlayer.onGround = true;
                }
                break;

            case FLOAT:
                if (mc.thePlayer.hurtTime >= 5 && !mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.0F;
                }
                break;
            case INVADED:
                if (mc.thePlayer.hurtTime != 0) {
                    mc.thePlayer.motionX *= .65;
                    mc.thePlayer.motionZ *= .65;
                }
                break;
        }
    }

    @Override
    public String getSuffix() {
        return " \2477" + enumValue.getValue().getDisplayName();
    }

    public enum Mode implements INameable {
        PACKET("Packet"),
        PULL("Pull"),
        FLOAT("Float"),
        INVADED("Invaded"),
        GROUND("Ground");
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