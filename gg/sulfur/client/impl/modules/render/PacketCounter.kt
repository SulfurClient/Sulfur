package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe
import com.sun.org.apache.xpath.internal.operations.Bool
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.RenderHUDEvent
import gg.sulfur.client.impl.events.enums.PacketDirection
import gg.sulfur.client.impl.utils.networking.ServerUtils
import net.minecraft.client.gui.Gui
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S02PacketChat
import java.awt.Color


/**
 * @author Kansio
 * @created 4:40.0 PM
 * @project Client
 */

class PacketCounter(moduleData: ModuleData) : Module(moduleData) {


    val `in`: Map<Long, Packet> = HashMap()
    val out: Map<Long, Packet> = HashMap()
    var clientPacket = ""
    var serverPacket = ""

    val displayPackets = BooleanValue("Display Packets", false, this)
    val noC03 = BooleanValue("Show Player Packet", false, this)
    val showCancelled = BooleanValue("Show Cancelled", true, this)

    init {
        register(displayPackets, noC03, showCancelled)
    }

    @Subscribe
    fun updatePacketCounter(event: PacketEvent) {
        if (event.packetDirection == PacketDirection.INBOUND) {
            if (noC03.value && event.packetClass.simpleName.contains("PacketEntity"))
                return

            if (event.packetClass.simpleName.startsWith("C"))
                return

            serverPacket = event.packetClass.simpleName
        } else {
            if (noC03.value && event.packetClass.simpleName.contains("PacketPlayer"))
                return

            if (!showCancelled.value && event.isCancelled)
                return

            clientPacket = event.packetClass.simpleName

            if (showCancelled.value && event.isCancelled) {
                clientPacket = event.packetClass.simpleName + " §7(Cancelled)"
            }
        }
        try {
            val packetMap: MutableMap<Long, Packet> =
                if (event.packetDirection == PacketDirection.OUTBOUND) out as MutableMap<Long, Packet> else `in` as MutableMap<Long, Packet>
            packetMap[System.currentTimeMillis()] = event.getPacket()
            packetMap.forEach { (key: Long, packet: Packet?) ->
                if (System.currentTimeMillis() - 1000L >= key) {
                    packetMap.remove(key)
                }
            }
        } catch (ignore: Exception) {
        }
    }

    @Subscribe
    fun onRender(event: RenderHUDEvent) {
        val hud = Sulfur.getInstance().moduleManager.get<Hud>(Hud::class.java)
        val spotify = Sulfur.getInstance().moduleManager.get<Spotify>(Spotify::class.java)
        val sessionInformation =
            Sulfur.getInstance().moduleManager.get<SessionInformation>(SessionInformation::class.java)

        var startY = 30

        if (sessionInformation.isToggled) {
            startY += 100
        }

        if (spotify.isToggled) {
            startY += 100
        }

        var y = startY

        Gui.drawRect(5.0, startY + 5.0, 156.0, if (displayPackets.value) y + 88.0 else y + 68.0, Color(0, 0, 0, 105).rgb)
        Gui.drawRect(5.0, startY + 5.0, 156.0, startY + 6.0, hud.color.value.rgb)
        Gui.drawRect(9.0, startY + 15 + 9.0, 152.0, startY + 15 + 8.0, Color(67, 67, 67).rgb)


        mc.fontRendererObj.drawString("Packet Counter: ", 8.0.toFloat(), (startY + 10.0).toFloat(), -1)
        mc.fontRendererObj.drawString("Inbound: ${`in`.size}", 8.0.toFloat(), (startY + 32.0).toFloat(), -1)
        mc.fontRendererObj.drawString("Outbound: ${out.size}", 8.0.toFloat(), (startY + 32.0 + 10).toFloat(), -1)
        mc.fontRendererObj.drawString(
            "Total: ${`in`.size + out.size}",
            8.0.toFloat(),
            (startY + 32.0 + 10 + 10).toFloat(),
            -1
        )
        if (displayPackets.value) {
            mc.fontRendererObj.drawString(
                "Latest Incoming: $serverPacket",
                8.0.toFloat(),
                (startY + 32.0 + 10 + 10 + 10).toFloat(),
                -1
            )
            mc.fontRendererObj.drawString(
                "Latest Outgoing: $clientPacket",
                8.0.toFloat(),
                (startY + 32.0 + 10 + 10 + 10 + 10).toFloat(),
                -1
            )
        }
    }
}