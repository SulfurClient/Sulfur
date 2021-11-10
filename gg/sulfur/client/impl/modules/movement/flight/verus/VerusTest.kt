package gg.sulfur.client.impl.modules.movement.flight.verus

import gg.sulfur.client.Sulfur
import gg.sulfur.client.impl.events.BlockCollisionEvent
import gg.sulfur.client.impl.events.MovementEvent
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.modules.movement.flight.FlightMode
import gg.sulfur.client.impl.notification.type.NotificationType
import gg.sulfur.client.impl.utils.movement.MotionUtils
import net.minecraft.block.BlockAir
import net.minecraft.util.AxisAlignedBB

/**
 * @author Kansio
 * @created 9:57 PM
 * @project Client
 */
class VerusTest : FlightMode("Vanerus") {

    var speedy = 0.0
    var ticks = 0
    var boosted = false

    override fun onEnable() {
    }

    override fun onDisable() {
        speedy = 0.0;
    }

    override fun onMove(event: MovementEvent?) {
        if (mc.thePlayer.ticksExisted % 4 == 0) {
            MotionUtils.setMotion(event, 1.44)
        }
    }

    override fun onCollide(event: BlockCollisionEvent?) {
        if (event!!.block is BlockAir) {
            if (mc.thePlayer.isSneaking) return
            val x = event.x.toDouble()
            val y = event.y.toDouble()
            val z = event.z.toDouble()
            if (y < mc.thePlayer.posY) {
                event.axisAlignedBB = AxisAlignedBB.fromBounds(-5.0, -1.0, -5.0, 5.0, 1.0, 5.0).offset(x, y, z)
            }
        }
    }
}