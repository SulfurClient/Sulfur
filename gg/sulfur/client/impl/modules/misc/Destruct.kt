package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.impl.modules.render.Hud
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.impl.modules.player.Panic
import org.lwjgl.opengl.Display

/**
 * @author Kansio
 * @created 11:04 PM
 * @project Client
 */
class Destruct(moduleData: ModuleData?) : Module(moduleData) {

    override fun onEnable() {
        val hud = Sulfur.getInstance().moduleManager.get<Hud>(Hud::class.java)
        val panic = Sulfur.getInstance().moduleManager.get<Panic>(Panic::class.java)
        hud.arraylistMode.value = Hud.Theme.LEGIT
        panic.toggle()
        Display.setTitle("Lunar Client (1.8.8-48f42de/master)")
    }
}