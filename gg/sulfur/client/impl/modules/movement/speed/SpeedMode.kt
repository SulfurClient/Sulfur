package gg.sulfur.client.impl.modules.movement.speed

import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.property.Value
import gg.sulfur.client.impl.events.MovementEvent
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.UpdateEvent
import gg.sulfur.client.impl.modules.movement.Speed
import gg.sulfur.client.impl.utils.ClassUtils
import gg.sulfur.client.impl.utils.MinecraftInstance
import java.util.*

/**
 * @author Kansio
 * @created 1:07 AM
 * @project Client
 */
abstract class SpeedMode(val modeName: String) : MinecraftInstance() {

    open fun onUpdate(event: UpdateEvent?) {}
    open fun onMove(event: MovementEvent?) {}
    open fun onPacket(event: PacketEvent?) {}
    open fun onEnable() {}
    open fun onDisable() {}

    open fun register(vararg properties: Value<*>?) {
        Collections.addAll(Sulfur.getInstance().valueManager.objects, *properties)
    }

    protected val speed: Speed
        get() = Sulfur.getInstance().moduleManager[Speed::class.java]!!

    //Credit: FDPClient
    open val values: List<Value<*>>
        get() = ClassUtils.getValues(this.javaClass, this)
}