package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.ColorValue;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.Render3DEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.movement.Speed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;

import java.awt.*;

public class Particles extends Module {

    public Particles(ModuleData moduleData) {
        super(moduleData);
        register(mode);
    }

    private final EnumValue<Particles.Mode> mode = new EnumValue<>("Mode", this, Particles.Mode.values());


    public void onEnable() {

    }

    public void onDisable() {

    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        for (Object o : mc.theWorld.loadedEntityList) {
            if (o instanceof EntityPlayer) {
                EntityPlayer ep = (EntityPlayer) o;
                Particles.Mode mode = this.mode.getValue();
                switch (mode) {
                    case HEART: {
                        mc.theWorld.spawnParticle(EnumParticleTypes.HEART, ep.posX, ep.posY + ep.getEyeHeight() - 1.0D, ep.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
                        break;
                    }
                    case FLAME: {
                        mc.theWorld.spawnParticle(EnumParticleTypes.FLAME, ep.posX, ep.posY + ep.getEyeHeight() - 1.0D, ep.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
                        break;
                    }
                    case SMOKE: {
                        mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ep.posX, ep.posY + ep.getEyeHeight() - 1.0D, ep.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String getSuffix() {
        String mode = this.mode.getValue().getDisplayName();
        return " \2477" + mode;
    }


    public enum Mode implements INameable {
        HEART("Heart"),
        FLAME("Flame"),
        SMOKE("Smoke");
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