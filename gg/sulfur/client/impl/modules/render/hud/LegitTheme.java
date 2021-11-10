package gg.sulfur.client.impl.modules.render.hud;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import gg.sulfur.client.impl.modules.movement.Sprint;
import gg.sulfur.client.impl.modules.render.Hud;

public class LegitTheme extends Theme {

    public LegitTheme(Module module) {
        super(module);
    }

    @Override
    public void render(RenderHUDEvent event) {
        Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);
        if (Sulfur.getInstance().getModuleManager().get(Sprint.class).isToggled() && Sulfur.getInstance().getModuleManager().get(Sprint.class).isToggled()) {
            mc.fontRendererObj.drawStringWithShadow("[Sprinting (Toggled)]", 4, 4, hud.color.getValue().getRGB());
        }
        mc.fontRendererObj.drawStringWithShadow("[" + mc.debugFPS + " FPS]" , 4, 16, hud.color.getValue().getRGB());
    }
}
