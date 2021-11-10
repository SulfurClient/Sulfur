package gg.sulfur.client.impl.modules.misc

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.utils.combat.MathUtils
import gg.sulfur.client.impl.utils.time.Stopwatch
import org.apache.commons.lang3.RandomStringUtils

class Spammer(moduleData: ModuleData) : Module(moduleData) {

    var speed = NumberValue("Speed", this, 100.0, 1.0, 20000.0, true)
    var stopwatch = Stopwatch()

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        if (stopwatch.timeElapsed(speed.value.toLong())) {
            val message2 = "discord\u061C.\u061Cgg/Rh54sFgS - ni\u061Cgger" + " [" + RandomStringUtils.random(8) + "]"
            mc.thePlayer.sendChatMessage(message2)
            stopwatch.resetTime()
        }
    }

    init {
        register(speed)
    }

}