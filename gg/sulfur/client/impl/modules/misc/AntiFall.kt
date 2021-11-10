package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.utils.networking.PacketUtil
import gg.sulfur.client.impl.utils.time.Stopwatch
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import org.lwjgl.input.Keyboard

/**
 * @author Kansio
 * @created 11:45 AM
 * @project Client
 */
class AntiFall(moduleData: ModuleData?) : Module(moduleData) {
    private val amount = NumberValue("Fall Distance", this, 3.0, 0.0, 30.0)
    private val pressKey = BooleanValue("On Control press", this, true)
    private val stopwatch = Stopwatch()
    override fun onEnable() {
        super.onEnable()
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent?) {
        if (mc.thePlayer.fallDistance > amount.value.toFloat()) {
            if (pressKey.value) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    PacketUtil.sendPacketNoEvent(
                        C04PacketPlayerPosition(
                            mc.thePlayer.posX,
                            mc.thePlayer.posY + 1,
                            mc.thePlayer.posZ,
                            true
                        )
                    )
                }
            } else {
                PacketUtil.sendPacketNoEvent(
                    C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY + 1,
                        mc.thePlayer.posZ,
                        true
                    )
                )
            }
        }
    }

    init {
        register(amount, pressKey)
    }
}