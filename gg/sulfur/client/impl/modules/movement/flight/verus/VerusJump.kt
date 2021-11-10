package gg.sulfur.client.impl.modules.movement.flight.verus

import gg.sulfur.client.impl.modules.movement.flight.FlightMode
import gg.sulfur.client.impl.events.BlockCollisionEvent
import gg.sulfur.client.impl.events.UpdateEvent
import net.minecraft.block.BlockAir
import gg.sulfur.client.impl.utils.MinecraftInstance
import net.minecraft.util.AxisAlignedBB

/**
 * @author Kansio
 * @created 10:55 PM
 * @project Client
 */
class VerusJump : FlightMode("Verus Jump") {

    var startY = 0.0

    override fun onCollide(event: BlockCollisionEvent?) {
        if (event!!.block is BlockAir) {
            if (event.y <= startY) {
                val x = event.x.toDouble()
                val y = event.y.toDouble()
                val z = event.z.toDouble()
                if (y < mc.thePlayer.posY) {
                    event.axisAlignedBB = AxisAlignedBB.fromBounds(-5.0, -1.0, -5.0, 5.0, 1.0, 5.0).offset(x, y, z)
                }
            }
        }
    }

    override fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump()
        }
    }

    override fun onEnable() {
        startY = mc.thePlayer.posY
    }
}