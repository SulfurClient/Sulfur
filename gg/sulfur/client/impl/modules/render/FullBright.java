package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.impl.events.UpdateEvent;

/**
 * @author Auth
 */

// moved from memeware

public class FullBright extends Module {

    private float lastBrightness;

    public FullBright(ModuleData moduleData) {
        super(moduleData);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        mc.gameSettings.gammaSetting = 999L;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        lastBrightness = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 999L;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = lastBrightness;
    }
}