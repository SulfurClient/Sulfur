package gg.sulfur.client.impl.modules.misc

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.utils.player.ChatUtil


/**
 * @author Kansio
 * @created 6:55 PM
 * @project Client
 */

class AutoVclip(data: ModuleData) : Module(data) {

    //nchBbkeSLqlJC5Qj

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        if (mc.theWorld.getBlock(mc.thePlayer.posX.toInt(), (mc.thePlayer.posY - 1).toInt(), mc.thePlayer.posZ.toInt()).unlocalizedName.lowercase().contains("glass")) {
            mc.thePlayer.setPosition(
                mc.thePlayer.posX,
                mc.thePlayer.posY - 5,
                mc.thePlayer.posZ
            )
        }
    }

}