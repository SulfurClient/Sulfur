package gg.sulfur.client.impl.modules.player;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastUse extends Module {

    private final NumberValue packets = new NumberValue("Packets", this, 20, 5, 100, true);
    private final EnumValue<Mode> mode = new EnumValue<>("Mode", this, Mode.values());
    private boolean reset;
    private final Stopwatch stopwatch = new Stopwatch();

    public FastUse(ModuleData moduleData) {
        super(moduleData);
        register(mode, packets);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.isCollidedVertically && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
            switch (mode.getValue()) {
                case GHOSTLY: {
                    if (mc.thePlayer.isUsingItem()) {
                        if (mc.thePlayer.onGround) {
                            if (mc.thePlayer.ticksExisted % 4 == 0) {
                                for (int j = 0; j < 8; j++) {
                                    double d = mc.thePlayer.posX;
                                    double d2 = mc.thePlayer.posY + 1.0E-9;
                                    double d3 = mc.thePlayer.posZ;
                                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(d, d2, d3, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                                }
                            }
                        }
                    }
                    break;
                }
                case VANILLA: {
                    for (int i = 0; i < packets.getValue().intValue(); i++) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    }
                    break;
                }
            }
        } else {
            reset = false;
        }
    }

    private enum Mode implements INameable {
        VANILLA("Vanilla"), GHOSTLY("Ghostly");

        private final String displayName;

        Mode(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }

    }

    @Override
    public void onEnable() {
        reset = false;
        super.onEnable();
    }

    @Override
    public String getSuffix() {
        return " \2477" + mode.getValue().getDisplayName();
    }
}