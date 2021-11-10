package gg.sulfur.client.impl.modules.combat

import gg.sulfur.client.api.module.ModuleData
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.api.property.impl.StringValue
import gg.sulfur.client.impl.events.DoCriticalEvent
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer

class Criticals(moduleData: ModuleData?) : Module(moduleData) {

    var mode = StringValue("Mode", this, "Verus", "Vanilla", "Mini")
    var useC06 = BooleanValue("Use C06", this, true)

    private var canCrit = false

    fun sendPacket(xOffset: Double = 0.0, yOffset: Double = 0.0, zOffset: Double = 0.0, ground: Boolean) {
        val x = mc.thePlayer.posX + xOffset
        val y = mc.thePlayer.posY + yOffset
        val z = mc.thePlayer.posZ + zOffset
        if (useC06.value) {
            mc.netHandler.addToSendQueue(
                C03PacketPlayer.C06PacketPlayerPosLook(
                    x,
                    y,
                    z,
                    mc.thePlayer.rotationYaw,
                    mc.thePlayer.rotationPitch,
                    ground
                )
            )
        } else {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, ground))
        }
    }

    @Subscribe
    fun onCrit(event: DoCriticalEvent) {
        if (event.isCancelled) return

        when (mode.value) {
            "Verus" -> {
                sendPacket(yOffset = 0.11, ground = false)
                sendPacket(yOffset = 0.1100013579, ground = false)
                sendPacket(yOffset = 0.0000013579, ground = false)
            }
            "Ghostly" -> {

            }
            "Vanilla"-> {
                mc.thePlayer.sendQueue.addToSendQueue(
                    C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY + 0.11,
                        mc.thePlayer.posZ,
                        false
                    )
                )
                mc.thePlayer.sendQueue.addToSendQueue(
                    C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY + 0.1100013579,
                        mc.thePlayer.posZ,
                        false
                    )
                )
                mc.thePlayer.sendQueue.addToSendQueue(
                    C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY + 0.0000013579,
                        mc.thePlayer.posZ,
                        false
                    )
                )
            }
            "Mini" -> {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY += 0.40
                }
            }
        }
    }

    override fun getSuffix(): String {
        return " " + mode.value
    }

    init {
        register(mode, useC06)
    }
}