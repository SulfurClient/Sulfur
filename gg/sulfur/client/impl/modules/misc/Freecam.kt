package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.impl.events.MovementEvent
import gg.sulfur.client.impl.utils.movement.MotionUtils
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.enums.PacketDirection
import gg.sulfur.client.impl.events.BlockCollisionEvent
import net.minecraft.network.Packet

/**
 * @author Kansio
 * @created 10:01 AM
 * @project Client
 */
class Freecam(moduleData: ModuleData?) : Module(moduleData) {

    private val sendAlives = BooleanValue("Cancel", this, false)
    private val speed = NumberValue("Speed", this, 4.0, 0.0, 10.0)

    var x = 0.0
    var y = 0.0
    var z = 0.0

    init {
        register(sendAlives, speed)
    }

    override fun onEnable() {
        mc.thePlayer.noClip = true

        //log xyz
        x = mc.thePlayer.posX
        y = mc.thePlayer.posY
        z = mc.thePlayer.posZ
    }

    override fun onDisable() {
        //set their pos back, otherwise they'll send a packet saying they're at their freecam location
        mc.thePlayer.setPosition(x, y, z)

        //prevent player getting flung to hong kong china when toggling off
        mc.thePlayer.motionY = 0.0
        mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionZ = 0.0
        mc.thePlayer.noClip = false
    }

    @Subscribe
    fun onMove(event: MovementEvent) {
        //set motion y
        event.motionY =
            if (mc.gameSettings.keyBindJump.isKeyPressed)
                1.0
            else if (mc.gameSettings.keyBindSneak.isKeyPressed)
                -1.0
            else
                0.0

        MotionUtils.setMotion(event, speed.value)
    }

    @Subscribe
    fun onPacket(event: PacketEvent) {
        val packet = event.getPacket<Packet>()
        if (event.packetDirection == PacketDirection.OUTBOUND) {
            event.isCancelled = true
        }
    }

    @Subscribe
    fun onCollide(event: BlockCollisionEvent) {
        event.isCancelled = true
    }
}