package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.NumberValue
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.property.impl.BooleanValue
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.UpdateEvent
import net.minecraft.network.play.client.C03PacketPlayer
import gg.sulfur.client.impl.events.enums.PacketDirection
import gg.sulfur.client.impl.utils.player.ChatUtil
import net.minecraft.network.Packet
import java.util.*

class Blink(moduleData: ModuleData?) : Module(moduleData) {

    var allPackets = BooleanValue("All Packets", this, true)
    var tick = BooleanValue("Tick", this, true)
    var tickRate: NumberValue = NumberValue("Tick Rate", this, 0.0, 1.0, 20.0, true, tick)
    var toggle = BooleanValue("Toggle Blinking on Tick", this, true, tick)
    var savedPackets = ArrayList<Packet>()
    var blinking = false

    override fun onEnable() {
        savedPackets.clear()
        if (!tick.value) {
            blinking = true
        }
    }

    override fun onDisable() {
        savedPackets.clear()
        stopBlink()
    }

    @Subscribe
    fun onPacket(event: PacketEvent) {
        if (!allPackets.value) {
            if (blinking) {
                if (event.getPacket<Packet>() is C03PacketPlayer) {
                    savedPackets.add(event.getPacket())
                    event.isCancelled = true
                }
            }
        } else {
            if (blinking) {
                if (event.packetDirection != PacketDirection.INBOUND) {
                    savedPackets.add(event.getPacket())
                    event.isCancelled = true
                }
            }
        }
    }

    @Subscribe
    fun onUpdate(event: UpdateEvent?) {
        if (tick.value) {
            if (!toggle.value) {
                if (mc.thePlayer.ticksExisted % tickRate.getCastedValue<Number>().toInt() == 0) {
                    blinking = true
                } else {
                    stopBlink()
                }
            } else {
                if (mc.thePlayer.ticksExisted % tickRate.getCastedValue<Number>().toInt() == 0) {
                    if (blinking) {
                        stopBlink()
                    } else {
                        blinking = true
                    }
                }
            }
        }
    }

    fun startBlink() {
        blinking = true
    }

    fun stopBlink() {
        if (savedPackets.size != 0) {
            ChatUtil.displayMessage(String.format("Sending %s packets", savedPackets.size))
        }
        for (packet in savedPackets) {
            mc.thePlayer.sendQueue.addToSendQueue(packet)
        }
        println("Sending " + Arrays.toString(savedPackets.toTypedArray()))
        savedPackets.clear()
        blinking = false
    }

    init {
        register(allPackets, tick, tickRate, toggle)
    }
}