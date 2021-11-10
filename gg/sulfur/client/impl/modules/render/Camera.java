package gg.sulfur.client.impl.modules.render;

import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;

public class Camera extends Module {

    public final BooleanValue noScoreboard = new BooleanValue("No Scoreboard", this, true);
    public final BooleanValue clearChat = new BooleanValue("Clear Chat", this, false);
    public final BooleanValue smoothChat = new BooleanValue("Smooth Chat", this, false);
    public final BooleanValue noHurtCam = new BooleanValue("No Hurt Cam", this, true);
    public final BooleanValue witherHearts = new BooleanValue("Black Hearts", this, false);

    public Camera(ModuleData moduleData) {
        super(moduleData);
        register(noScoreboard, clearChat, smoothChat, noHurtCam, witherHearts);
    }
}
