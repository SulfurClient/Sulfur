package gg.sulfur.client.impl.modules.render;

import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;

/**
 * @author Kansio
 * @created 10:59 PM
 * @project Client
 */
public class Glow extends Module {

    public Glow(ModuleData moduleData) {
        super(moduleData);
    }

    @Override
    public void onEnable() {
        mc.gameSettings.ofFastRender = false;
    }
}
