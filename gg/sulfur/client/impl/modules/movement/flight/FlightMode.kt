package gg.sulfur.client.impl.modules.movement.flight

import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.property.Value
import gg.sulfur.client.impl.events.BlockCollisionEvent
import gg.sulfur.client.impl.events.MovementEvent
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.modules.movement.Flight
import gg.sulfur.client.impl.utils.ClassUtils
import gg.sulfur.client.impl.utils.MinecraftInstance

/**
 * @author Kansio
 * @created 5:23 PM
 * @project Client
 */
abstract class FlightMode(val modeName: String) : MinecraftInstance() {

    open fun onUpdate(event: UpdateEvent?) {}
    open fun onMove(event: MovementEvent?) {}
    open fun onPacket(event: PacketEvent?) {}
    open fun onCollide(event: BlockCollisionEvent?) {}
    open fun onEnable() {}
    open fun onDisable() {}

    protected val flight: Flight
        get() = Sulfur.getInstance().moduleManager[Flight::class.java]!!

    //Credit: FDPClient
    open val values: List<Value<*>>
        get() = ClassUtils.getValues(this.javaClass, this)
}