package gg.sulfur.client.impl.modules.movement

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import gg.sulfur.client.api.property.impl.StringValue
import gg.sulfur.client.impl.events.BlockCollisionEvent
import gg.sulfur.client.impl.events.MovementEvent
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.modules.movement.flight.FlightMode
import gg.sulfur.client.impl.modules.movement.speed.SpeedMode
import gg.sulfur.client.impl.utils.ReflectUtils
import gg.sulfur.client.impl.utils.java.ReflectUtils.newInstance
import gg.sulfur.client.impl.utils.movement.MotionUtils


/**
 * @author Kansio
 * @created 5:24 PM
 * @project Client
 */

class Flight(moduleData: ModuleData?) : Module(moduleData) {

    //Credit: FDPClient (the Reflection stuff)
    private val modes = ReflectUtils.getReflects("${this.javaClass.`package`.name}.flight", FlightMode::class.java).map { it.newInstance() as FlightMode }.sortedBy { it.modeName }
    val mode: FlightMode get() = modes.find { modeValue.equals(it.modeName) } ?: throw NullPointerException() // this should not happen


    private val modeValue = StringValue("Mode", this, *modes.map { it.modeName }.toTypedArray())
    val speed = NumberValue("Speed", this, 1.0, 0.0, 10.0);

    init {
        register(modeValue, speed)
    }

    override fun onEnable() {
        mode.onEnable()
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
        MotionUtils.setMotion(0.0)

        mode.onDisable()
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent) {
        mode.onUpdate(event)
    }

    @Subscribe
    fun onMove(event: MovementEvent) {
        mode.onMove(event)
    }

    @Subscribe
    fun onPacket(event: PacketEvent) {
        mode.onPacket(event)
    }

    @Subscribe
    fun onCollide(event: BlockCollisionEvent) {
        mode.onCollide(event)
    }

    override fun getSuffix(): String {
        return " ${mode.modeName}"
    }
}