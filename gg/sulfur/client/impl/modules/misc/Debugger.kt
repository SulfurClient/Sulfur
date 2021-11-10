package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.property.impl.BooleanValue
import net.minecraft.client.Minecraft
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.events.enums.PacketDirection
import gg.sulfur.client.impl.utils.player.ChatUtil
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C0FPacketConfirmTransaction
import java.text.DecimalFormat

class Debugger(moduleData: ModuleData?) : Module(moduleData) {

    var bps = BooleanValue("BPS", this, false)
    var packets = BooleanValue("Packets", this, false)
    var allpackets = BooleanValue("All Packets", this, true)

    @Subscribe
    fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer == null) {
            return
        }
        if (bps.value && mc.thePlayer.isMoving) {
            val bps = mc.thePlayer.getDistance(
                mc.thePlayer.lastTickPosX,
                mc.thePlayer.lastTickPosY,
                mc.thePlayer.lastTickPosZ
            ) * (Minecraft.getMinecraft().timer.ticksPerSecond * Minecraft.getMinecraft().timer.timerSpeed)
            ChatUtil.displayMessage("Current BPS: " + DecimalFormat("0.##").format(bps))
        }
    }

    @Subscribe
    fun onPacket(event: PacketEvent) {
        if (packets.value) {
            if (allpackets.value) {
                ChatUtil.displayMessage(event.getPacket<Packet>().javaClass.simpleName)
            } else if (event.packetDirection != PacketDirection.INBOUND) {
                if (event.getPacket<Packet>() is C03PacketPlayer || event.getPacket<Packet>() is C0FPacketConfirmTransaction) {
                    return
                }
                ChatUtil.displayMessage(event.getPacket<Packet>().javaClass.simpleName)
            }
        }
    }

    init {
        register(bps, packets, allpackets)
    }
}