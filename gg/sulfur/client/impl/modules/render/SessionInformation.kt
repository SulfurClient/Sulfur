package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.RenderHUDEvent
import gg.sulfur.client.impl.events.enums.PacketDirection
import gg.sulfur.client.impl.utils.networking.ServerUtils
import gg.sulfur.client.impl.utils.player.ChatUtil
import net.minecraft.client.gui.Gui
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S02PacketChat
import niggerlib.packetlib.Server
import java.awt.Color

/**
 * @author Kansio
 * @created 4:40.0 PM
 * @project Client
 */

class SessionInformation(moduleData: ModuleData) : Module(moduleData) {

    var startTime: Long = 0
    var totalKills = 0
    var totalDeaths = 0

    @Subscribe
    fun onRender(event: RenderHUDEvent) {
        val hud = Sulfur.getInstance().moduleManager.get<Hud>(Hud::class.java)

        var startY = 64
        var y = startY

        Gui.drawRect(5.0, startY + 5.0, 136.0, y + 68.0, Color(0, 0, 0, 105).rgb)
        Gui.drawRect(5.0, startY + 5.0, 136.0, startY + 6.0, hud.color.value.rgb)
        Gui.drawRect(9.0, startY + 15 + 9.0, 132.0, startY + 15 + 8.0, Color(67, 67, 67).rgb)

        var durationInMillis: Long = System.currentTimeMillis() - startTime

        val millis: Long = durationInMillis % 1000
        val second: Long = durationInMillis / 1000 % 60
        val minute: Long = durationInMillis / (1000 * 60) % 60
        val hour: Long = durationInMillis / (1000 * 60 * 60) % 24

        val time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis)

        mc.fontRendererObj.drawString("Session Information: ", 8.0.toFloat(), (startY + 10.0).toFloat(), -1)
        mc.fontRendererObj.drawString("Playtime: $time", 8.0.toFloat(), (startY + 32.0).toFloat(), -1)
        mc.fontRendererObj.drawString("Total Kills: $totalKills", 8.0.toFloat(), (startY + 32.0 + 10).toFloat(), -1)
        mc.fontRendererObj.drawString(
            "Total Deaths: $totalDeaths",
            8.0.toFloat(),
            (startY + 32.0 + 10 + 10).toFloat(),
            -1
        )
    }

    @Subscribe
    fun onChatReceive(event: PacketEvent) {
        if (event.getPacket<Packet>() is S02PacketChat) {
            var packet: S02PacketChat = event.getPacket()
            var msg: String = packet.chatComponent.unformattedText

            if (!ServerUtils.isOnServer()) return

            when (ServerUtils.getServer().lowercase()) {
                "ghostly.live" -> {
                    if (msg.startsWith(mc.thePlayer.name) && msg.contains("was slain by")) {
                        totalDeaths++
                        return
                    }
                    if (msg.contains("was slain by") && msg.contains(mc.thePlayer.name)) {
                        totalKills++
                    }
                }
                "blocksmc.com" -> {
                    if (msg.startsWith(mc.thePlayer.name) && msg.contains("was killed by")) {
                        totalDeaths++
                        return
                    }
                    if (msg.contains("was killed by") && msg.contains(mc.thePlayer.name)) {
                        totalKills++
                    }
                }
            }
        }
    }

    init {
        startTime = System.currentTimeMillis()
    }
}