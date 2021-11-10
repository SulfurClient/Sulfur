package gg.sulfur.client.impl.modules.misc

import com.google.common.eventbus.Subscribe
import gg.sulfur.client.api.module.Module
import gg.sulfur.client.api.module.ModuleData
import gg.sulfur.client.impl.events.DeathEvent
import gg.sulfur.client.impl.events.PacketEvent
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.util.ResourceLocation
import java.util.*
import java.util.function.Consumer

class DeathSounds(moduleData: ModuleData?) : Module(moduleData) {

    private val killMessages = listOf(
        "foi jogado no void por",
        "morreu para",
        "slain by",
        "was killed by",
        "hit the ground too hard thanks to",
        "was shot by"
    )

    @Subscribe
    fun onDeathScreen(event: DeathEvent?) {
        playSound()
    }

    @Subscribe
    fun onChatReceive(event: PacketEvent) {
        if (event.getPacket<Packet>() is S02PacketChat) {
            val msg = (event.getPacket<Packet>() as S02PacketChat).chatComponent.unformattedText
            val split = msg.split(" ".toRegex()).toTypedArray()
            killMessages.forEach(Consumer { oofmodtrigger: String? ->
                if (msg.contains(oofmodtrigger!!) && msg.contains(mc.thePlayer.name) && split[0].equals(
                        mc.thePlayer.name,
                        ignoreCase = true
                    )
                ) {
                    playSound()
                }
            })
        }
    }

    fun playSound() {
        mc.soundHandler
            .playSound(
                PositionedSoundRecord.createPositionedSoundRecord(
                    ResourceLocation("wasted.gta"), 1.0f))
        return;
    }
}