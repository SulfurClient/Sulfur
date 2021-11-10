package gg.sulfur.client.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.utils.networking.PacketUtil;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author Auth
 */

public class NoSlow extends Module {

    private final EnumValue<Mode> enumValue = new EnumValue<>("Mode", this, NoSlow.Mode.values());

    public NoSlow(ModuleData moduleData) {
        super(moduleData);
        register(enumValue);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (!mc.thePlayer.isBlocking()) return;

        switch (enumValue.getValue()) {
            case NCP: {
                if (event.isPre()) {
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
                } else {
                    PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                }
                break;
            }
            case GHOSTLY: {
                PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                break;
            }
        }
    }

    public String getSuffix() {
        Mode mode = enumValue.getValue();
        return " \2477" + mode.getDisplayName();
    }

    public enum Mode implements INameable {
        VANILLA("Vanilla"), NCP("NCP"), GHOSTLY("Ghostly");
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
