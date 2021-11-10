package gg.sulfur.client.impl.modules.misc

import gg.sulfur.client.api.module.ModuleData
import com.google.common.eventbus.Subscribe
import gg.sulfur.client.impl.events.PacketEvent
import gg.sulfur.client.impl.events.enums.PacketDirection
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.util.IChatComponent
import net.minecraft.util.ChatComponentText
import gg.sulfur.client.impl.modules.misc.NameProtect
import gg.sulfur.client.Sulfur
import gg.sulfur.client.api.module.Module
import net.minecraft.network.Packet

class NameProtect(moduleData: ModuleData?) : Module(moduleData) {

    @Subscribe
    fun onPacket(packetEvent: PacketEvent) {
        if (packetEvent.packetDirection == PacketDirection.INBOUND) {
            if (packetEvent.getPacket<Packet>() is S02PacketChat) {
                val chat = packetEvent.getPacket<S02PacketChat>()
                val chatComponent = chat.chatComponent
                if (chatComponent is ChatComponentText && chat.isChat) {
                    println(chatComponent.getFormattedText())
                    val text = chatComponent.getFormattedText().replace(mc.thePlayer.name, protectedName)
                    packetEvent.setPacket(S02PacketChat(ChatComponentText(text)))
                }
            }
        }
    }

    companion object {
        @JvmField
        var protectedName = "You" // TODO save nameprotect name like module settings
        val isEnabled: Boolean
            get() = Sulfur.getInstance().moduleManager.get<Module>(NameProtect::class.java).isToggled
    }
}