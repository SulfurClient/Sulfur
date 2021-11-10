package gg.sulfur.client.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import net.minecraft.network.play.client.C0BPacketEntityAction;

/**
 * @author Auth
 */

public class Sprint extends Module {

    private final BooleanValue omni = new BooleanValue("Omni", this, false);

    public Sprint(ModuleData moduleData) {
        super(moduleData);
        register(omni);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isSneaking()) return;

        if (mc.thePlayer.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction entityAction = event.getPacket();
            if (entityAction.func_180764_b() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                //event.setCancelled(true);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}