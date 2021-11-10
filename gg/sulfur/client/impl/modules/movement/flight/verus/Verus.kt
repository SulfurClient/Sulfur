package gg.sulfur.client.impl.modules.movement.flight.verus

import gg.sulfur.client.impl.modules.movement.flight.FlightMode
import gg.sulfur.client.impl.events.BlockCollisionEvent
import net.minecraft.block.BlockAir
import gg.sulfur.client.impl.utils.MinecraftInstance
import net.minecraft.util.AxisAlignedBB
import gg.sulfur.client.impl.events.MovementEvent
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.utils.movement.MotionUtils

/**
 * @author Kansio
 * @created 10:51 PM
 * @project Client
 */
class Verus : FlightMode("Verus") {

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

    override fun onMove(event: MovementEvent?) {
        if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava && !mc.thePlayer.isInWater && !mc.thePlayer.isOnLadder && mc.thePlayer.ridingEntity == null && mc.thePlayer.hurtTime < 1) {
            if (mc.thePlayer.isMoving) {
                mc.gameSettings.keyBindJump.pressed = false
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump()
                    mc.thePlayer.motionY = 0.0
                    MotionUtils.strafe(0.61f)
                    event!!.motionY = 0.41999998688698
                }
                MotionUtils.strafe()
            }
        }
    }
}