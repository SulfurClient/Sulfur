package gg.sulfur.client.impl.modules.render

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.impl.events.RenderHUDEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import java.awt.Color


/**
 * @author Kansio
 * @created 1:12 AM
 * @project Client
 */

class GameInformation(data: ModuleData) : Module(data) {

    @Subscribe
    fun onRender(event: RenderHUDEvent) {
        val hud = Sulfur.getInstance().moduleManager.get<Hud>(Hud::class.java)

        val packet = Sulfur.getInstance().moduleManager.get<PacketCounter>(PacketCounter::class.java)
        val spotify = Sulfur.getInstance().moduleManager.get<Spotify>(Spotify::class.java)
        val sessionInformation =
            Sulfur.getInstance().moduleManager.get<SessionInformation>(SessionInformation::class.java)

        var startY = 64
        var y = startY
        var x = 0

        if (packet.isToggled || spotify.isToggled || sessionInformation.isToggled) {
            x = 150;
        }

        Gui.drawRect(x + 5.0, startY + 5.0, x + 166.0, y + 68.0, Color(0, 0, 0, 105).rgb)
        Gui.drawRect(x + 5.0, startY + 5.0, x + 166.0, startY + 6.0, hud.color.value.rgb)
        Gui.drawRect(x + 9.0, startY + 15 + 9.0, x + 162.0, startY + 15 + 8.0, Color(67, 67, 67).rgb)


        mc.fontRendererObj.drawString("Game Information: ", x + 8.0.toFloat(), (startY + 10.0).toFloat(), -1)
        mc.fontRendererObj.drawString("FPS: ${Minecraft.debugFPS}", x + 8.0.toFloat(), (startY + 32.0).toFloat(), -1)
        mc.fontRendererObj.drawString(
            "GPU: ${Sulfur.getInstance().gpu}",
            x + 8.0.toFloat(),
            (startY + 32.0 + 10).toFloat(),
            -1
        )
        mc.fontRendererObj.drawString(
            "Config: " + if (Sulfur.getInstance().currentConfig.equals("")) "none" else Sulfur.getInstance().currentConfig,
            x + 8.0.toFloat(),
            (startY + 32.0 + 10 + 10).toFloat(),
            -1
        )
    }

}