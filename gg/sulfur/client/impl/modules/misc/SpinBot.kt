package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.modules.combat.Aura
import gg.sulfur.client.impl.modules.render.Rotations

class SpinBot(data: ModuleData) : Module(data) {

    var yaw = 0f
    var speed = NumberValue("Speed", this, 0.0, 0.3, 100.0)
    var lookDown = BooleanValue("Down", this, false)

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        if (Sulfur.getInstance().moduleManager.getByName("Kill Aura").isToggled && Aura.target != null) return
        if (Sulfur.getInstance().moduleManager.getByName("Scaffold").isToggled) return

        if (yaw >= 359.00) {
            yaw = 0f
        }
        yaw += speed.value.toFloat()
        event.rotationYaw = yaw
        if (lookDown.value) {
            event.rotationPitch = 90f
        }
        if (Sulfur.getInstance().moduleManager.isEnabled(Rotations::class.java)) {
            if (lookDown.value) mc.thePlayer.renderPitchHead = 90f
            mc.thePlayer.renderYawOffset = yaw
            mc.thePlayer.renderYawHead = yaw
        }
    }

    init {
        register(speed, lookDown)
    }
}