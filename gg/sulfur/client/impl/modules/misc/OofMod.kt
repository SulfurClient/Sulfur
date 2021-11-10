package gg.sulfur.client.impl.modules.misc

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.api.property.impl.EnumValue
import gg.sulfur.client.api.property.impl.StringValue
import gg.sulfur.client.api.property.impl.interfaces.INameable
import gg.sulfur.client.impl.events.PacketEvent
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.util.ResourceLocation
import java.util.*
import java.util.function.Consumer

/**
 * @author DivineEnergy
 * @date 11/4/2021
 * @for Sulfur
 * @at 1:10 PM EST
 */
class OofMod(moduleData: ModuleData?) : Module(moduleData) {

    private val killMessages = listOf(
        "foi jogado no void por",
        "morreu para",
        "was slain by",
        "slain by",
        "was killed by",
        "hit the ground too hard thanks to",
        "was shot by"
    )
    val mode : StringValue = StringValue("Mode", this, "Roblox", "Nword1", "Nword2", "Yoda", "Windows")

    @Subscribe
    fun onChatReceive(event: PacketEvent) {
        if (event.getPacket<Packet>() is S02PacketChat) {
            val msg = (event.getPacket<Packet>() as S02PacketChat).chatComponent.unformattedText
            val split = msg.split(" ".toRegex()).toTypedArray()
            killMessages.forEach(Consumer { oofmodtrigger: String? ->
                if (msg.contains(oofmodtrigger!!) && msg.contains(mc.thePlayer.name) && !split[0].equals(
                        mc.thePlayer.name,
                        ignoreCase = true
                    )
                ) {
                    playSound(mode.value)
                }
            })
        }
    }

    fun playSound(mode : String) {
        mc.soundHandler
            .playSound(PositionedSoundRecord.createPositionedSoundRecord(ResourceLocation("oofmod." + mode.lowercase(
                Locale.getDefault()
            )), 1.0f))
        return;
    }

    init {
        register(mode)
    }

}