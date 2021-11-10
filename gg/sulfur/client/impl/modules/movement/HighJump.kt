package gg.sulfur.client.impl.modules.movement

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import gg.sulfur.client.api.property.impl.StringValue
import gg.sulfur.client.api.util.Util
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.notification.type.NotificationType
import gg.sulfur.client.impl.utils.movement.MotionUtils
import gg.sulfur.client.impl.utils.networking.PacketUtil
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C0BPacketEntityAction

/**
 * @author Kansio
 * @created 6:58 PM
 * @project Client
 */
class HighJump(moduleData: ModuleData) : Module(moduleData) {

    val mode: StringValue = StringValue("Mode", this, "Verus")
    val up: NumberValue = NumberValue("Verus Upwards", this, 0.8, 0.05, 6.0)
    val speed: NumberValue = NumberValue("Verus Speed", this, 1.45, 0.05, 10.0)

    var launched = false
    var wasLaunched = false
    var jumped = false

    init {
        register(mode, up, speed)
    }

    override fun onEnable() {
        when (mode.value) {
            "Verus" -> {
                if (!mc.thePlayer.onGround) {
                    Sulfur.getInstance().notificationManager.postNotification("You must be on ground to use this module", NotificationType.INFO)
                    toggle()
                }
                mc.timer.timerSpeed = 0.3f
                MotionUtils.damageVerus()
            }
        }
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
        jumped = false
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        when (mode.value) {
            "Verus" -> {
                if (mc.thePlayer.hurtTime > 1 && !launched) {
                    launched = true
                }
                if (launched) {
                    if (!jumped) {
                        mc.thePlayer.motionY = up.value
                        jumped = true
                    }
                    MotionUtils.setMotion(speed.value)
                    launched = false
                    wasLaunched = true
                    toggle()
                }
            }
        }
    }
}