package gg.sulfur.client.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.SliderUnit;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.EntityStepEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.exploit.Phase;
import gg.sulfur.client.impl.utils.TimeUtils;
import gg.sulfur.client.impl.utils.networking.PacketUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step extends Module {

    private final EnumValue<Mode> enumValue = new EnumValue<>("Mode", this, Step.Mode.values());
    private final NumberValue height = new NumberValue("Height", this, 1, 1, 10, SliderUnit.BLOCKS);
    private final NumberValue timer = new NumberValue("Timer", this, 0.5, 0.1, 2, false);
    private final NumberValue delay = new NumberValue("Timer Duration", this, 150, 25, 500, SliderUnit.MS, true);

    public Step(ModuleData moduleData) {
        super(moduleData);
        register(enumValue, height, timer, delay);
    }

    @Subscribe
    public void onEntityStep(EntityStepEvent event) {
        if (event.getEntity() instanceof EntityPlayerSP) {
            Mode mode = enumValue.getValue();
            if (mode == Mode.NCP) {
                if (mc.thePlayer.onGround && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && !mc.thePlayer.isOnLadder() && !Sulfur.getInstance().getModuleManager().get(Phase.class).isToggled()) {
                    if (event.isPre()) {
                        event.setHeight(height.getCastedValue().floatValue());
                    } else {
                        float[] stepValues = new float[]{};
                        final float realHeight = event.getHeight();

                        if (realHeight > .9F) {
                            stepValues = new float[]{0.42F, 0.7532F};
                        } else if (realHeight > 0.5f) {
                            stepValues = new float[]{0.42F, 0.7532F};
                        }
                        for (float value : stepValues) {
                            PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ, false));
                        }

                        if (realHeight > 0.5f) {
                            TimeUtils.sleepTimer(mc.timer, timer.getValue().floatValue(), delay.getValue().longValue());
                        }
                    }
                }
            } else if (!event.isPre()) {
                final float realHeight = event.getHeight();
                if (realHeight > 0.5f) {
                    TimeUtils.sleepTimer(mc.timer, timer.getValue().floatValue(), delay.getValue().longValue());
                }
            }

        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (enumValue.getValue().equals(Mode.VANILLA)) {
            if (event.isPre() && !Sulfur.getInstance().getModuleManager().get(Phase.class).isToggled())
                mc.thePlayer.stepHeight = height.getCastedValue().floatValue();
            if (Sulfur.getInstance().getModuleManager().get(Phase.class).isToggled()) mc.thePlayer.stepHeight = 0.6F;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.thePlayer.stepHeight = 0.6F;
    }

    public enum Mode implements INameable {
        VANILLA("Vanilla"), NCP("NCP");
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