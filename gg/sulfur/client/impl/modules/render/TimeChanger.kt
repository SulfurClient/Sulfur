package gg.sulfur.client.impl.modules.render

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.UpdateEvent
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S03PacketTimeUpdate
import net.minecraft.network.play.server.S2BPacketChangeGameState


/**
 * @author Kansio
 * @created 12:59 AM
 * @project Client
 */

class TimeChanger(data: ModuleData) : Module(data) {

    val time = NumberValue("Time", this, 0.0, 0.0,24000.0)

    init {
        register(time)
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        mc.theWorld.worldTime = time.value.toLong()
    }

    @Subscribe
    fun onPacket(event: PacketEvent) {
        val packet = event.getPacket<Packet>()

        if (packet is S03PacketTimeUpdate) {
            event.isCancelled = true
        }

        if (packet is S2BPacketChangeGameState) {
            event.isCancelled = true
        }
    }

}