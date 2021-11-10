package gg.sulfur.client.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.impl.events.PacketEvent;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

/**
 * @author Kansio
 * @created 2:33 PM
 * @project Client
 */
public class AntiBot extends Module {

    private ArrayList<Entity> bots = new ArrayList<>();

    public AntiBot(ModuleData moduleData) {
        super(moduleData);
    }

    @Override
    public void onEnable() {
        bots.clear();
        super.onEnable();
    }

    @Subscribe
    public void onPacket(PacketEvent event) {

    }

    public ArrayList<Entity> getBots() {
        return bots;
    }
}
