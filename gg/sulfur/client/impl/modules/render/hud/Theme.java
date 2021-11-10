package gg.sulfur.client.impl.modules.render.hud;

import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import net.minecraft.client.Minecraft;

public abstract class Theme {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    private final Module module;

    public Theme(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public abstract void render(RenderHUDEvent event);
}
