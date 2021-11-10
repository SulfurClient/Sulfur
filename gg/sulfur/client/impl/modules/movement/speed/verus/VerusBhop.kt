package gg.sulfur.client.impl.modules.movement.speed.verus

import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.impl.events.MovementEvent
import gg.sulfur.client.impl.modules.movement.Speed
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode
import gg.sulfur.client.impl.utils.movement.MotionUtils
import net.minecraft.potion.Potion


/**
 * @author Kansio
 * @created 1:33 AM
 * @project Client
 */
class VerusBhop : SpeedMode("Verus") {

    var value = 0
    var speed2: Speed? = null
    var boost: BooleanValue? = null

    override fun onEnable() {
        speed2 = this.speed
        boost = BooleanValue("Boost", speed2, true)
        if (!Sulfur.getInstance().valueManager.getValuesFromOwner(this.speed).contains(boost)) {
            this.speed.register(boost)
        }
    }

    override fun onDisable() {
        if (Sulfur.getInstance().valueManager.getValuesFromOwner(this.speed).contains(boost)) {
            this.speed.unRegister(boost)
        }
    }

    override fun onMove(event: MovementEvent?) {
        if (mc.gameSettings.keyBindBack.pressed) {
            return
        }
        if (mc.thePlayer.isMovingOnGround) {
            event!!.motionY = MotionUtils.getMotion(0.42f).also { mc.thePlayer.motionY = it }
        }

        var sped2 = if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) 0.365 else 0.355

        if (mc.thePlayer.hurtTime >= 1) {
            sped2 = speed.speed.value.toDouble()
        }

        MotionUtils.setMotion(event, sped2)
    }
}