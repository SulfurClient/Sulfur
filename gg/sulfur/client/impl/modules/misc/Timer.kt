package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.impl.events.UpdateEvent

class Timer(moduleData: ModuleData?) : Module(moduleData) {

    private val timer = NumberValue("Timer", this, 2.0, 0.1, 10.0)
    private val tick = BooleanValue("Tick", this, false)
    private val tickRate = NumberValue("Tick Rate", this, 5.0, 1.0, 20.0, true, tick)

    @Subscribe
    fun onUpdate(event: UpdateEvent?) {
        if (tick.value) {
            if (mc.thePlayer.ticksExisted % tickRate.getCastedValue<Number>().toInt() == 0) {
                //ChatUtil.displayMessage("called");
                mc.timer.timerSpeed = timer.getCastedValue<Number>().toFloat()
            } else {
                mc.timer.timerSpeed = 1f
            }
        } else {
            mc.timer.timerSpeed = timer.getCastedValue<Number>().toFloat()
        }
    }

    init {
        register(timer, tick, tickRate)
    }
}