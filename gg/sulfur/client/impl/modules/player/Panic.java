package gg.sulfur.client.impl.modules.player;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.impl.modules.misc.Commands;
import gg.sulfur.client.impl.modules.movement.Sprint;
import gg.sulfur.client.impl.modules.render.Hud;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import org.lwjgl.opengl.Display;

public class Panic extends Module {

    public Panic(ModuleData moduleData) {
        super(moduleData);
    }

    public boolean panic;

    @Override
    public void onEnable() {
        if (!panic) {
            Display.setTitle("Minecraft 1.8.9");
            ChatUtil.displayChatMessage("Disabled everything, enable the module again to unpanic.");
            Sulfur.getInstance().getModuleManager().getObjects().stream().filter(Module::isToggled).forEach(Module::toggle);
            panic = true;
        } else {
            Sulfur.getInstance().getModuleManager().get(Commands.class).toggle();
            Sulfur.getInstance().getModuleManager().get(Sprint.class).toggle();
            Sulfur.getInstance().getModuleManager().get(Hud.class).toggle();
            ChatUtil.displayChatMessage("Enabled some modules, enable the module again to panic.");
            this.toggle();
            panic = false;
        }
    }
}
