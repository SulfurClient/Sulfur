package gg.sulfur.client.impl.modules.movement;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.utils.world.BlockUtils;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

/**
 * @author Kansio
 * @created 7:11 PM
 * @project Client
 */
public class IceSpeed extends Module {

    public IceSpeed(ModuleData moduleData) {
        super(moduleData);
    }

    @Override
    public void onDisable() {
        Blocks.ice.slipperiness = 0.98F;
        Blocks.packed_ice.slipperiness = 0.98F;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        final Material material = BlockUtils.getMaterial(mc.thePlayer.getPosition().offsetDown());
        if(material == Material.ice || material == Material.packedIce) {
            Blocks.ice.slipperiness = 2F;
            Blocks.packed_ice.slipperiness = 2f;
        }
    }

}
